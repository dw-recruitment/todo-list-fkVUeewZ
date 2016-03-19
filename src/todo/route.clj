(ns todo.route
  (:require [compojure.core :refer [GET context defroutes routes]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [site-defaults
                                              wrap-defaults]]
            [ring.util.response :as response]
            [todo.handler :as handler]))

(defroutes ^:private resource-routes
  "Public resource (asset) routes"
  (route/resources "/"))

(defn- www-routes
  "Create non-API web application routes"
  [{conn :datomic}]
  (routes
    (GET "/" [] (handler/index-get conn))
    (GET "/about" [] (handler/about-get))
    (route/not-found (response/not-found "<html><body><h1>Not Found</h1></body></html>"))))

(defn app-routes
  "Create the main entrypoint into the web application"
  [system]
  (routes
    (context "/public" [] resource-routes)
    (-> (www-routes system)
        (wrap-defaults site-defaults))))
