(ns todo.core
  (:require [datomic.api :as d]
            [todo.db :as db]
            [todo.route :as route]
            [immutant.web :as web]))

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
    (let [conn (d/connect db-uri)
          system {:datomic conn}]
      (db/install-schema! conn (db/read-schema schema-path))
      (doseq [d (db/read-data data-path)]
        (db/create-item! conn d))
      (if dev-mode?
        (web/run-dmc (route/app-routes system))
        (web/run (route/app-routes system))))))
