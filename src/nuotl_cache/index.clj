(ns nuotl-cache.index
    (:require [clj-time.core :as time]
              [ring.adapter.jetty :as jetty]
              [ring.util.response :refer [response]]
              [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
              [compojure.core :refer [GET ANY defroutes]]
              [compojure.handler :refer [site]]
              [nuotl-cache.db :as db]
              )
    (:gen-class))

(db/get-events)

(defroutes app-routes
  (GET "/events" [] (response (db/get-events)))
  (GET "/tweeter" [] (response (db/get-tweeters)))
  (ANY "*" [] (response {:message "404"})))

(def app
  (->
   (site app-routes)
   (wrap-json-body)
   (wrap-json-response {:keywords? true})
   ))

(defn -main [& args]
  (if (not (empty? args))
    (jetty/run-jetty app {:port (read-string (first args))})
    (jetty/run-jetty app {:port 3000})))
