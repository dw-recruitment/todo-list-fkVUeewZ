(ns todo.db
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.set :refer [map-invert]]
            [datomic.api :as d])
  (:import [datomic Util]))

(defrecord Item [id text state])

(def item-state->datomic
  "A map of item states to their equivalents in Datomic."
  {:todo :item.state/todo
   :done :item.state/done})

(defn read-schema
  "Loads the schema EDN file relative to the resource path."
  [path]
  (-> path io/resource io/reader Util/readAll))

(defn read-data
  "Loads the data EDN file relative to the resource path."
  [path]
  (-> path io/resource slurp edn/read-string))

(defn install-schema!
  "Installs the elements of the schema and returns a seq of the return values of
  transacting each element."
  [conn schema]
  (doall
    (for [s schema]
      @(d/transact conn s))))

(defn items
  "Retrieve a list of to-do items."
  [conn]
  (let [datomic->item-state (map-invert item-state->datomic)]
    (map (fn [[id t s]] (->Item id t (datomic->item-state s)))
         (d/q '[:find ?item ?text ?state
                :where [?item :item/text ?text]
                       [?item :item/state ?state-ref]
                       [?state-ref :db/ident ?state]]
              (d/db conn)))))

(defn create-item!
  "Adds a to-do list item to the database and returns its entity id."
  [conn item]
  (let [tempid (d/tempid :db.part/user)
        tx [{:db/id tempid
             :item/text (:text item)
             :item/state {:db/ident (item-state->datomic (:state item))}}]
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
