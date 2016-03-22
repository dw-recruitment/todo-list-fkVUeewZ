(ns todo.db
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.set :refer [map-invert]]
            [datomic.api :as d])
  (:import [datomic Util]))

(defrecord List [id name])

(defrecord Item [id text state list-id])

(def item-state->datomic
  "A map of item states to their equivalents in Datomic."
  {:todo :item.state/todo
   :done :item.state/done})

(defn read-datomic-edn
  "Loads a datomic EDN file relative to the resource path."
  [path]
  (->> path io/resource slurp (edn/read-string {:readers *data-readers*})))

(defn lists
  "Retrieve all to-do lists."
  [conn]
  (map (partial apply ->List)
       (d/q '[:find ?list ?name
              :where [?list :list/name ?name]]
            (d/db conn))))

(defn create-list!
  "Create a new to-do list."
  [conn list]
  (let [tempid (d/tempid :db.part/user)
        tx [{:db/id tempid
             :list/name (:name list)}]
        result @(d/transact conn tx)]
    (d/resolve-tempid (d/db conn) (:tempids result) tempid)))

(defn items
  "Retrieve the to-do items in a given list."
  [conn list-id]
  (let [datomic->item-state (map-invert item-state->datomic)]
    (map (fn [[id t s l]] (->Item id t (datomic->item-state s) l))
         (d/q '[:find ?item ?text ?state ?list
                :in $ ?list
                :where [?item :item/list ?list]
                       [?item :item/text ?text]
                       [?item :item/state ?state-ref]
                       [?state-ref :db/ident ?state]]
              (d/db conn)
              list-id))))

(defn create-item!
  "Adds a to-do list item to the database and returns its entity id."
  [conn list-id item]
  (let [tempid (d/tempid :db.part/user)
        tx [{:db/id tempid
             :item/text (:text item)
             :item/state {:db/ident (item-state->datomic (:state item))}
             :item/list list-id}]
        result @(d/transact conn tx)]
    (d/resolve-tempid (d/db conn) (:tempids result) tempid)))

(defn update-item-state!
  "Updates a to-do list item’s state in the database and returns its entity id."
  [conn id new-state]
  (let [tx [{:db/id id
             :item/state {:db/ident (item-state->datomic new-state)}}]]
    @(d/transact conn tx)
    id))

(defn delete-item!
  "Deletes (retracts) a to-do list item from the database."
  [conn id]
  (let [tx [[:db.fn/retractEntity id]]]
    @(d/transact conn tx)
    id))
