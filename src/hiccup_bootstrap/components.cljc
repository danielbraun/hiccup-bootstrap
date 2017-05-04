(ns hiccup-bootstrap.components
  (:require [clojure.string :as string]))

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
