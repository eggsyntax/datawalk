(ns datawalk.print)

;; TODO haven't really looked at this yet, it's just copied
;; straight from harmonium.datomic.util.explore
(defn- longest-length
  "Return the length of the longest (in # of chars) item in the coll"
  [coll]
  (apply max (map #(count (str %)) coll)))

(defn- quote-strings
  "Surround strings with quotation marks; return other values unchanged"
  [v]
  (if (string? v) (str "\"" v "\"") v))

(defn to-string
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
                ,  (format "%02d. %s\n" index (quote-strings item))
                ;; ...or a map of items (so item is a [k v])
                (map? data)
                ,  (let [
                         ;; _ (println "item =" item)
                         ;; _ (println "type =" (type item))
                         [k v] item
                         format-s (str "%02d. %"
                                       (+ 2 (longest-length (keys data)))
                                       "s: %s\n")]
                     (format format-s index k (quote-strings v)))
                ;; TODO this is redundant, right?
                ;; (string? data)
                ;; ,  data
                :else
                ,  (str "how should I print a" (type data) "?")))
            (take 30 data))
        :else ; unhandled singular type
        ,   (str data)))

(defn print-data [data]
  (println (to-string data)))
