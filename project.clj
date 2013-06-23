(defproject nuotl-cache "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [com.novemberain/monger "1.4.0"]
                 [clj-time "0.4.4"]
                 [cheshire "5.0.0"]
                 [ring/ring-jetty-adapter "1.1.4"]
                 [ring/ring-json "0.2.0"]
                 [org.clojure/tools.logging "0.2.3"]
                 [midje "1.4.0"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler nuotl-cache.index/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
