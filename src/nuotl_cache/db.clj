(ns nuotl-cache.db
  (:require [monger.core :as core]
            [monger.collection :as coll]
            [monger.joda-time]
            ))

(core/connect!)
(core/set-db! (core/get-db "nuotl"))

(defn get-events []
  (coll/find-maps "event"))

(defn get-tweeters []
  (coll/find-maps "tweeter"))
