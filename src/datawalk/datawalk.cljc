(ns datawalk.datawalk
  "Transforms data"
  (:require
            ;; Temporary for dev:
            [datawalk.print :as pr]
            [datawalk.util :as u]
            ))

;;;;;;; State:

;; We track current data,
(def data (atom nil)) ; we don't rely on this except for non-interactive use

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

;; (I originally wrote a small initial version of datawalk that passed all of
;; these around at every recur step, differently for each (or most) operations.
;; It works fine, but it's clumsier and less readable, so I'm happy to use a few
;; namespaced globals for a small, self-contained project like this.)

;;;;;;; Helpers

;; The past and the future are both stacks; to move backward or forward, we pop
;; something off one stack, push current data onto the other, and return the
;; popped value as the new present.
;; `from-time` and `to-time` are the-past & the-future in some order. `present`
;; is just the current data, which may not yet be part of the-past or the-future.
(defn- time-travel [from-time present to-time]
  (if (seq @to-time)
    (let [new-present (peek @to-time)]
      (swap! from-time conj present)
      (swap! to-time pop)
      new-present)
    (do (println "You have reached the end of time. You shall go no further.\n")
        present)))

;;;;;;; User API

;; All API fns take data as their final argument, and return updated data.
;; Changes to paths, saved, the-past, and the-future atoms are made inline as
;; side effects.

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
            (swap! paths assoc (u/not-set next-data) next-path)
            next-data)
        (set? data)
        , (let [next-data (nth (u/not-set data) n)
                next-path (conj (@paths data) n)]
            (swap! paths assoc next-data next-path)
            next-data)
        (map? data)
        , (let [;_ (println "nonsequential data is a" (type data))
                ks (keys data)
                k (nth ks n)
                ;; _ (println "conjing (in map) onto" (type (@paths data)))
                next-data (get data k)
                next-path (conj (@paths data) k)]
            ;; (println "k:" k)
            (swap! paths assoc (u/not-set next-data) next-path)
            next-data)
        :else ; not drillable; no-op
        , (do (println "Can't drill into a" (type data))
              (swap! the-past pop) ; we haven't moved; avoid dup
              data)))

(defn quit [data]
  ;; Returns saved data
  @saved)

(defn exit-with-current [data]
  data)

(defn save-current [data]
  ;; saved is a map containing numeric indices (for easy retrieval) & arbitrary
  ;; items
  (let [next-index (count @saved)]
    (swap! saved assoc next-index data)
    data))

(defn save-path [data]
  (save-current (@paths data))
  data)

(defn backward [data]
  (time-travel the-future data the-past))

(defn forward [data]
  (time-travel the-past data the-future))

(defn root [data]
  @the-root)

(defn up [data]
  (get-in @the-root (butlast (@paths data))))

(def help-text
  ;; quit and exit-with-current are only relevant in the fully interactive
  ;; repl version, but this is not actually enforced in any way.
  {"#" "Enter any listed number to drill to that item"
   "q" "quit              ; exit and return saved values if any (repl-only)"
   "x" "exit-with-current ; exit & return just this ent (repl-only)"
   "s" "save-current      ; save to map of return values"
   "v" "save-path         ; save path to map of return values"
   "b" "backward          ; step backward in history"
   "f" "forward           ; step forward in history"
   "r" "root              ; jump back to root"
   "u" "up                ; step upward [provides list of referring entities]"
   "h" "help              ; print help & return same ent"
   "p" "print-path        ; path: print path to current item."
   "n" "print-saved       ; print data saved so far"
   ;; "!" "function ; call an arbitrary 1-arg fn on data, jump to result"
   })

(defn help [ent]
  (println "COMMANDS:")
  (run! println help-text)
  (println)
  ent)

(defn print-path [data]
  (println "PATH:" (@paths data))
  (println)
  data)

;; TODO
(defn print-saved [data]
  (println "SAVED:\n" (map (partial pr/limitln pr/*max-line-length*)
                           @saved))
  (println)
  data)

;; TODO implement
(defn function [data])
