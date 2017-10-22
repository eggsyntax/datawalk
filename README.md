# datawalk

Provides a simple interactive data explorer.

(datawalk.core/repl my-data-structure)

I've spent an awful lot of time at the REPL navigating through complex nested
data structures, often maps with long namespaced keys. I prefer to minimize the
amount of typing I have to do for repetitive tasks, and this is certainly one of
them.

So datawalk pretty-prints the top level of your data structure, and then lets
you drill down repeatedly, typically with just a single keystroke (the number
representing the item's position in the printed list, as shown next to the
item) plus enter.

Often the reason I'm drilling down is to determine a path to pass to `get-in`,
so datawalk will track and [p]rint the path from the original root to the level
you're on.

At any point in this process, you can [s]ave the current item or sa[v]e the
path to the item to a map of saved items which will be returned when you [q]uit.
You can also e[x]it and return just the current item.

You can also move [b]ackward or [f]orward in time, or move [u]p a level, or jump
back to the original [r]oot.

Finally, you can use [!] to call an arbitrary single-arg function on the current
data (jumping to the result), and you can print [h]elp to get a reminder of
these actions.

datawalk tries to do one thing well: eliminate tedium when navigating data
structures. I hope you find it as useful as I have :)

## Usage summary:

```
(datawalk.core/repl my-data-structure) to start datawalking.

numbers: Enter any number to jump to the corresponding item
q :exit ; exit and return saved values if any
x :exit-with-current ; exit & return just this value
s :save-current ; save to map of return values
v :save-path ; save path to map of return values
b :backward ; step backward in history
f :forward ; step forward in history
r :root ; jump back to root
u :up ; step upward [provides list of referring entities]
h :help ; print help & return same ent
p :print-path ; path: print path to current item.
! :function ; call an arbitrary 1-arg fn on data, jump to result
```

# Configuration

datawalk.print contains several dynamic vars which you can rebind anywhere
to change the behavior of datawalk. These vars all affect the representation
of the current data structure, not the underlying behavior.

\*max-items\*: the top level of your data structure may be a sequence with
  hundreds or thousands of items; you probably don't want to print them all.
  Defaults to 30.

\*max-line-length\*: on each printed line, datawalk attempts to represent the
  entire contents of the data on that line. This can sometimes be enormously
  long. You may wish to tune it to match the width of your repl environment.

\*max-key-length\*: when displaying maps, the keys may be so long that they
  take up most of the space defined by *max-line-length*. This is most often
  true with namespaced keys. *max-key-length* limits the space taken up by
  the keys, in order to be sure to leave room for the values. When keys must
  be chopped, they're chopped from the right to try to capture the name
  rather than the namespace.

\*debug-mode\*: when this is on, datawalk will print the values of the current
  path, saved values, the-past, and the-future at each step.

# ClojureScript

ClojureScript presents a challenge: while you can trivially get user input in
Clojure with `read-line`, ClojureScript has no way to do so (although some
specific cljs repls like planck add that functionality themselves). Because of
this issue, it's very difficult to create a true interactive program as
described above.

I've had some interesting discussions about it, and @mfikes kindly pointed to
his abio library (https://github.com/abiocljs) which begins to address the
problem. I'm hoping that at some point I'll manage to put together a truly
agnostic solution (PRs welcome!).

In the meantime, there's a semi-interactive solution that can be used in
*all* REPLs, Clojure and ClojureScript. Some people may actually prefer it,
since it keeps you in an ordinary REPL at all times. Its only downside is that
you have to type a few more characters.

To use this solution (assuming you've referred datawalk.core/look-at and
datawalk.core/w), start by calling

`user> (look-at my-data-structure)`

This initializes datawalk's internal state, so that it's always tracking a
piece of current data. From there, call `(w *)`, where `*` represents any of
the commands listed in the usage summary. So for example:

`user> (w 2) ; drill down to item 2 in the numbered sequence`
`user> (w b) ; step backward in time`
`user> (w s) ; save the current data to the map of saved values`

After each step, the current state will be stored in datawalk atoms and can
be freely accessed:

`datawalk.datawalk/data` stores the current data.
`datawalk.datawalk/saved` holds the map of stored values.

In the same ns, you can also access `the-root`, `the-past`, and `the-future`.

Additionally, datawalk's built-in printing tools are available:
`user> (w n)` prints the map of saved values.
`user> (w p)` prints the path from the root to the current data.

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
