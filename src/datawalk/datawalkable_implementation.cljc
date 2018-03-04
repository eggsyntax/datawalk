(ns datawalk.datawalkable-implementation
  "Implements the protocol found in datawalk.datawalkable"
  (:require [datawalk.print :as p]
            [datawalk.datawalk :as d]
            [datawalk.datawalkable :refer [Datawalkable dw-to-string dw-drill]]))

;; Note that dw-to-string implementations use entirely p/ functions,
;; ie datawalk.print fns; dw-drill implemantations use entirely d/
;; functions from datawalk.datawalk.

;; TODO create separate clj and cljs versions -- as written, this breaks in cljs
;; because the types are mostly clj types
(extend-protocol Datawalkable
  #?(:clj Object
     :cljs default)
  (dw-to-string
    ([data] (p/limitln (:max-line-length @p/config) data))
    ([data top-level] (p/limitln (:max-line-length @p/config) data)))
  (dw-drill [n data]
    (do (println "Don't know how to drill into" (type data)
                 "\nTry extending the Datawalkable protocol to"
                 (type data) "or one of its supers.\n"
                 "Consider sharing the extension back to the datawalk project!\n")
        data))
  java.util.Map
  (dw-to-string
    ([data] (p/stringify-map data))
    ([data top-level] (p/stringify-map data top-level)))
  (dw-drill [n data] (d/drill-map n data))
  clojure.lang.IPersistentSet
  (dw-to-string
    ([data] (p/stringify-set data))
    ([data top-level] (p/stringify-set data top-level)))
  (dw-drill [n data] (d/drill-set n data))
  clojure.lang.Seqable
  (dw-to-string
    ([data] (p/stringify-seq data))
    ([data top-level] (p/stringify-seq data top-level)))
  ;; TODO will drill-sequential work on any seqable?
  (dw-drill [n data] (d/drill-sequential n data))
  clojure.lang.Sequential
  (dw-to-string
    ([data] (p/stringify-seq data))
    ([data top-level] (p/stringify-seq data top-level)))
  (dw-drill [n data] (d/drill-sequential n data))
  ;; ClojureScript has no blocking derefables.
  #?@(:clj [clojure.lang.IBlockingDeref
            (dw-to-string
             ([data] (p/stringify-derefable data))
             ([data top-level] (p/stringify-derefable data top-level)))
            ;; Drilling into a future/promise dereferences it with a fast
            ;; timeout so we don't block if it doesn't contain a value yet.
            (dw-drill [n data] (d/drill-blocking-derefable n data))])
  clojure.lang.IDeref
  (dw-to-string
    ([data] (p/stringify-derefable data))
    ([data top-level] (p/stringify-derefable data top-level)))
  (dw-drill [n data] (d/drill-derefable n data))
  nil
  (dw-to-string
    ([data] "")
    ([data top-level] ""))
  (dw-drill [n data] data))
