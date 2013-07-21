(ns nuotl-cache.index
    (:require [clj-time.core :as time]
              [ring.adapter.jetty :as jetty]
              [ring.util.response :refer [response]]
              [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
              [compojure.core :refer [GET ANY POST defroutes]]
              [compojure.handler :refer [site]]
              [nuotl-cache.db :as db]
              [clj-yaml.core :as yaml]
              )
    (:gen-class))

(def areas
  (read-string (slurp (clojure.java.io/resource "areas.edn"))))

(defroutes app-routes
  (GET "/events/:year/:month" [year month] (response (db/get-events
                                                        (read-string year)
                                                        (read-string month))))
  (GET "/tweeters" [] (response (db/get-tweeters)))
  (GET "/areas" [] (response areas))
  (POST "/events" {body :body} (db/add-event body))
  (ANY "*" [] (response {:message "404"})))

(def app
  (->
   (site app-routes)
   (wrap-json-body {:keywords? true})
   (wrap-json-response)))

(defn -main [& args]
  (let [config (yaml/parse-string (slurp (nth args 0)))]
    (db/connect-to-db config)
    (jetty/run-jetty app {:port ((config :http) :port)})))
