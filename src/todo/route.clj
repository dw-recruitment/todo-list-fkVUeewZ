(ns todo.route
  (:require [compojure.core :refer [GET context defroutes]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [site-defaults
                                              wrap-defaults]]
            [ring.util.response :as response]
            [todo.handler :as handler]))

(defroutes ^:private resource-routes
  "Public resource (asset) routes"
  (route/resources "/"))

(defroutes ^:private www-routes
  "Application routes"
  (GET "/" request handler/index-get)
  (GET "/about" request handler/about-get)
  (route/not-found (response/not-found "<html><body><h1>Not Found</h1></body></html>")))

(defroutes routes
  "Main entrypoint into the web application"
  (context "/public" [] resource-routes)
  (-> www-routes
      (wrap-defaults site-defaults)))
