(ns todo.db
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [datomic.api :as d])
  (:import [datomic Util]))

(defn load-schema
  "Loads the schema EDN file relative to the resource path."
  [path]
  (-> path io/resource io/reader Util/readAll))

(defn install-schema!
  "Installs the elements of the schema and returns a seq of the return values of
  transacting each element."
  [conn schema]
  (doall
    (for [s schema]
      @(d/transact conn s))))
