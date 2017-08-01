(ns hiccup-bootstrap.components
  (:require [clojure.string :as string]
            [hiccup-bootstrap.def :refer [defcomponent]]))

(defcomponent container
  [{:keys [fluid?]}
   content]
  [:div {:class {[:container (when fluid? :fluid)] true}}
   content])

(defcomponent nav-item
  [{:keys [active? disabled? href]
    :or {href "#"}}
   content]
  [:li {:role :presentation
        :class {:active active?
                :disabled disabled?}}
   [:a {:href href}
    content
    (when active? [:span.sr-only "(current)"])]])

(defcomponent nav
  [{:keys [bs-style stacked? justified? navbar?]}
   content]
  [:ul {:class {:nav true
                [:nav bs-style] bs-style
                [:nav :stacked] stacked?
                [:nav :justified] justified?
                [:navbar :nav] navbar?}}
   content])

(defcomponent navbar
  [{:keys [bs-style brand brand-link component-element
           fluid? position fixed static]
    :or {bs-style :default
         component-element :nav
         brand-link "#"
         brand "Brand"}}
   content]
  [component-element
   {:class {:navbar true
            [:navbar bs-style] bs-style
            [:navbar :fixed fixed] fixed
            [:navbar :static static] static}}
   (container
     {:fluid? fluid?}
     [:div.navbar-header
      [:button {:type :button
                :class {:navbar-toggle true
                        :collapsed true}
                :data-toggle :collapse
                :data-target "#navbar-collapse1"
                :aria-expanded "false"}
       [:span.sr-only "Toggle navigation"]
       (repeat 3 [:span.icon-bar])]
      [:a.navbar-brand {:href brand-link} brand]]
     [:div.navbar-collapse.collapse
      {:id "navbar-collapse1"}
      content])])

(defn- join-classes [classes]
  (string/join " " classes))

(defn panel [{:keys [footer header bs-style collapsible? expanded?
                     title attrs]
              :or {bs-style :default}}
             & children]
  [(if collapsible? :details :div)
   (merge {:class (join-classes ["panel"
                                 (when bs-style (str "panel-" (name bs-style)))])
           :open expanded?}
          attrs)
   (when (or title header)
     [(if collapsible? :summary :div) {:class :panel-heading}
      (if title [:span.panel-title " " title] header)])
   [:div.panel-body children]
   (when footer [:div.panel-footer footer])])

(defn tabs [tab-maps]
  [:div
   [:ul.nav.nav-tabs {:role :tablist}
    (for [{:keys [id title active? disabled?]} tab-maps]
      [:li {:role :presentation
            :class (clojure.string/join " "
                                        [(when active? "active")
                                         (when disabled? "disabled")])}
       [:a {:href (when id (str "#" (name id)))
            :aria-controls id
            :role :tab
            :data-toggle :tab} title]])]
   [:div.tab-content
    (for [{:keys [content active? id]} tab-maps]
      [:div.tab-pane {:role :tabpanel
                      :class (when active? "active")
                      :id id} content])]])

(defcomponent pagination
  [{:keys [items active-page href max-buttons
           first? last? next? previous? parameter-name
           url extra parameter-name]
    :or {items 1
         url ""
         href identity
         max-buttons 5
         first? false
         last? false
         next? true
         previous? true}} _]
  (let [page-link
        (fn [i caption]
          [:li {:class
                {:active (and (integer? caption) (= i active-page))
                 :disabled (and (string? caption)
                                (or (not (pos? i))
                                    (= i active-page)
                                    (> i items)))}}
           [:a {:href (href i), :title i} caption]])]
    [:nav [:ul.pagination
           (when first? (page-link 1 "&laquo;"))
           (when previous? (page-link (dec active-page) "‹"))
           (for [i (if max-buttons
                     (let [start (- active-page (int (/ max-buttons 2)))]
                       (range start (+ start max-buttons)))
                     (range 1 (inc items)))
                 :when (<= 1 i items)]
             (page-link i i))
           (when next? (page-link (inc active-page) "›"))
           (when last? (page-link items "&raquo;"))]]))
