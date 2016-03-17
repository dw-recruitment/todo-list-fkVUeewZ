(ns todo.handler
  (:require [ring.util.response :as response]))

(defn index-get
  [_]
  (-> (response/resource-response "index.html" {:root "html"})
      (response/content-type "text/html")))
