(ns datawalk.core
  (:require [datawalk.datawalk :as w]
            [datawalk.print    :as pr]
            [datawalk.parse    :as ps]
            [clojure.string :as string]
            #_[clojure.tools.reader :as rdr]
            #_[clojure.tools.reader.reader-types :as rdrt]
            )
  #?(:cljs (:require-macros [datawalk.core :refer [ww]])))

;; Dependencies:
;; core
;;   datawalk (contains state)
;;     util
;;     print
;;   parse
;;     (datawalk)
;;   print
;;     util


;; Notes:
;; Throughout: d = data
;; - Would use a customized clojure.main repl, but AFAIK it won't work w cljs.
;; - A key challenge for making this work in cljs is that generic user input
;;   isn't available in cljs. I brought it up in #clojurescript, and found the
;;   following resources:
;;   - https://clojurians.slack.com/archives/C03S1L9DN/p1508602683000025
;;     - discussion goes at least until 2:30pm
;;     - note especially: `:special-fns` get processed by clj
;;   - https://github.com/abiocljs (@mfikes)
;;   - https://github.com/potetm/tire-iron
;;   - https://www.google.com/search?q=IReplEnvOption
;; - Assumption: the order of (keys m) will be consistent during a session.
;;   Empirically, this seems to be true, although I wouldn't necessarily trust
;;   it in production code.
;; - AFAICT, special fns are generally defined by the repl, so it
;;   may not be possible to inject them for my own needs unless I create
;;   my own repl.
;;   - see https://github.com/bhauman/lein-figwheel/blob/bddbcc0fe326ea1e4aafb649a0b95c9113377611/sidecar/src/figwheel_sidecar/system.clj
;; - Other options:
;;   - I could just make a totally ordinary fn and make users use that at the
;;   cljs repl -- ie just print it out, and then do
;;   user> (x b) ; backward
;;   user> (x 3) ; drill to item 3
;;   ...and so on.
;;   - I could make users run in a specific env, eg planck or node. Unfortunately,
;;     reading input seems to be wildly inconsistent across browsers (eg
;;     SpiderMonkey has `read-line`), so I couldn't even make something that
;;     worked in all browser repls :(
;;   - I could make users copy-paste over into clj :(

(def prompt "[datawalk] > ")

(def ^:private exit-command? #{w/exit w/exit-with-current})

;; Commands (in addition to drill) which advance the time step
(def ^:private time-stepping? #{w/root w/up w/function})

(defn initialize [d]
  (reset! w/data d)
  (reset! w/the-root d)
  (reset! w/paths {d []}) ; Start with (empty) path to root
  (reset! w/saved {})
  (reset! w/the-past [])
  (reset! w/the-future [])
  )

(defn print-globals [] (pr/print-globals  (@w/paths @w/data) @w/saved @w/the-past @w/the-future))

(defn- read-input
  "Get user input (at repl) -- later this needs to be generalized for both clj
  and the various cljs environments."
  []
  (flush)
  (let [input #?(:clj (read-line)
                 :cljs nil)] ; TODO (see cljs section of README)
    (println input)
    input))

;; I'd like to
;; - change datawalk to non-recurring / non-input-getting
;;   - calling it by itself initializes data and then returns
;; - create `repl` fn that just calls `datawalk` and then loops & gets input
;; - create `ww` macro that parses its args as a command (as though the args
;;   were user input), executes the command (changing the various states) and
;;   returns, so that user (eg in cljs) can step through the data repeatedly
;;   like:
;;   [datawalk] > (datawalk my-data)
;;   [datawalk] > (ww 2) ; drill to item 2
;;   [datawalk] > (ww p) ; print path
;;   [datawalk] > (ww b) ; step backward

(defn datawalk
  ;; parse inp
  ;; eval
  ;; print
  "Run a single step of exploration. Inner fn called by both `repl` and `ww`.
  Takes a data structure to act on, and input to parse and act on."
  [data in]
  (when pr/*debug-mode* (print-globals))
  (let [f (ps/parse in)
        next-data (if f (f data) data)]
    ;; We store data in an atom only so that it can be referred
    ;; to elsewhere; we don't need it in this fn.
    (reset! w/data next-data)
    (pr/print-data data)
    (if (exit-command? f)
      ;; TODO - control what's output. Could potentially bypass datawalk entirely
      ;; & just handle in repl, since the exit commands aren't really relevant
      ;; except in the true repl
      :exit ; used as signal to stop looping
      (do (when (and (or (ps/read-int in) (time-stepping? f))
                     (not= data next-data)) ; complicaton from various edge cases
            (swap! w/the-past conj data)
            (reset! w/the-future []))
          next-data))))

(defn repl
  "Runs a small, self-contained, fully-interactive REPL for exploring data
  (Clojure-only for the moment)."
  ;; initialize
  ;; loop:
  ;;   read-input
  ;;   -> datawalk
  [d]
  (println "Exploring interactively.\n")
  (initialize d)
  (pr/print-data d)
  (loop [data d]
    (print prompt)
    (when pr/*debug-mode* (print-globals))
    (let [in (read-input)
          next-data (datawalk data in)]
      (if (= :exit next-data)
        next-data ; TODO
        (recur next-data)))))

(defn look-at
  "Initializes the semi-interactive version. Typically you should call look-at
  once, and then ww many times."
  [d]
  (println "Exploring semi-interactively.\n")
  (initialize d)
  (pr/print-data d))

(defmacro ww [& args]
  ;; parse args as input
  ;; -> datawalk
  ;; NO loop
  )

(comment
  (datawalk {:a 1 :b {:c #{2 3 4} :d "5" :e [6 7 {:f "8" :g {:h :9}}]}})

  )
