(ns datawalk.dw-protocol-implementation
  "Implements the protocol found in datawalk.dw-protocol"
  (:require [datawalk.print :as p]
            [datawalk.datawalk :as d]
            [datawalk.dw-protocol :refer [Datawalkable dw-to-string dw-drill]]))

;; Note that dw-to-string implementations use entirely p/ functions,
;; ie datawalk.print fns; dw-drill implemantations use entirely d/
;; functions from datawalk.datawalk.

;; TODO create separate clj and cljs versions -- as written, this breaks in cljs
(extend-protocol Datawalkable
  #?(:clj Object
     :cljs default)
  (dw-to-string
    ([data] (p/limitln (:max-line-length @p/config) data))
    ([data top-level] (p/limitln (:max-line-length @p/config) data)))
  java.util.Map
  (dw-to-string
    ([data] (p/stringify-map data))
    ([data top-level] (p/stringify-map data top-level)))
  clojure.lang.IPersistentSet
  (dw-to-string
    ([data] (p/stringify-set data))
    ([data top-level] (p/stringify-set data top-level)))
  clojure.lang.Seqable
  (dw-to-string
    ([data] (p/stringify-seq data))
    ([data top-level] (p/stringify-seq data top-level)))
  clojure.lang.IDeref
  (dw-to-string
    ([data] (p/stringify-derefable data))
    ([data top-level] (p/stringify-derefable data top-level)))
  nil
  (dw-to-string
    ([data] "")
    ([data top-level] ""))
  )
