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

(defn get-events [year month]
  (let [start-date (date-time year month)
        end-date (plus start-date (months 1))]
    (coll/find-maps "event" {:start {$gte start-date $lt end-date}})))
