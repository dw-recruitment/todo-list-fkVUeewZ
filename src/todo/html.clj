(ns todo.html
  (:require [hiccup.core :refer [h]]
            [hiccup.page :refer [html5 include-css include-js]]))

(def state->html
  {:done "Yes"
   :todo "No"})

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
           content]
          (include-js "/public/vendor/jquery/jquery-2.2.2.min.js")
          (include-js "/public/scripts/todo.js")]))

(defn index
  [items]
  (application "democracy.works TODO assignment - Home"
               [:h1
                [:a {:href "http://democracy.works/"} "democracy.works"]
                " TODO application"]
               [:form#item-create.pure-form {:type "POST" :action "/api/items/"}
                [:fieldset
                 [:legend "What do you need to get done?"]
                 [:input {:type "text" :placeholder "Make a beer run" :name "text"}]
                 [:button.pure-button.pure-primary {:type "submit"} "Add TODO item"]]]
               [:table#todo-items.pure-table
                [:caption "All TODO items"]
                [:thead
                 [:th "Item"]
                 [:th "Done?"]
                 [:th]]
                [:tbody
                  (for [i items]
                    [:tr {:data-item-id (str (:id i))
                          :data-item-state (name (:state i))}
                     [:td {:class (if (= (:state i) :done) "item-done" nil)}
                          (h (:text i))]
                     [:td (h (state->html (:state i)))]
                     [:td [:button {:class (if (= (:state i) :done)
                                             "pure-button pure-button-default"
                                             "pure-button pure-button-primary")}
                                   (if (= (:state i) :done) "Undo" "Done")]]])]]))

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
