(ns datawalk.dwprotocol)

(defprotocol Datawalkable
  "Describes how a type of thing should stringify itself for datawalk.
  Implementations should work in both clj and cljs (cl-format is useful for
  this). Output will be chopped off at max-line-length characters, and should
  be designed accordingly. dw-to-string should have two arities, one which
  prints the item by itself, in such a way that it can be embedded in a line,
  and one which prints for the topmost level. The latter mostly only applies
  to items which are in some sense seqable collections, and the convention
  for their top-level behavior is that each member of the collection is
  printed on a separate line, preceded by a number which should represent
  the nth member. See stringify-seq and stringify-map for examples. For
  items which are not conceptually a seqable collection, the two arities
  can and generally should be identical."
  (dw-to-string [data] [data top-level] "convert to a string for datawalk")
  (dw-drill [n data] "drill down into item n of this data"))
