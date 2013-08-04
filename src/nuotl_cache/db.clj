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
        end-date (plus start-date (months 1))
        events (map
                  (fn [event] (assoc event :tweeter (coll/find-one-as-map "tweeter" {:_id (:tweeter event)})))
                  (coll/find-maps "event" {:start {$gte start-date $lt end-date}}))]
    (debug (format "Retrieving events for %s/%s: " year month))
    events
    ))

(defn remove-event [id]
  (debug (str "Deleting event with id " id))
  (debug (coll/remove-by-id "event" id))
  {:deleted id}
  )

(defn get-tweeters []
  (coll/find-maps "tweeter"))

(defn get-tweeter [id]
  (coll/find-map-by-id "tweeter" id))

(defn get-areas []
  (coll/find-maps "area"))

(defn add-event [event]
  (debug (str "Adding event " event))
  (coll/save "event"
             (merge event {:start (parse (event :start)) :end (parse (event :end))}))
  event)

(defn add-reply [reply]
  (debug (str "Adding reply " reply))
  (coll/save "reply" reply)
  reply
  )

(defn get-reply [id]
  (let [result (coll/find-map-by-id "reply" id)]
    (debug "Retrieved reply for id " id ": " result)
    result
    ))

(defn remove-reply [id]
  (debug (str "Deleting reply with id " id))
  (coll/remove-by-id "reply" id)
  {:deleted id}
  )

(defn add-or-update-tweeter [tweeter]
  (debug (str "Adding/updating tweeter: " tweeter))
  (coll/update "tweeter" (select-keys tweeter [:_id]) tweeter :upsert true)
  tweeter
  )
