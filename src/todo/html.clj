(ns todo.html
  (:require [hiccup.page :refer [html5 include-css]]))

(defn application
  [title & content]
  (html5 {:lang "en"}
         [:head
          [:meta {:charset "utf-8"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
          [:title title]
          (include-css "/public/vendor/pure/0.6.0/pure-min.css")
          (include-css "/public/styles/todo.css")]
         [:body
          [:div.main
           [:div.pure-menu.pure-menu-horizontal
            [:a.pure-menu-heading.pure-menu-link {:href "/"} "TODO"]
            [:ul.pure-menu-list
             [:li.pure-menu-item
              [:a.pure-menu-link {:href "/about"} "About"]]]]
           content]]))

(defn index
  []
  (application "democracy.works TODO assignment - Home"
                [:h1
                 [:a {:href "http://democracy.works/"} "democracy.works"]
                 " TODO application"]
                [:img.center.pure-img {:src "/public/assets/under-construction.gif"
                                       :alt "Broken image icon falling on worker"}]))

(defn about
  []
  (application "democracy.works TODO assignment - About"
               [:h1 "About TODO"]
               [:p "Say goodbye to "
                   [:a {:href "https://trello.com/"
                        :title "But really, I love Trello"}
                    "Trello"]
                   " - we are disrupting the to-do list marketplace. If you’re an"
                   " investor looking to get in on the ground floor, now’s the time."]
               [:h2 "Limited only by your imagination"]
               [:ul
                [:li "Grocery lists"]
                [:li "Medication reminders"]
                [:li [:i "The rest is up to you..."]]]))
