(ns datawalk.print
  ;; We use cl-format because cljs doesn't have core fn format
  (:require [clojure.pprint :refer [cl-format]]
            [clojure.string :as string]
            [datawalk.datawalkable :refer [Datawalkable dw-to-string]])
  ;; (:import (datawalk.datawalkable dw-to-string))
  )

(def config (atom nil))

(defn maybe-initialize-config []
  (when-not @config
    (reset! config {:max-items 30
                    :max-line-length 120
                    :max-key-length 24
                    :debug-mode false})))

;; initialize on load (as a dev convenience)
(maybe-initialize-config)

(defn- longest-length
  "Return the length of the longest (in # of chars) item in the coll"
  [coll]
  (min (:max-key-length @config)
       (apply max (map #(count (str %)) coll))))

(defn- quote-strings
  "Surround strings with quotation marks; return other values unchanged"
  [v]
  (if (string? v) (str "\"" v "\"") v))

(defn- ends-in-nl? [s] (= (last (str s)) \newline))

(defn- limit-left
  "Ensure that string s doesn't exceed n chars. Preserve newline."
  [n s]
  (let [short-s (subs (str s) 0 (min n (count (str s))))]
    (if (and (ends-in-nl? s)
             (not (ends-in-nl? short-s)))
      (str short-s "\n")
      short-s)))

(defn- limit-right
  "Ensure that string s doesn't exceed n chars; chops from right"
  [n s]
  (let [s (str s)
        slen (count s)
        cutoff (min n slen)]
    (subs s (- slen cutoff) slen)))

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

(defn limitln
  ([n s]
   (limitln n s true))
  ([n s from-left?]
   (let [limit (if from-left? limit-left limit-right)]
     (str (limit n s)))))

(defn eagerly
  "Sometimes dw-to-string encounters lazy sequences; those should be made eager
  for printing. We call `into` twice to compensate for the variation in where
  different sequence types conj onto (at some cost to performance)."
  [x]
  (if (is-seqable? x)
    (let [emp (empty x)]
      (into emp (into emp x)))
    x))

(defn stringify-seq-item-numbered [index item]
  (let [format-s (str "~2,'0D. ~A")]
    (limitln (:max-line-length @config)
             (cl-format nil  "~2,'0D. ~A\n" index (dw-to-string item)))))

(defn stringify-seq-item [_ item] ; ignore index
  (limitln (:max-line-length @config) (dw-to-string item)))

(defn stringify-seq
  "For each item, print it, chopped at max-line-length chars, and optionally
  prepended with an index number (4 chars)."
  ([data]
   (stringify-seq data false))
  ([data top-level]
   (eagerly
    (map-indexed (if top-level
                   stringify-seq-item-numbered
                   stringify-seq-item)
                 (take (:max-items @config) data)))))

(defn stringify-kv [format-s index item]
  (let [[k v] item]
    (limitln (:max-line-length @config)
             (cl-format nil
                        format-s
                        index
                        (limit-right (:max-key-length @config) (dw-to-string k))
                        (dw-to-string v)))))

(defn stringify-map
  "For each kv pair, stringify k and v, and print them colon-separated, chopped
  at max-line-length chars, and (if top-level?) prepended with an index number
  (4 chars)."
  ([data]
   (stringify-map data false))
  ([data top-level?]
   ;; (prn data)
   (let [some-data (into {} (take (:max-items @config) data))
         ;; _ (prn some-data)
         longest-key (longest-length (keys some-data))]
     (if top-level?
       (let [format-s (str "~2,'0D. ~" longest-key "A : ~A\n")]
         (eagerly
          (map-indexed (partial stringify-kv format-s) some-data)))
       (str some-data)))))

(defn stringify-derefable
  "At the top level, derefables pretend to be sequences -- ie they're printed
  with a number in front of them -- as an affordance to suggest to the user
  that they can be drilled into."
  ([data]
   (stringify-derefable data false))
  ([data top-level?]
   (let [maybe-number (if top-level? "00. " "")
         format-s (str maybe-number "~A")]
     (cl-format nil format-s (quote-strings data)))))

(defn stringify-set
  "Preserve the literal #{} representation"
  ([data]
   (stringify-set data false))
  ([data top-level?]
   (let [some-data (into #{} (take (:max-items @config) data))]
     (if top-level?
       (eagerly (map-indexed #(cl-format nil "~2,'0D. ~A\n" %1 (quote-strings %2)) some-data))
       (str data)))))

(defn to-string
  [data]
  (dw-to-string data :top-level))

(defn to-debug-string [x]
  (if (and (is-seqable? x) (not (string? x)))
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
