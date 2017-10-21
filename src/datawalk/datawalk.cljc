(ns datawalk.datawalk
  "Transforms data"
  (:require [datawalk.state :as st]
            ;; Temporary for dev:
            [datawalk.print :as pr]
            ))

;; Throughout: d = data

;; Low-level helpers

(defn- not-set [x]
  (if (set? x) (vec x) x))

;; High-level helpers

(defn save [item]
  (let [next-index (count @st/saved)]
    (swap! st/saved (assoc next-index item))))

(defn reset-data! [d]
  (reset! st/data d))

;; User API

(defn exit []
  ;; Print final stuff (maybe just passed in as d)
  (reset-data! nil))

(defn drill
  "Given a number n, drill down to that numbered item"
  [n]
  ;; (println "drilling into" data)
  (let [paths @st/paths
        data  @st/data]
    (cond (sequential? data)
         , (let [next-data (nth data n)
                 ;; _ (println "conjing (in seq) onto" (type paths))
                 next-path (conj (paths data) n)]
             (swap! st/paths assoc (not-set next-data) next-path)
             next-data)
         (map? data)
         , (let [;_ (println "nonsequential data is a" (type data))
                 ks (keys data)
                 _ (println "ks:" ks)
                 k (nth ks n)
                 ;; _ (println "conjing (in map) onto" (type (paths data)))
                 next-data (get data k)
                 _ (prn "next-data" next-data)
                 _ (prn "k:" k)
                 next-path (conj (get data paths) k)]
             _ (prn "next-path" next-path)
             ;; (println "k:" k)
             (swap! st/paths assoc (not-set next-data) next-path)
             next-data)
         :else ; not drillable; no-op
         , (do (println "Can't drill into a" (type data))
               data))))

(defn exit-with-current [])

(defn save-current [])

(defn save-path [])

(defn backward [])

(defn forward [])

(defn root [])

;; TODO maybe?
(defn up [])

(defn help [])

(defn print-path [])

(defn function [])


;; Dev helpers
(defn init []
  (do (reset-data! {:a 1 :b {:c 2 :d 3}})
      (reset! st/paths {})
      (reset! st/saved {})))
