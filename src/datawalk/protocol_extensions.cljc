(ns datawalk.protocol-extensions
  "Extend datawalk protocols to additional datatypes. This is done *only* for
  datatypes which are included as dependencies in the project, so that datawalk
  itself doesn't require dependencies which most users may not need."
  (:require [datawalk.datawalkable :refer [Datawalkable]]))

;; TODO make note in readline: users please submit PRs for new extensions

;; Datomic extensions:

;; TODO grr, this won't compile --
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



;; The simplest solution, which we see below, is just to convert the type of the
;; data to something that Datawalkable already knows how to handle. In the case
;; of dw-to-string, the conversion doesn't become part of the next data.

;; (extend-protocol datawalk.datawalkable/Datawalkable
;;   datomic.query.EntityMap
;;   (datawalk.datawalkable/dw-to-string
;;     ([data] (datawalk.datawalkable/dw-to-string (ent-map->map data)))
;;     ([data top-level] (datawalk.datawalkable/dw-to-string (ent-map->map data) top-level)))
;;   ;; (datawalk.datawalkable/dw-drill [data n] (first (drop n data)))
;;   (datawalk.datawalkable/dw-drill [data n] (datawalk.datawalkable/dw-drill (ent-map->map data) n))
;;   )
