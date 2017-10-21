(ns datawalk.state)

;; We track current data,
(def data (atom nil))

;; the paths from root to each visited node,
(def paths (atom {}))

;; and a map of values to return on exit.
(def saved (atom {}))
