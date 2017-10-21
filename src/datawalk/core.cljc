(ns datawalk.core
  (:require [datawalk.datawalk :as dw]
            [datawalk.print    :as pr]
            [datawalk.parse    :as ps]
            #_[clojure.string :as string]
            #_[clojure.tools.reader :as rdr]
            #_[clojure.tools.reader.reader-types :as rdrt]
            ))

;; Dependencies:
;; core
;;   datawalk (contains state)
;;   parse
;;   print


;; Notes:
;; Throughout: d = data
;; - Would use a customized clojure.main repl, but AFAIK it won't work w cljs.
;; - A key challenge for making this work in cljs is that generic user input
;;   isn't available in cljs. I brought it up in #clojurescript, and found the
;;   following resources:
;;   - https://clojurians.slack.com/archives/C03S1L9DN/p1508602683000025
;;     - note especially: `:special-fns` get processed by clj
;;   - https://github.com/abiocljs (@mfikes)
;;   - https://github.com/potetm/tire-iron
;;   - https://www.google.com/search?q=IReplEnvOption

(defn datawalk
  "Runs a small, self-contained REPL for exploring data."
  [d]
  (println "yo")
  )
