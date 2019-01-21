(ns hiccup-bootstrap.3.components
  (:require [clojure.string :as string]
            [hiccup.def :refer [defelem]]))

(def bs-styles #{:info :danger :success :warning})

(defn- classes [m]
  (->> m
       (filter second)
       (map (comp #(cond->> %
                     (not (vector? %)) vector
                     true (remove nil?)
                     true (map (fn [x]
                                 (try (name x)
                                      (catch Exception e x))))
                     true (string/join "-"))
                  first))
       (string/join " ")))

(defn container [{:keys [fluid?]} & content]
  [:div {:class (classes {[:container (when fluid? :fluid)] true})}
   content])

(def caret [:span.caret])

(defelem glyphicon [glyph]
  [:span {:class (classes {:glyphicon true
                           [:glyphicon glyph] glyph})
          :aria-hidden true}])

(defelem navbar-text [& content]
  [:p.navbar-text content])

(defelem navbar-dropdown [caption & items]
  [:li.dropdown
   [:a.dropdown-toggle {:data-toggle :dropdown, :href "#"}
    caption " " caret]
   [:ul.dropdown-menu {} items]])

(defelem badge [& content]
  [:span.badge content])

(defn navbar
  [{:keys [bs-style brand brand-link component-element
           fluid? position fixed static]
    :or {bs-style :default
         component-element :nav
         brand-link "#"
         brand "Brand"}}
   & content]
  [component-element
   {:class (classes {:navbar true
                     [:navbar bs-style] bs-style
                     [:navbar :fixed fixed] fixed
                     [:navbar :static static] static})}
   (container
    {:fluid? fluid?}
    [:div.navbar-header
     [:button {:type :button
               :class (classes {:navbar-toggle true
                                :collapsed true})
               :data-toggle :collapse
               :data-target "#navbar-collapse1"
               :aria-expanded "false"}
      [:span.sr-only "Toggle navigation"]
      (repeat 3 [:span.icon-bar])]
     [:a.navbar-brand {:href brand-link} brand]]
    [:div.navbar-collapse.collapse
     {:id "navbar-collapse1"}
     content])])

(defn list-group-item [{:keys [href heading text bs-style content active
                               disabled]} & ccontent]
  [(if href :a :div)
   {:class (classes {:list-group-item true
                     [:list :group :item bs-style] bs-style
                     :active active
                     :disabled disabled})
    :href href}
   (when heading [:h4.st-group-item-heading heading])
   (when text [:p.list-group-item-text text])
   (or content ccontent)])

(defn list-group [items]
  [:div.list-group
   (for [item items]
     (if (map? item)
       (list-group-item item)
       (list-group-item {} item)))])

(defn label [{:keys [bs-style content]
              :or {bs-style :default}}
             & ccontent]
  [:span {:class (classes {:label true
                           [:label bs-style] true})}
   (or content ccontent)])
