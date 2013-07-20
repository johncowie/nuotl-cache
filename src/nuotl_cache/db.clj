(ns nuotl-cache.db
  (:require [monger.core :as core]
            [monger.collection :as coll]
            [monger.joda-time]
            [monger.operators :refer [$gte $lt]]
            [clj-time.core :refer [date-time plus months year month]]
  ))

(defn connect-to-db [config]
  (core/connect! {:host ((config :mongo) :host) :port ((config :mongo) :port)})
  (core/set-db! (core/get-db ((config :mongo) :database))))

;(core/connect!)
;(core/set-db! (core/get-db "nuotl"))

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
  (coll/save "event" event))

(defn add-tweeter [tweeter]
  (coll/save "tweeter" tweeter))
