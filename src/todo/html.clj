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
  [lists]
  (application "democracy.works TODO assignment - Home"
               [:h1
                [:a {:href "http://democracy.works/"} "democracy.works"]
                " TODO application"]
               [:form#list-create.pure-form {:type "POST" :action "/api/lists/"}
                [:fieldset
                 [:input {:type "text" :placeholder "Holiday shopping items" :name "name"}]
                 "&nbsp;"
                 [:button.pure-button.pure-primary {:type "submit"} "Add new list"]]]
               (apply concat
                 (for [l lists]
                   [[:table.todo-items.pure-table {:data-list-id (:id l)}
                    [:caption (h (:name l))]
                    [:thead
                     [:th "Item"]
                     [:th "Done?"]
                     [:th]]
                    [:tbody
                      (for [i (:items l)]
                        [:tr {:data-item-id (str (:id i))
                              :data-item-state (name (:state i))}
                         [:td {:class (if (= (:state i) :done) "item-done" nil)}
                              (h (:text i))]
                         [:td (h (state->html (:state i)))]
                         [:td [:button {:class (if (= (:state i) :done)
                                                 "pure-button pure-button-default item-state-change"
                                                 "pure-button pure-button-primary item-state-change")}
                                       (if (= (:state i) :done) "Undo" "Done")]
                              "&nbsp;"
                              [:button.pure-button.pure-button-default.item-delete "Delete"]]])]]
                   [:form.item-create.pure-form {:type "POST"
                                                 :action (str "/api/lists/" (:id l) "/items/")}
                    [:fieldset
                     [:input {:type "text" :placeholder "Pick up cat food" :name "text"}]
                     "&nbsp;"
                     [:button.pure-button.pure-primary {:type "submit"} "Add item"]]]]))))

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
