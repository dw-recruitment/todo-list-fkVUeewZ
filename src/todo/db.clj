(ns todo.db
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.set :refer [map-invert]]
            [datomic.api :as d])
  (:import [datomic Util]))

(defrecord Item [text state])

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
    (map (fn [[t s]] (->Item t (datomic->item-state s)))
         (d/q '[:find ?text ?state
                :where [?item :item/text ?text]
                       [?item :item/state ?state-ref]
                       [?state-ref :db/ident ?state]]
              (d/db conn)))))

(defn create-item!
  "Add a to-do list item to the database."
  [conn item]
  @(d/transact conn
               [{:db/id #db/id[:db.part/user]
                 :item/text (:text item)
                 :item/state {:db/ident (item-state->datomic (:state item))}}]))
