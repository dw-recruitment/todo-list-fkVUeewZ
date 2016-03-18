(ns todo.handler
  (:require [todo.html :as html]))

(defn index-get
  []
  (html/index))

(defn about-get
  []
  (html/about))
