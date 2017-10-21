(ns datawalk.parse
  "Parses user input into a call to a fn in datawalk.datawalk"
  (:require [datawalk.datawalk :as w]))


{"#" "Enter any number to jump to the corresponding item"
 "q" ":exit ; exit and return saved values if any"
 "x" ":exit-with-current ; exit & return just this value"
 "s" ":save-current ; save to map of return values"
 "v" ":save-path ; save path to map of return values"
 "b" ":backward ; step backward in history"
 "f" ":forward ; step forward in history"
 "r" ":root ; jump back to root"
 "u" ":up ; step upward [provides list of referring entities]"
 "h" ":help ; print help & return same ent"
 "p" ":print-path ; path: print path to current item."
 "!" ":function ; call an arbitrary 1-arg fn on data, jump to result"
 }

(defn- read-int [s]
  #?(:clj (try (Integer/parseInt s)
               (catch NumberFormatException _ nil))
     :cljs (let [n (js/parseInt s)]
             (if (number? n) n nil))))

(def cmd-map
  ;; Special commands get keywords; the rest are fns that will be called on the data
  {"q" w/exit
   "x" w/exit-with-current ; exit & return just this ent
   "s" w/save-current ; save to map of return values
   "v" w/save-path ; save path to map of return values
   "b" w/backward ; step backward in history
   "f" w/forward ; step forward in history
   "r" w/root ; jump back to root
   "u" w/up ; step upward [provides list of referring entities]
   "h" w/print-help ; print help & return same ent
   "p" w/print-path ; path: print path to current item.
   "!" w/function ; call an arbitrary 1-arg fn on data, jump to result
   "?" w/print-help
   ;; Not yet written:
   "t" nil ; type: print the type of the current item
   ;; possibles: map and filter cmds, similar to function cmd.
   ;; all others become no-op
   "" nil ;
   })


(defn parse [inp]
  ;; (println "raw input: " inp)
  ;; If #: drill into that value
  (if-let [n (read-int inp)]
    (partial w/drill n)
    ;; else: get fn to call on data
    (get cmd-map inp
         (fn [data] (do (println "Unknown command:" inp)
                      (w/no-op data))))))
