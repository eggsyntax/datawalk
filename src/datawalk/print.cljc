(ns datawalk.print
  (:require [datawalk.util :as u]))

(def ^:dynamic *max-items* 30)

(def ^:dynamic *max-line-length* 80)

(def ^:dynamic *max-key-length* 16)

;; TODO haven't really looked at this yet, it's just copied
;; straight from harmonium.datomic.util.explore
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
                          (format "%02d. %s" index (quote-strings item)))
                ;; ...or a map of items (so item is a [k v])
                (map? data)
                ,  (let [
                         ;; _ (println "item =" item)
                         ;; _ (println "type =" (type item))
                         [k v] item
                         format-s (str "%02d. %"
                                       (+ 2 (longest-length (keys data)))
                                       "s: %s")]
                     (limitln *max-line-length*
                              (format format-s
                                      index
                                      (limit-right *max-key-length* k)
                                      (quote-strings v))))
                (set? data)
                , (limitln *max-line-length*
                         (format "%02d. %s" index (quote-strings item)))
                ;; TODO this is redundant, right?
                ;; (string? data)
                ;; ,  data
                :else
                ,  (str "how should I print a" (type data) "?")))
            (take *max-items* data))
        :else ; unhandled singular type
        ,   (str data)))

(defn print-data [data]
  (println (to-string data)))
