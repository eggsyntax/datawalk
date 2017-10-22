# datawalk

Provides a simple interactive data explorer.

(datawalk.core/datawalk my-data-structure)

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

# ClojureScript

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
