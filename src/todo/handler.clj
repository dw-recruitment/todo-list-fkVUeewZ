(ns todo.handler
  (:require [todo.db :as db]
            [todo.html :as html]))

(defn index-get
  [conn]
  (html/index (db/items conn)))

(defn about-get
  []
  (html/about))

(defn items-post
  [{params :params} conn]
  (str (db/create-item! conn (db/->Item nil (:text params) :todo))))

(defn item-put
  [{params :params} conn]
  (str (db/update-item-state! conn (Long/parseLong (:id params)) (keyword (:state params)))))
