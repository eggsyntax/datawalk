(ns datawalk.core
  (:require [datawalk.datawalk :as dw]
            [datawalk.print    :as pr]
            [datawalk.parse    :as ps]
            [clojure.string :as string]
            ;; Maybe later, for attempting read-line in cljs
            #_[clojure.tools.reader :as rdr]
            #_[clojure.tools.reader.reader-types :as rdrt]
            )
  #?(:cljs (:require-macros [datawalk.core :refer [w]])))

;; Dependency tree
;; core
;;   datawalk (includes all state)
;;     (print)
;;   parse
;;     (datawalk)
;;   print
;;     util

;; Notes:
;; - Throughout: d = data
;; - Assumption: the order of (keys m) will be consistent during a session.
;;   Empirically, this seems to be true, although I wouldn't necessarily trust
;;   it in production code.
;; - Goals:
;;   - Get some fspecs in there -- hmm, can I access before-and-after values
;;     for the state atoms?
;;   - This is based on an even more special-purpose program I wrote to explore
;;     Datomic data, which took heavy advantage of the laziness of Datomic's
;;     EntityMap. I'd ideally like to bring some of that over to datawalk at
;;     some point.
;; - ClojureScript ideas:
;;   - Would use a customized clojure.main repl, but AFAIK it won't work w cljs.
;;   - A key challenge for making this work in cljs is that generic user input
;;     isn't available in cljs. I brought it up in #clojurescript, and found the
;;     following resources:
;;     - https://clojurians.slack.com/archives/C03S1L9DN/p1508602683000025
;;       - discussion goes at least until 2:30pm
;;       - note especially: `:special-fns` get processed by clj
;;     - https://github.com/abiocljs (@mfikes)
;;     - https://github.com/potetm/tire-iron
;;     - https://www.google.com/search?q=IReplEnvOption
;;   - As far as I can tell, special fns are generally defined by the repl, so it
;;     may not be possible to inject them for my own needs unless I create my own
;;     repl.
;;     - see https://github.com/bhauman/lein-figwheel/blob/bddbcc0fe326ea1e4aafb649a0b95c9113377611/sidecar/src/figwheel_sidecar/system.clj
;;   - Other options:
;;     - I could just make a totally ordinary fn and make users use that at the
;;     cljs repl -- ie just print it out, and then do
;;     user> (x b) ; backward
;;     user> (x 3) ; drill to item 3
;;     ...and so on.
;;     - I could make users run in a specific env, eg planck or node. Unfortunately,
;;       reading input seems to be wildly inconsistent across browsers (eg
;;       SpiderMonkey has `read-line`), so I couldn't even make something that
;;       worked in all browser repls :(
;;     - I could make users copy-paste over into clj :(

(def prompt "[datawalk] > ")

(def ^:private exit-command? #{dw/quit dw/exit-with-current})

;; Commands (in addition to drill) which advance the time step
(def ^:private time-stepping? #{dw/root dw/up dw/function})

(defn initialize-state [d]
  (reset! dw/data d)
  (reset! dw/the-root d)
  (reset! dw/paths {d []}) ; Start with (empty) path to root
  (reset! dw/saved {})
  (reset! dw/the-past [])
  (reset! dw/the-future [])
  )

(defn print-globals [] (pr/print-globals  (@dw/paths @dw/data) @dw/saved @dw/the-past @dw/the-future))

(defn- read-input
  "Get user input (at repl) -- later this needs to be generalized for both clj
  and the various cljs environments."
  []
  (flush)
  (let [input #?(:clj (read-line)
                 :cljs nil)] ; TODO (see cljs section of README)
    (println input)
    input))

(defn datawalk
  "Run a single step of exploration. Inner fn called by both `repl` and `w`.
  Takes a data structure to act on, and input to parse and act on."
  [data in]
  (when pr/*debug-mode* (print-globals))
  (let [f (ps/parse in)
        next-data (if f (f data) data)]
    ;; We store data in an atom only so that it can be referred
    ;; to elsewhere; we don't need it in this fn.
    (reset! dw/data next-data)
    (pr/print-data next-data)
    (if (exit-command? f)
      ;; TODO - control what's output. Could potentially bypass datawalk entirely
      ;; & just handle in repl, since the exit commands aren't really relevant
      ;; except in the true repl
      :exit ; used as signal to stop looping
      (do (when (and (or (ps/read-int in) (time-stepping? f))
                     (not= data next-data)) ; complicaton from various edge cases
            (swap! dw/the-past conj data)
            (reset! dw/the-future []))
          next-data))))

;; datawalk essentially has two versions, a fully-interactive version which
;; (currently) only runs in Clojure, and a semi-interactive version which runs
;; anywhere. See README for more details. In short, `repl` is the fully-
;; interactive version, and `look-at` + `w` is the semi-interactive. Note that
;; the two versions are equal in power; the only advantage of the fully-
;; interactive version is that it requires typing fewer characters.

#?(:clj
   (defn repl
     "Runs a small, self-contained, fully-interactive REPL for exploring data
  (Clojure-only for the moment)."
     [d]
     (println "Exploring interactively.\n")
     (initialize-state d)
     (pr/print-data d)
     (loop [data d]
       (when pr/*debug-mode* (print-globals))
       (print prompt)
       (flush) ; no-op in cljs
       (let [in (read-input)]
         (cond
           (= "q" in) @dw/saved
           (= "x" in) data
           :else (recur (datawalk data in)))))))


(defn look-at
  "Initializes the semi-interactive version. Typically you should call look-at
  once, and then w many times. Usable in all environments."
  [d]
  (println "Exploring semi-interactively.
Now that you've initialized the data, use w to continue.
(w h) will give you a summary of available commands.\n")
  (initialize-state d)
  (pr/print-data d))

(defmacro w
  "Take a single step through the data, using any of the commands. For example,
  [datawalk] > (w 2) ; drill to item 2
  [datawalk] > (w p) ; print path to current data
  [datawalk] > (w b) ; step backward
  Use (w h) to get a summary of available commands. w presumes you've already
  called `look-at` once to specify what data is being explored.
  Usable in all environments."
  [& args]
  (let [string-args# (mapv str args)]
    `(if @dw/data
       (datawalk @dw/data ~@string-args#)
       (println "No data to explore. Perhaps you haven't called look-at?"))
    nil))


(comment
  ;; fully-interactive
  (repl {:a 1 :b {:c #{2 3 4} :d "5" :e [6 7 {:f "8" :g {:h :9}}]}})

  ;; semi-interactive
  (look-at {:a 1 :b {:c #{2 3 4} :d "5" :e [6 7 {:f "8" :g {:h :9}}]}})
  ;; followed by any number of calls to w

  )
