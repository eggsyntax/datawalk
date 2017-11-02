# datawalk

https://github.com/eggsyntax/datawalk

Provides a simple interactive data explorer.

```
(datawalk.core/repl my-data-structure)
```

In Clojure and ClojureScript, we spend a lot of time exploring and manipulating
data structures, sometimes enormous ones. I've whiled away innumerable hours at
the REPL navigating through complex nested data structures, often heavily-nested
maps with long namespaced keys. I prefer to minimize the amount of typing I have
to do for repetitive tasks, and this is certainly one of those tasks.

So datawalk pretty-prints the top level of your data structure, and then lets
you drill down repeatedly, often with just a single keystroke (the number
representing the item's position in the printed list, as shown next to the
item) plus enter.

Often the reason for drilling down at the REPL is to determine a path to pass to
`get-in`, so datawalk will track and [p]rint the path from the original root to
the level you're on.

At any point in this process, you can [s]ave the current item or sa[v]e the
path (from root to item) into a map of saved items which will be returned when
you [q]uit. You can also e[x]it and return just the current item.

As mentioned, you can [p]rint the path to current data. You can also print
[c]urrent data (useful when you want to see it untruncated), print the [m]ap
of saved data, or print [h]elp to get a reminder of these commands. Capitalize
any of the printing commands to get a pretty-printed version.

You can also move [b]ackward or [f]orward in time, or move [u]p a level, or jump
back to the original [r]oot, or use [!] (function) to call an arbitrary
single-arg │ function on the current data (jumping to the result).

datawalk tries to do one thing well: eliminate tedium and typing when navigating
data structures. The learning curve should be trivial; please let me know if it's
not and I'll try to change that. I hope you find it as useful as I have.

## Demo (fully-interactive mode):


![Demo](resources/fully-interactive.gif?raw=true "Demo")


## Installation:

Leiningen:
```
[datawalk "0.1.4-SNAPSHOT"]
```

Details at https://clojars.org/datawalk

## Usage summary:

```
Clojure:
  (datawalk.core/repl my-data-structure) to start datawalking.
Clojure and ClojureScript:
  (look-at my-data-structure), and then (w [command])

Commands:
  numbers: Enter any number to jump to the corresponding item
  q :quit              ; exit and return saved values if any
  x :exit-with-current ; exit & return just this value
  s :save-current      ; save to map of return values
  v :save-path         ; save path to map of return values
  b :backward          ; step backward in history
  f :forward           ; step forward in history
  r :root              ; jump back to original root
  ! :function          ; call a 1-arg fn on data, jump to result (clj only)
  u :up                ; step upward [provides list of referring entities]
  h :help              ; print help & return same ent
  p :print-path        ; print path from root to current item.
  m :print-saved-map   ; print the map of saved data
  c :print-full-cur    ; print the current data in full, without truncation
```

## Fully-interactive version (Clojure-only)

Start the fully-interactive version with `(datawalk.core/repl my-data-structure)`.
From there, you just enter a single character followed by enter -- a number
to drill down to a specific item in the displayed list, `b` to step back to
where you just were, `h` for help, and so on. It should be extremely self-
explanatory.

## Semi-interactive version (Clojure and ClojureScript)

To use the semi-interactive version, let's assume you've referred the two
key functions:

`(require '[datawalk.core :as dw :refer [look-at w]])`

Start by calling

`user> (look-at my-data-structure)`

This initializes datawalk's internal state, so that it's always tracking a
piece of current data. From there, call `(w *)`, where `*` represents any of
the commands listed in the usage summary. So for example:
```
user> (w 2) ; drill down to item 2 in the numbered sequence
user> (w b) ; step backward in time
user> (w s) ; save the current data to the map of saved values
user> (w h) ; view the help (command summary)
```

After each step, the current state will be stored in datawalk atoms and can
be freely accessed:

`datawalk.datawalk/data` stores the current data.

`datawalk.datawalk/saved` holds the map of stored values.

In the same ns, you can also access `the-root`, `the-past`, and `the-future`.

Additionally, datawalk's built-in printing tools are available:

```
user> (w m) prints the map of saved values.
user> (w p) prints the path from the root to the current data.
```

## Demo (semi-interactive mode):


![Demo](resources/semi-interactive.gif?raw=true "Demo")


## Why two versions?

I developed the semi-interactive version so I could use datawalk in
ClojureScript. ClojureScript presents a challenge: while you can trivially get
user input in Clojure with `read-line`, ClojureScript has no consistent way to
do so (although some specific cljs repls like planck add that functionality
themselves). Because of this issue, it's very difficult to create a true
interactive program as described above.

I've gotten some interesting tips about it, and @mfikes kindly pointed to
his abio library (https://github.com/abiocljs) which begins to address the
problem. I'm hoping that at some point I'll manage to put together a truly
agnostic solution (PRs welcome!).

But now that I've created the semi-interactive version as an interim solution,
it turns out it has some real advantages. Some people may actually prefer it,
since it keeps you in an ordinary REPL at all times. Its only downside is that
you have to type a few more characters.

```
         Fully-interactive:                |   Semi-interactive:
                                           |
PROS:  - fewer keystrokes                  | - Runs anywhere
       - extremely fast                    | - Easy to do other stuff in the middle of a session
                                           | - You're always in a normal REPL
                                           |
CONS:  - Clojure-only                      | - Requires more keystrokes
       - Can't do other stuff in the       | - Not quite as fast to explore data
         middle of a session               |
```

## Configuration

`datawalk.print` contains a `config` atom holding several entries which you can
  alter to change the behavior of datawalk. These configs all affect the
  representation of the current data structure, not the underlying behavior.

* `:max-items` the top level of your data structure may be a sequence with
  hundreds or thousands of items; you probably don't want to print them all.
  Defaults to 30.

* `:max-line-length` on each printed line, datawalk attempts to represent the
  entire contents of the data on that line. This can sometimes be enormously
  long. You may wish to tune it to match the width of your repl environment.

* `:max-key-length` when displaying maps, the keys may be so long that they take
  up most of the space defined by \*max-line-length\*. This is most often true
  with namespaced keys. \*max-key-length\* limits the space taken up by the
  keys, in order to be sure to leave room for the values. When keys must be
  chopped, they're chopped from the right to try to capture the name rather than
  the namespace.

* `:debug-mode` when this is truthy, datawalk will print the values of the current
  path, saved values, the-past, and the-future at each step.

`datawalk.core` also has a convenience function, `set-line-length`, which can be
  passed a single number. `max-line-length` will be set to this value, and
  `max-key-length` will be set to 20% of the value.

## Note for Emacs users

Emacs accepts user input (via `read-line`) in the minibuffer -- this can be
confusing when you first encounter it; the REPL buffer looks like it's hung.
Just look down at the minibuffer and you'll see you can enter a command there.
Note that it's important to q)uit the datawalk session, or your REPL buffer may
be left in a problematic state.

## License

Copyright © 2017 Jesse Davis (aka Egg Syntax)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
