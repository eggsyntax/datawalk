(ns datawalk.parse
  "Parses user input into a call to a fn in datawalk.datawalk"
  (:require [datawalk.datawalk :as dw]))


(defn read-int [s]
  #?(:clj (try (Integer/parseInt s)
               (catch NumberFormatException _ nil))
     :cljs (let [n (js/parseInt s)]
             (if (number? n) n nil))))

;; TODO maybe add print-globals command?
(def cmd-map
  {"q" dw/quit              ; exit and return saved values if any
   "x" dw/exit-with-current ; exit & return just this value
   "s" dw/save-current      ; save to map of return values
   "v" dw/save-path         ; save path to map of return values
   "b" dw/backward          ; step backward in history
   "f" dw/forward           ; step forward in history
   "r" dw/root              ; jump back to original root
   "u" dw/up                ; step upward [provides list of referring entities]
   "h" dw/help              ; print help & return same ent
   "p" dw/print-path        ; print path from root to current item.
   "n" dw/print-saved-map   ; print the map of saved data
   ;;; Not yet implemented:
   "!" dw/function          ; call an arbitrary 1-arg fn on data, jump to result
   "t" nil ; type: print the type of the current item
   ;;; possibles: map and filter cmds, similar to function cmd.
   "" nil ; all others become no-op
   })

;; TODO handle case where > 1 arg, to support `(w ! my-fn)`
(defn parse [inp]
  ;; (println "raw input: " inp)
  ;; If #: drill into that value
  (if-let [n (read-int inp)]
    (partial dw/drill n)
    ;; else: get fn to call on data
    (get cmd-map inp
         (fn [data] (do (println "Unknown command:" inp)
                      (dw/no-op data))))))
