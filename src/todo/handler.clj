(ns todo.handler
  (:require [todo.db :as db]
            [todo.html :as html]))

(defn index-get
  [conn]
  (html/index
    (for [l (db/lists conn)]
      (assoc l :items (db/items conn (:id l))))))

(defn about-get
  []
  (html/about))

(defn lists-post
  [{params :params} conn]
  (str (db/create-list! conn (db/->List nil (:name params)))))

(defn items-post
  [{params :params} conn]
  (let [list-id (Long/parseLong (:list-id params))]
    (str (db/create-item! conn
                          list-id
                          (db/->Item nil (:text params) :todo list-id)))))

(defn item-put
  [{params :params} conn]
  (str (db/update-item-state! conn
                              (Long/parseLong (:id params))
                              (keyword (:state params)))))

(defn item-delete
  [{params :params} conn]
  (str (db/delete-item! conn
                        (Long/parseLong (:id params)))))
