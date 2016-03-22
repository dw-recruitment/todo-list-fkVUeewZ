(ns todo.core
  (:require [datomic.api :as d]
            [todo.db :as db]
            [todo.route :as route]
            [immutant.web :as web]))

(defn ->system
  "Create and initialize stateful components of the system.

  In a more complex application, this would be replaced with something like
  https://github.com/stuartsierra/component"
  [db-uri]
  {:datomic (d/connect db-uri)})

(defn -main
  [& args]
  ; Some of these could use environment variables but for simplicity we’ll
  ; hardcode.
  (let [db-uri "datomic:mem://todo"
        schema-path "datomic/todo-schema.edn"
        data-path "datomic/todo-data.edn"
        dev-mode? (not (System/getenv "LEIN_NO_DEV"))]
    ; Create the Datomic database anew since it is in-memory.
    (d/create-database db-uri)
    (let [{conn :datomic :as system} (->system db-uri)]
      @(d/transact conn (db/read-datomic-edn schema-path))
      @(d/transact conn (db/read-datomic-edn data-path))
      (if dev-mode?
        (web/run-dmc (route/app-routes system))
        (web/run (route/app-routes system))))))
