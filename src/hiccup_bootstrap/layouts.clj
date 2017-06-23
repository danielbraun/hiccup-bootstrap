(ns hiccup-bootstrap.layouts)

(defn basic [{:keys [language-code title content extra-head extra-script
                     jquery-url base-url]
              :or {content "Bootstrap body content"
                   base-url "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7"
                   jquery-url "//ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"
                   title "Bootstrap template title"}}]
  (list
    "<!DOCTYPE html>"
    [:html {:lang language-code}
     [:head
      [:meta {:charset :utf-8}]
      [:meta {:content "IE=edge"
              :http-equiv "X-UA-Compatible"}]
      [:meta {:name "viewport"
              :content "width=device-width, initial-scale=1"}]
      [:title title]
      [:link {:href (str base-url "/css/bootstrap.min.css")
              :rel :stylesheet}]
      "<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->"
      "<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->"
      "<!--[if lt IE 9]>"
      [:script {:src "https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"}]
      [:script {:src "https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"}]
      "<![endif]-->"
      extra-head]
     [:body
      content
      [:script {:src jquery-url}]
      [:script {:src (str base-url "/js/bootstrap.min.js")}]
      extra-script]]))
