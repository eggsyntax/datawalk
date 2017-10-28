(ns datawalk.print
  ;; We use cl-format because cljs doesn't have core fn format
  ;; (:require #?(:clj  [clojure.pprint :refer [cl-format]]
  ;;              :cljs [cljs.pprint    :refer [cl-format]]))
  (:require [clojure.pprint :refer [cl-format]])
  )

(def config (atom {}))

(defn initialize-config []
  (reset! config {:max-items 30
                  :max-line-length 120
                  :max-key-length 24
                  :debug-mode false}))

(defn- longest-length
  "Return the length of the longest (in # of chars) item in the coll"
  [coll]
  (min (:max-key-length @config)
       (apply max (map #(count (str %)) coll))))

(defn- quote-strings
  "Surround strings with quotation marks; return other values unchanged"
  [v]
  (if (string? v) (str "\"" v "\"") v))

(defn- limit-left
  "Ensure that string s doesn't exceed n chars"
  [n s]
  (subs (str s) 0 (min n (count (str s)))))

(defn- limit-right
  "Ensure that string s doesn't exceed n chars; chops from right"
  [n s]
  (let [s (str s)
        slen (count s)
        cutoff (min n slen)]
    (subs s (- slen cutoff) slen)))

(defn limitln
  ([n s]
   (limitln n s true))
  ([n s from-left?]
   (let [limit (if from-left? limit-left limit-right)]
     (str (limit n s) "\n"))))

;; Some leftover comments for copying:
;;   "Stringify a single kv-pair of a maplike thing; suitable for fulfilling
;;   Datawalk-Map-Stringable"
;;   "Stringify a single item of a sequence; suitable for fulfilling
;;   Datawalk-Seq-Stringable"
;;   "Stringify a piece of data in its entirety; suitable for fulfilling
;;   Datawalk-Stringable"
;;   "Describes how a single kv-pair of a sequence should stringify itself for
;;   datawalk. Items will be prepended with numbers (4 chars) and the result will
;;   be chopped off at max-line-length characters"
;;   "Describes how a single item of a sequence should stringify itself for
;;   datawalk. Items will be prepended with numbers (4 chars) and the result will
;;   be chopped off at max-line-length characters"

;; TODO YOUAREHERE trying to set up a 2nd arity for dw-to-string, which
;; when present causes the data to be print in a numbered, nl-separated
;; way (at least if that applies to this sort of thing)
;; BUT AFAICT it just always throws an error on the single-arity version.
;; eg compare (dw-to-string 7) (dw-to-string 7 8) -- see implementations
;; for those 2 calls on lines 114/115

(defprotocol Datawalk-Stringable
  "Describes how a type of thing should stringify itself for datawalk.
  Implementations should work in both clj and cljs (cl-format is useful for
  this). Output will be chopped off at max-line-length characters, and should
  be designed accordingly."
  (dw-to-string [data] [data top-level] "convert to a string for datawalk"))

(defn stringify-seq-item-numbered [index item]
  (let [format-s (str "~2,'0D. ~A")]
    (limitln (:max-line-length @config)
             (cl-format nil  "~2,'0D. ~A" index (quote-strings
                                                 (dw-to-string item))))))

(defn stringify-seq-item [_ item] ; ignore index
  (limitln (:max-line-length @config) (dw-to-string item)))

(defn stringify-seq
  "For each item, print it, chopped at max-line-length chars, and optionally
  prepended with an index number (4 chars)."
  ([data]
   (stringify-seq data false))
  ([data top-level]
   (map-indexed (if top-level
                  stringify-seq-item-numbered
                  stringify-seq-item)
                data)))

(defn stringify-kv [format-s index item]
  (let [[k v] item]
    (limitln (:max-line-length @config)
             (cl-format nil format-s
                        index
                        (limit-right (:max-key-length config) (dw-to-string k))
                        (dw-to-string v)))))

(defn stringify-map
  "For each kv pair, stringify k and v, and print them colon-separated, chopped
  at max-line-length chars, and prepended with an index number (4 chars)."
  [data]
  (let [;; _ (println "item =" item)
        ;; _ (println "type =" (type item))
        format-s (str "~2,'0D. ~"
                      (longest-length (keys data))
                      "A: ~A")]
    (map-indexed (partial stringify-kv format-s) data)))

;; TODO remember to do (take max-items)!
(extend-protocol Datawalk-Stringable
  Object
  (dw-to-string
    ([data] (limitln (:max-line-length @config) data))
    ([data top-level] (limitln (:max-line-length @config) data)))
  java.util.Map
  (dw-to-string
    ([data] (stringify-map data))
    ([data top-level] (stringify-map data top-level)))
  clojure.lang.Seqable
  (dw-to-string
    ([data] (stringify-seq data))
    ([data top-level] (stringify-seq data top-level)))
  )

(defn to-string-new
  [data]
  (dw-to-string data)
  )

(def to-string to-string-new)

;; TODO change to protocol so that users can extend
#_(defn to-string
    "Specialized pretty-printer for printing our sequences of things with numbers prepended"
    [data]
    ;; (println "data =" data)
    (cond (string? data) ; strings masquerade as seqs, so we handle separately
          ,  (quote-strings data)
          (seqable? data)
          ,  (map-indexed
              (fn [index item]
                ;; data is a seq of items...
                (cond
                  (sequential? data)
                  ,  (limitln *max-line-length*
                              (cl-format nil "~2,'0D. ~A" index (quote-strings item)))
                  (set? data)
                  ,  (limitln *max-line-length*
                              (cl-format nil "~2,'0D. ~A" index (quote-strings item)))
                  :else
                  ,  (limitln *max-line-length*
                              (cl-format nil "~2,'0D. (no-prnt-prtcl ~A) ~A" index (type item) (quote-strings item)))))
              (take *max-items* data))
          :else ; unhandled singular type
          ,   (str " " data)))

(defn to-debug-string [x]
  (if (and (seqable? x) (not (string? x)))
    (map (partial limitln (:max-line-length @config)) x)
    (limitln (:max-line-length @config) x)))

(defn print-globals [path saved the-past the-future]
  (println "PATH:\n" (to-debug-string path))
  (println "SAVED:\n" (to-debug-string saved))
  (println "THE-PAST:\n" (to-debug-string the-past))
  (println "THE-FUTURE:\n" (to-debug-string the-future))
  (println))

(defn print-data [data]
  (println (to-string data)))
