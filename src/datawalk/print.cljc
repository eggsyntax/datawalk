(ns datawalk.print)

(def ^:dynamic *max-items* 30)

(def ^:dynamic *max-line-length* 80)

(def ^:dynamic *max-key-length* 16)

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

(defn- limitln
  ([n s]
   (limitln n s true))
  ([n s from-left?]
   (let [limit (if from-left? limit-left limit-right)]
     (str (limit n s) "\n"))))

(defn to-string
  "Specialized pretty-printer for printing our sequences of things with numbers prepended"
  [data]
  ;; TODO use cl-format to represent nil as "nil" instead of "null"?
  ;;     It's oddly amusing to see it as "null" tho
  ;;     https://clojuredocs.org/clojure.core/format
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
                          (format "%02d. %s" index (quote-strings item)))
                ;; ...or a map of items (so item is a [k v])
                (map? data)
                ,  (let [
                         ;; _ (println "item =" item)
                         ;; _ (println "type =" (type item))
                         [k v] item
                         format-s (str "%02d. %"
                                       (longest-length (keys data))
                                       "s: %s")]
                     (limitln *max-line-length*
                              (format format-s
                                      index
                                      (limit-right *max-key-length* k)
                                      (quote-strings v))))
                (set? data)
                ,  (limitln *max-line-length*
                         (format "%02d. %s" index (quote-strings item)))
                :else
                ,  (str "how should I print a" (type data) "?")))
            (take *max-items* data))
        :else ; unhandled singular type
        ,   (str " 00. " data)))

(defn to-debug-string [x]
  (if (and (seqable? x) (not (string? x)))
    (map (partial limitln *max-line-length*) x)
    (limitln *max-line-length* x)))

(defn print-debug-info [path saved the-past the-future]
  (println "PATH:\n" (to-debug-string path))
  (println "SAVED:\n" (to-debug-string saved))
  (println "THE-PAST:\n" (to-debug-string the-past))
  (println "THE-FUTURE:\n" (to-debug-string the-future))
  (println))

(defn print-data [data]
  (println (to-string data)))
