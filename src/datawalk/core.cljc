(ns datawalk.core
  (:require [clojure.string :as string]
            [clojure.tools.reader :as rdr]
            [clojure.tools.reader.reader-types :as rdrt]
            ))

;; Notes:
;; Throughout: d = data
;; - Would use a customized clojure.main repl, but AFAIK it won't work w cljs.


(defn datawalk
  "Runs a small, self-contained REPL for exploring data."
  [d]
  (println "yo")
  )
