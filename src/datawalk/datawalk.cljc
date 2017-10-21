(ns datawalk.datawalk
  "Transforms data"
  (:require
            ;; Temporary for dev:
            [datawalk.print :as pr]
            ))

;;;;;;; State:

;; We track current data,
(def data (atom nil))

;; the paths from root to each visited node (we save them all so we can
;; know the correct path when we time travel),
(def paths (atom {}))

;; and a map of values to return on exit.
(def saved (atom {}))

;; Throughout: d = data

;;;;;;; Low-level helpers

(defn- not-set [x]
  (if (set? x) (vec x) x))

;;;;;;; High-level helpers

(defn save [item]
  (let [next-index (count @saved)]
    (swap! saved (assoc next-index item))))

(defn reset-data! [d]
  (reset! data d))

;;;;;;; User API

(defn exit [data]
  ;; Print final stuff (maybe just passed in as d)
  (reset-data! nil))

(defn drill
  "Given a number n, drill down to that numbered item"
  [n data]
  ;; (println "drilling into" data)
  (let [paths @paths]
    (cond (sequential? data)
         , (let [next-data (nth data n)
                 ;; _ (println "conjing (in seq) onto" (type paths))
                 next-path (conj (paths data) n)]
             (swap! paths assoc (not-set next-data) next-path)
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
                 next-path (conj (paths data) k)]
             _ (prn "next-path" next-path)
             ;; (println "k:" k)
             (swap! paths assoc (not-set next-data) next-path)
             next-data)
         :else ; not drillable; no-op
         , (do (println "Can't drill into a" (type data))
               data))))

(defn exit-with-current [data])

(defn save-current [data])

(defn save-path [data])

(defn backward [data])

(defn forward [data])

(defn root [data])

;; TODO maybe?
(defn up [data])

(defn help [data])

(defn print-path [data])

(defn function [data])


;; Dev helpers
(defn init []
  (do (reset-data! {:a 1 :b {:c 2 :d 3}})
      (reset! paths {})
      (reset! saved {})))
