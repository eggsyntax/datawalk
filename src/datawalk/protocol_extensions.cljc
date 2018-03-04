(ns datawalk.protocol-extensions
  "Extend datawalk protocols to additional datatypes. This is done *only* for
  datatypes which are included as dependencies in the project, so that datawalk
  itself doesn't require dependencies which most users may not need."
  (:require [datawalk.datawalkable :refer [Datawalkable]]))

;; TODO make note in readline: users please submit PRs for new extensions

(print "Datawalk extensions: ")

;; TODO YOUAREHERE grr, this won't compile --
;; throws java.lang.ClassNotFoundException: datomic.query.EntityMap even
;; though I'm explicitly catching that error. Presumably it's throwing at
;; compile time? Although the REPL starts up fine.

;; (try (require 'datomic.api)
;;      (print "EntityMap ")
;;      ;; TODO throws clojure.lang.Compiler$CompilerException
;;      (extend datomic.query.EntityMap
;;        Datawalkable
;;        {:dw-to-string (fn
;;                         ([x] x)
;;                         ([x top-level?] x))})
;;      (catch java.lang.ClassNotFoundException e
;;        (println "\nDatomic not found; omitting datomic profile."))
;;      (catch java.io.FileNotFoundException e
;;        (println "\nDatomic not found; omitting datomic profile.")))

(println)
