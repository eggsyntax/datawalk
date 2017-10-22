(ns datawalk.util
  "Util fns, especially shared ones")

(defn not-set [x]
  (if (set? x) (vec x) x))
