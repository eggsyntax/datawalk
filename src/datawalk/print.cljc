(ns datawalk.print
  ;; We use cl-format because cljs doesn't have core fn format
  (:require [clojure.pprint :refer [cl-format]]))

(def config (atom nil))

(defn maybe-initialize-config []
  (when-not @config
    (reset! config {:max-items 30
                    :max-line-length 120
                    :max-key-length 24
                    :debug-mode false})))

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

(defn- is-seqable?
  "seqable? is added in clj 1.9 -- using this roughly equivalent fn for
  backward-compatibility"
  [x]
  (if (sequential? x)
    true
    (try
      (seq x)
      #?(:clj (catch IllegalArgumentException e nil)
         :cljs (catch js/Error e nil)))))

(defn to-string
  "Specialized pretty-printer for printing our sequences of things with numbers prepended"
  [data]
  ;; (println "data =" data)
  (cond (string? data) ; strings masquerade as seqs, so we handle separately
        ,  (str " 00. " (quote-strings data))
        (is-seqable? data)
        ,  (map-indexed
            (fn [index item]
              ;; data is a seq of items...
              (cond
                (sequential? data)
                ,  (limitln (:max-line-length @config)
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
                     (limitln (:max-line-length @config)
                              (cl-format nil format-s
                                         index
                                         (limit-right (:max-key-length @config) k)
                                         (quote-strings v))))
                (set? data)
                ,  (limitln (:max-line-length @config)
                            (cl-format nil "~2,'0D. ~A" index (quote-strings item)))
                :else
                ;; TODO get rid of "no-prnt-prtcl" -- maybe a `?`?
                ,  (limitln (:max-line-length @config)
                            (cl-format nil "~2,'0D. (no-prnt-prtcl) ~A" index (quote-strings item)))))
            (take (:max-items @config) data))
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
