(ns nuotl-cache.db
  (:require [monger.core :as core]
            [monger.collection :as coll]
            [monger.joda-time] ; joda-time integration
            [monger.json]      ; cheshire integration
            [monger.operators :refer [$gte $lt]]
            [clj-time.core :refer [date-time plus months year month]]
            [clj-time.format :refer [parse]]
            [clojure.tools.logging :refer [debug]]
  ))

(defn connect-to-db [config]
  (core/connect! {:host ((config :mongo) :host) :port ((config :mongo) :port)})
  (core/set-db! (core/get-db ((config :mongo) :database))))

(defn get-events [year month]
  (let [start-date (date-time year month)
        end-date (plus start-date (months 1))]
    (map
     (fn [event] (assoc event :tweeter (coll/find-one-as-map "tweeter" {:_id (:tweeter event)})))
     (coll/find-maps "event" {:start {$gte start-date $lt end-date}}))))

(defn get-tweeters []
  (coll/find-maps "tweeter"))

(defn get-areas []
  (coll/find-maps "area"))

(defn add-event [event]
  (debug (str "Adding event " event))
  (coll/save "event"
             (merge event {:start (parse (event :start)) :end (parse (event :end))}))
  event)
