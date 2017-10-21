(ns datawalk.datawalk
  "Transforms data"
  (:require
            ;; Temporary for dev:
            [datawalk.print :as pr]
            ))

;;;;;;; State:

;; We track current data,
(def data (atom nil))

;; the root / starting-point
(def the-root (atom nil))

;; the paths from root to each visited node (we save them all so we can
;; know the correct path when we time travel),
(def paths (atom {}))

;; a map of values to return on exit,
(def saved (atom {}))

;; the past (visited nodes),
(def the-past (atom []))

;; and the future.
(def the-future (atom []))

;; Throughout: d = data

;;;;;;; Low-level helpers

(defn- not-set [x]
  (if (set? x) (vec x) x))

;; The past and the future are both stacks; to move backward or forward, we pop
;; something off one stack, push current data onto the other, and return the
;; popped value as the new present
;; from & to are past & future in some order
(defn- time-travel [from-time present to-time]
  (if (seq @to-time)
    (let [new-present (peek @to-time)]
      (swap! from-time conj present)
      (swap! to-time pop)
      new-present)
    ;; [(conj from-time present) (peek to-time) (pop to-time)]
    (do (println "You have reached the end of time. You shall go no further.\n")
        present)))

;;;;;;; High-level helpers

(defn save [item]
  (let [next-index (count @saved)]
    (swap! saved (assoc next-index item))))

;; (defn- move-forward [past present future]
;;   (time-travel past present future))

;; (defn- move-backward [past present future]
;;   (reverse ; we reverse because time-travel always returns [from-time present to-time]
;;    (time-travel future present past)))

;;;;;;; User API

;; All API fns take data as their final argument, and return
;; updated data. Changes to the paths, saved, the-past, and the-future atoms are
;; made inline as side effects.

(defn no-op [data]
  data)

(defn drill
  "Given a number n, drill down to that numbered item"
  [n data]
  ;; (println "drilling into" data)
  (cond (sequential? data)
        , (let [next-data (nth data n)
                ;; _ (println "conjing (in seq) onto" (type @paths))
                next-path (conj (@paths data) n)]
            (swap! paths assoc (not-set next-data) next-path)
            next-data)
        (map? data)
        , (let [;_ (println "nonsequential data is a" (type data))
                ks (keys data)
                k (nth ks n)
                ;; _ (println "conjing (in map) onto" (type (@paths data)))
                next-data (get data k)
                next-path (conj (@paths data) k)]
            ;; (println "k:" k)
            (swap! paths assoc (not-set next-data) next-path)
            next-data)
        :else ; not drillable; no-op
        , (do (println "Can't drill into a" (type data))
              data)))

(defn exit [data]
  ;; Returns saved data
  @saved)

(defn exit-with-current [data]
  data)

(defn save-current [data]
  ;; saved is a map containing numeric indices (for easy retrieval) & arbitrary
  ;; items
  (let [next-index (count @saved)]
    (swap! saved assoc next-index data)))

(defn save-path [data]
  (save-current (@paths data))
  data)

(defn backward [data]
  (time-travel the-future data the-past))

(defn forward [data]
  (time-travel the-past data the-future))

(defn root [data]
  @root)

;; TODO maybe?
(defn up [data])

(def help-text
  {"#" "Enter any listed number to drill to that item"
   "q" "exit ; exit and return saved values if any"
   "x" "exit-with-current ; exit & return just this ent"
   "s" "save-current ; save to map of return values"
   "v" "save-path ; save path to map of return values"
   "b" "backward ; step backward in history"
   "f" "forward ; step forward in history"
   "r" "root ; jump back to root"
   "u" "up ; step upward [provides list of referring entities]"
   "h" "help ; print help & return same ent"
   "p" "print-path ; path: print path to current item."
   "!" "function ; call an arbitrary 1-arg fn on data, jump to result"
   })

(defn print-help [ent]
  (println "COMMANDS:")
  (run! println help-text)
  (println)
  ent)

(defn print-path [data]
  (println "PATH:" (@paths data))
  (println)
  data)

(defn function [data])
