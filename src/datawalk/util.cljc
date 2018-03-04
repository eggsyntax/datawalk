(ns datawalk.util
  "Util fns, especially shared ones")

(defn not-set
  "Convert sets to vectors so that they're ordered; leave everything else untouched."
  [x]
  (if (set? x) (vec x) x))
