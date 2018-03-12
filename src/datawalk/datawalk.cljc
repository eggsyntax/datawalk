(ns datawalk.datawalk
  "Transforms data"
  (:require [clojure.pprint :refer [pprint]]
            [datawalk.datawalkable :refer [Datawalkable dw-drill]]
            [datawalk.print :as pr]
            [datawalk.util :as u]))

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
    (do (println "**** You have reached the end of time. You shall go no further. ****\n")
        present)))

;;;;;;; User API

;; All API fns take data as their final argument, and return updated data.
;; Changes to paths, saved, the-past, and the-future atoms are made inline as
;; side effects.

(defn no-op [data]
  data)

;; TODO pull `(conj (@paths data) n)` out into a helper fn to clarify that
;; it's the same for all of them. Or maybe that PLUS the swap, although there's
;; the slight variation of whether u/not-set is called.
;; But also see line 110.
;; (defn update-paths! data n)

(defn drill-seqable [n data]
  (let [next-data (first (drop n data))
        next-path (conj (@paths data) n)]
    (swap! paths assoc (u/not-set next-data) next-path)
    next-data))

(defn drill-sequential [n data]
  (let [next-data (nth data n)
        next-path (conj (@paths data) n)]
    (swap! paths assoc (u/not-set next-data) next-path)
    next-data))

(defn drill-set [n data]
  (let [next-data (nth (u/not-set data) n)
        next-path (conj (@paths data) n)]
    (swap! paths assoc next-data next-path)
    next-data))

(defn drill-map [n data]
  (let [k (nth (keys data) n)
        next-data (get data k)
        next-path (conj (@paths data) k)]
    (swap! paths assoc (u/not-set next-data) next-path)
    next-data))

(defn drill-blocking-derefable
  "Drilling into a future/promise dereferences it with a fast timeout so we
  don't block if it doesn't contain a value yet."
  [n data]
  #?(:clj (if-let [next-data (deref data 100 nil)]
            (do (swap! paths assoc
                       (u/not-set next-data)
                       (conj (@paths data) 'deref))
                next-data)
            (do (prn "Can't deref, no data yet!")
                data))))

(defn drill-derefable [n data]
  (let [next-data (deref data)]
    (swap! paths assoc
           (u/not-set next-data)
           (conj (@paths data) 'deref))
    next-data))

;; TODO separate swapping to the path out of the individual drill functions;
;; otherwise, users will add protocols for dw-drill without knowing to swap, and
;; therefore the path will not be extended on those types.

(defn drill
  "Given a number n, drill down to that numbered item"
  [n data]
   ;; (println "drilling into" data)
  (try
    ;; Note that we have to switch argument order from here down. drill takes [n data]
    ;; so that we can create (partial drill n). But dw-drill, as a protocol function, expects
    ;; its first argument to determine the type -- so we have to swap.
    (dw-drill data n)
    (catch #?(:clj IndexOutOfBoundsException
              :cljs js/Error) e
      (do (println "\nThere is no item numbered" n "in the list of current data. Try again.\n")
          data))))

(defn quit [data]
  ;; Returns saved data
  @saved)

(defn exit-with-current [data]
  data)

(defn- save-to-saved-map
  [data]
  (let [next-index (+ 1 (count @saved))]
    (swap! saved assoc next-index data)
    data))

(defn save-current
  "Save the current data for later availability. @saved is a map containing
  numeric indices (for easy retrieval) & arbitrary items. It's 1-indexed for
  ease of keyboarding, and to serve as a reminder that this is in some sense not
  a 'natural' index but an arbitrarily constructed one (eg we could have just as
  easily indexed by a, b, c...). "
  [data]
  (println "Saving current data to saved-data map")
  (save-to-saved-map data))

(defn save-path [data]
  (println "Saving the path to current to saved-data map")
  (save-to-saved-map (@paths data))
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
  ["q:  quit              ; exit and return saved values if any (repl-only)"
   "x:  exit-with-current ; exit & return just this ent (repl-only)"
   "s:  save-current      ; save to map of return values"
   "v:  save-path         ; save path to map of return values"
   "b:  backward          ; step backward in history"
   "f:  forward           ; step forward in history"
   "r:  root              ; jump back to root"
   "u:  up                ; step upward [provides list of referring entities]"
   "h:  help              ; print help & return same ent"
   "p:  print-path        ; path: print path to current item."
   "m:  print-saved-map   ; print data saved so far"
   "c:  print-full-cur    ; print the current data in full, not truncated"
   "!:  function          ; call a 1-arg fn on data, jump to result (clj only)"])


(defn help [ent]
  (println "COMMANDS:")
  (println "#  Enter any listed number to drill to that item")
  (run! println help-text)
  (println)
  ent)

(defn print-path [data]
  (println "PATH:")
  (println (@paths data))
  (println)
  data)

(defn prn-saved-map [data]
  (println "SAVED:")
  (prn @saved)
  (println)
  data)

(defn pprint-saved-map [data]
  (println "SAVED:")
  (pprint @saved)
  (println)
  data)

(defn prn-full-cur [data]
  (println "CURRENT:")
  (prn data)
  (println)
  data)

(defn pprint-full-cur [data]
  (println "CURRENT:")
  (pprint data)
  (println)
  data)

#?(:clj (defn- read-input [prompt]
          (print prompt)
          (let [input (read-line)]
            (println input)
            input)))

(defn function [data]
  #?(:clj
     (let [_ (println "Please enter a function of one variable")
           inp (read-input "Enter a fn >> ")
           f (eval (read-string inp))]
       (if (contains? #{"q" ""} inp) ; in case they want to abort
         data
         (if (fn? f)
           (f data) ; typical outcome
           (do (println inp "is not a function; try again.")
               (function data)))))
     :cljs
     (do (println "! command is only available in Clojure.")
         (println "  In cljs, just call a fn on the data as you usually would.")
         (println)
         data)))
