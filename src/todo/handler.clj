(ns todo.handler
  (:require [ring.util.response :as response]))

(defn- html-response
  [relative-path]
  (-> (response/resource-response relative-path {:root "html"})
      (response/content-type "text/html")))

(defn index-get
  [_]
  (html-response "index.html"))

(defn about-get
  [_]
  (html-response "about.html"))
