(ns datawalk.print
  ;; We use cl-format because cljs doesn't have core fn format
  (:require #?(:clj  [clojure.pprint :refer [cl-format]]
               :cljs [cljs.pprint    :refer [cl-format]])))

(def ^:dynamic *max-items* 30)

(def ^:dynamic *max-line-length* 150)

(def ^:dynamic *max-key-length* 30)

(def ^:dynamic *debug-mode* false)

(defn- longest-length
  "Return the length of the longest (in # of chars) item in the coll"
  [coll]
  (min *max-key-length*
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

(defn kv-item-to-string
  "Stringify a single kv-pair of a maplike thing; suitable for fulfilling
  Datawalk-Map-Stringable"
  [k v]
  )

(defn seq-item-to-string
  "Stringify a single item of a sequence; suitable for fulfilling
  Datawalk-Seq-Stringable"
  [item]
  )

(defn data-to-string
  "Stringify a piece of data in its entirety; suitable for fulfilling
  Datawalk-Stringable"
  [data]
  )

(defprotocol Datawalk-Map-Stringable
  "Describes how a single kv-pair of a sequence should stringify itself for
  datawalk. Items will be prepended with numbers (4 chars) and the result will
  be chopped off at *max-line-length* characters"
  (dw-kv-item-to-string [k v] "convert a single kv-pair to a string for datawalk"))

(defprotocol Datawalk-Seq-Stringable
  "Describes how a single item of a sequence should stringify itself for
  datawalk. Items will be prepended with numbers (4 chars) and the result will
  be chopped off at *max-line-length* characters"
  (dw-item-to-string [item] "convert a single item to a string for datawalk"))

(defprotocol Datawalk-Stringable
  "Describes how a type of thing should stringify itself for datawalk.
  Implementations should work in both clj and cljs (cl-format is useful for
  this). Output will be chopped off at *max-line-length* characters, and should
  be designed accordingly."
  (dw-to-string [data] "convert to a string for datawalk")
  )

(defn to-string-new
  [data]
  (cond
    (extends? Datawalk-Map-Stringable data) nil
    (extends? Datawalk-Seq-Stringable data) nil
    (extends? Datawalk-Stringable data) nil
    :else nil
    ))

;; TODO change to protocol so that users can extend
(defn to-string
  "Specialized pretty-printer for printing our sequences of things with numbers prepended"
  [data]
  ;; (println "data =" data)
  (cond (string? data) ; strings masquerade as seqs, so we handle separately
        ,  (str " 00. " (quote-strings data))
        (seqable? data)
        ,  (map-indexed
            (fn [index item]
              ;; data is a seq of items...
              (cond
                (sequential? data)
                ,  (limitln *max-line-length*
                            (cl-format nil "~2,'0D. ~A" index (quote-strings item)))
                ;; ...or a map of items (so item is a [k v])
                (map? data)
                ,  (let [
                         ;; _ (println "item =" item)
                         ;; _ (println "type =" (type item))
                         [k v] item
                         format-s (str "~2,'0D. ~"
                                       (longest-length (keys data))
                                       "A: ~A")]
                     (limitln *max-line-length*
                              (cl-format nil format-s
                                         index
                                         (limit-right *max-key-length* k)
                                         (quote-strings v))))
                (set? data)
                ,  (limitln *max-line-length*
                            (cl-format nil "~2,'0D. ~A" index (quote-strings item)))
                :else
                ,  (limitln *max-line-length*
                            (cl-format nil "~2,'0D. (no-prnt-prtcl) ~A" index (quote-strings item)))))
            (take *max-items* data))
        :else ; unhandled singular type
        ,   (str " " data)))

(defn to-debug-string [x]
  (if (and (seqable? x) (not (string? x)))
    (map (partial limitln *max-line-length*) x)
    (limitln *max-line-length* x)))

(defn print-globals [path saved the-past the-future]
  (println "PATH:\n" (to-debug-string path))
  (println "SAVED:\n" (to-debug-string saved))
  (println "THE-PAST:\n" (to-debug-string the-past))
  (println "THE-FUTURE:\n" (to-debug-string the-future))
  (println))

(defn print-data [data]
  (println (to-string data)))
