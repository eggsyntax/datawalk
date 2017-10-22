(ns datawalk.core
  (:require [datawalk.datawalk :as w]
            [datawalk.print    :as pr]
            [datawalk.parse    :as ps]
            [clojure.string :as string]
            #_[clojure.tools.reader :as rdr]
            #_[clojure.tools.reader.reader-types :as rdrt]
            )
  #?(:cljs (:require-macros [datawalk.core :refer [step]])))

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

(def exit-command? #{w/exit w/exit-with-current})

;; Commands (in addition to drill) which advance the time step
(def time-stepping? #{w/root w/up w/function})

(defn initialize [d]
  (reset! w/data d)
  (reset! w/the-root d)
  (reset! w/paths {d []}) ; Start with (empty) path to root
  (reset! w/saved {})
  (reset! w/the-past [])
  (reset! w/the-future [])
  )

(defn read-input
  ;; TODO
  "Get user input (at repl) -- later this needs to be generalized for both clj
  and the various cljs environments."
  []
  (flush)
  (let [input (read-line)]
    (println input)
    input))

(defn datawalk
  "Runs a small, self-contained REPL for exploring data."
  ;; Technically a PREL
  [d]
  (println "Exploring.\n")
  (initialize d)
  (loop [data d]
    ;; (println "past:\n" (string/join "\n " @w/the-past))
    ;; (println "future:\n" (string/join "\n " @w/the-future))
    ;; (println)
    (when pr/*debug-mode*
      (pr/print-debug-info (@w/paths data) @w/saved @w/the-past @w/the-future))
    (pr/print-data data)
    (print prompt)
    (let [in (read-input)
          f (ps/parse in)
          next-data (if f (f data) data)]
      ;; We store data in an atom only so that it can be referred
      ;; to outside this fn.
      (reset! w/data next-data)
      (if (exit-command? f)
        next-data
        (do (when (or (ps/read-int in) (time-stepping? f))
              (swap! w/the-past conj data)
              (reset! w/the-future []))
            (recur next-data))))))

(comment
  (datawalk {:a 1 :b {:c #{2 3 4} :d "5" :e [6 7 {:f "8" :g {:h :9}}]}})

  )
