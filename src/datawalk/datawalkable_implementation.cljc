(ns datawalk.datawalkable-implementation
  "Implements the protocol found in datawalk.datawalkable"
  (:require [datawalk.print :as p]
            [datawalk.datawalk :as d]
            [datawalk.datawalkable :refer [Datawalkable dw-to-string dw-drill]])
  #?(:cljs (:require-macros [datawalk.datawalkable-implementation :refer [extend-protocol-to-many]])))

#?(:clj
   (defmacro extend-protocol-to-many
     "Extend a protocol to multiple types which all take the same implementation
  for each function. Pass the protocol name, the sequence of types it should be
  extended to, and an implementation for each function defined by the protocol."
     [protocol types & implementation-bodies]
     `(do ~@(for [type# types]
              `(extend-protocol ~protocol ~type#
                                ~@implementation-bodies)))))

#?(:clj
   (extend-protocol Datawalkable
     Object
     (dw-to-string
       ([data] (p/limitln (:max-line-length @p/config) data))
       ([data top-level] (p/limitln (:max-line-length @p/config) data)))
     (dw-drill [data n]
       (do (println "Don't know how to drill into" (type data)
                    "\nTry extending the Datawalkable protocol to"
                    (type data) "or one of its supers.\n"
                    "Consider sharing the extension back to the datawalk project!\n")
           data))
     java.util.Map
     (dw-to-string
       ([data] (p/stringify-map data))
       ([data top-level] (p/stringify-map data top-level)))
     (dw-drill [data n] (d/drill-map n data))
     clojure.lang.IPersistentSet
     (dw-to-string
       ([data] (p/stringify-set data))
       ([data top-level] (p/stringify-set data top-level)))
     (dw-drill [data n] (d/drill-set n data))
     ;; TODO is it a terrible idea to extend to Seqable? I fear it might be,
     ;; because different types may respond idiosyncratically to `seq` (see
     ;; datomic.query.EntityMap, for example)
     ;;
     ;; clojure.lang.Seqable
     ;; (dw-to-string
     ;;   ([data] (p/stringify-seq data))
     ;;   ([data top-level] (p/stringify-seq data top-level)))
     ;; (dw-drill [data n] (d/drill-seqable n data))
     clojure.lang.Sequential
     (dw-to-string
       ([data] (p/stringify-seq data))
       ([data top-level] (p/stringify-seq data top-level)))
     (dw-drill [data n] (d/drill-sequential n data))
     ;; ClojureScript has no blocking derefables. (but what's IDerefWithTimeout? I don't see it used)
     ;; TODO Unfortunately, IBlockingDeref doesn't descend from IDeref. So the two
     ;; are checked in an indeterminate order. So an attempt to drill into a
     ;; future/promise might block, if it happens to be treated as IDeref.
     clojure.lang.IBlockingDeref
     (dw-to-string
       ([data] (p/stringify-derefable data))
       ([data top-level] (p/stringify-derefable data top-level)))
      ;; Drilling into a future/promise dereferences it with a fast
      ;; timeout so we don't block if it doesn't contain a value yet.
     (dw-drill [data n] (d/drill-blocking-derefable n data))
     clojure.lang.IDeref
     (dw-to-string
       ([data] (p/stringify-derefable data))
       ([data top-level] (p/stringify-derefable data top-level)))
     (dw-drill [data n] (d/drill-derefable n data))
     nil
     (dw-to-string
       ([data] "")
       ([data top-level] ""))
     (dw-drill [data n] data))

   :cljs
   (do
     (extend-protocol Datawalkable
       default
       (dw-to-string
         ([data] (p/limitln (:max-line-length @p/config) data))
         ([data top-level] (p/limitln (:max-line-length @p/config) data)))
       (dw-drill [data n]
         (do (println "Don't know how to drill into" (type data)
                      "\nTry extending the Datawalkable protocol to"
                      (type data) "or one of its supers.\n"
                      "Consider sharing the extension back to the datawalk project!\n")
             data))
       nil
       (dw-to-string
         ([data] "")
         ([data top-level] ""))
       (dw-drill [data n] data))
     ;; Maps:
     (extend-protocol-to-many
      Datawalkable
      [cljs.core/PersistentArrayMap cljs.core/PersistentHashMap cljs.core/PersistentTreeMap]
      (dw-to-string
       ([data] (p/stringify-map data))
       ([data top-level] (p/stringify-map data top-level)))
      (dw-drill [data n] (d/drill-map n data)))
     ;; Sets:
     (extend-protocol-to-many
      Datawalkable
      [cljs.core/PersistentHashSet cljs.core/PersistentTreeSet]
      (dw-to-string
       ([data] (p/stringify-set data))
       ([data top-level] (p/stringify-set data top-level)))
      (dw-drill [data n] (d/drill-set n data)))
     ;; Sequentials
     (extend-protocol-to-many
      Datawalkable
      [cljs.core/PersistentQueue cljs.core/LazySeq cljs.core/Range cljs.core/EmptyList cljs.core/List cljs.core/PersistentVector cljs.core/PersistentArrayMapSeq cljs.core/PersistentTreeMapSeq cljs.core/NodeSeq cljs.core/ArrayNodeSeq cljs.core/KeySeq cljs.core/ValSeq cljs.core/RSeq cljs.core/IndexedSeq cljs.core/ChunkedSeq cljs.core/PersistentQueueSeq cljs.core/Cons cljs.core/Subvec]
      (dw-to-string
       ([data] (p/stringify-seq data))
       ([data top-level] (p/stringify-seq data top-level)))
      (dw-drill [data n] (d/drill-sequential n data)))
     ;; Derefables:
     (extend-protocol-to-many
      Datawalkable
      [cljs.core/Atom cljs.core/Var cljs.core/Delay]
      (dw-to-string
       ([data] (p/stringify-derefable data))
       ([data top-level] (p/stringify-derefable data top-level)))
      (dw-drill [data n] (d/drill-derefable n data)))))

(comment

  (require '[datawalk.core :as dc :refer [look-at w]])
  (require '[datawalk.datawalk :as dw])
  (require '[datawalk.example-data :as xd])
  (look-at xd/tiny-example)

  )
