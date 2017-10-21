(ns datawalk.parse
  "Parses user input into a call to a fn in datawalk.datawalk")


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

;;
;;
;;
;;
;;
;;
;;
;;
;;
;;
;;
