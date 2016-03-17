(ns todo.core
  (:require [todo.route :as route]
            [immutant.web :as web]))

(defn -main
  [& args]
  (let [dev-mode? (not (System/getenv "LEIN_NO_DEV"))]
    (if dev-mode?
      (web/run-dmc route/routes)
      (web/run route/routes))))
