(ns todo.handler
  (:require [todo.db :as db]
            [todo.html :as html]))

(defn index-get
  [conn]
  (html/index (db/items conn)))

(defn about-get
  []
  (html/about))
