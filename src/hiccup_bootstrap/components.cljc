(ns hiccup-bootstrap.components
  (:require [clojure.string :as string]
            [hiccup.def :refer [defelem]]
            [hiccup-bootstrap.def :refer [defcomponent]]))

(defn- join-classes
  ([prefix classes]
   (cond->> classes
     true (map name)
     prefix (map (partial str (name prefix) "-"))
     true (string/join " ")))
  ([classes]
   (join-classes nil classes)))

(defcomponent container
  [{:keys [fluid?]}
   content]
  [:div {:class {[:container (when fluid? :fluid)] true}}
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

(defelem button
  ([content] (button [:default] content))
  ([styles content] [:button.btn {:type :button
                                  :class (join-classes :btn styles)}
                     content]))

(defelem form-group [& body]
  [:div {:class :form-group} body])

(defelem button-group [& body]
  [:div.btn-group {:role :group} body])

(defelem tab-content [& body]
  [:div.tab-content body])

(defelem tab-pane [& body]
  [:div {:role :tabpanel, :class "tab-pane"} body])

(defelem pager [& body]
  [:ul.pager body])

(defelem nav
  [styles & body]
  [:ul.nav {:class (join-classes :nav styles)}
   body])

(defelem nav-item [& body]
  [:li {:role :presentation} body])

(defelem page-header [& body]
  [:div.page-header body])

(defelem row [& body]
  [:div.row])
