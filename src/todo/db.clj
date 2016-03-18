(ns todo.db
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [datomic.api :as d])
  (:import [datomic Util]))

(defrecord Item [text state])

(def item-states
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

(defn create-item!
  "Add a to-do list item to the database."
  [conn item]
  @(d/transact conn [{:db/id #db/id[:db.part/user]
                      :item/text (:text item)
                      :item/state {:db/ident (item-states (:state item))}}]))
