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
  (dw-to-string data :top-level)
  )

(def to-string to-string-new)

;; TODO THINK as I build protocols for other datatypes - json, datomic, etc -
;;      it'll probably entail extra dependencies. Consider creating 2 builds,
;;      one with minimal dependencies and one that's batteries-included &
;;      has protocols for a bunch of datatypes.
;; TODO when viewing a map entry, we only see the val rather than "key: val"
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
