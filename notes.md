- ClojureScript ideas:
  - Would use a customized clojure.main repl, but AFAIK it won't work w cljs.
  - A key challenge for making this work in cljs is that generic user input
    isn't available in cljs. I brought it up in #clojurescript, and found the
    following resources:
    - https://clojurians.slack.com/archives/C03S1L9DN/p1508602683000025
      - discussion goes at least until 2:30pm
      - note especially: `:special-fns` get processed by clj
    - https://github.com/abiocljs (@mfikes)
    - https://github.com/potetm/tire-iron
    - https://www.google.com/search?q=IReplEnvOption
  - As far as I can tell, special fns are generally defined by the repl, so it
    may not be possible to inject them for my own needs unless I create my own
    repl.
    - see https://github.com/bhauman/lein-figwheel/blob/bddbcc0fe326ea1e4aafb649a0b95c9113377611/sidecar/src/figwheel_sidecar/system.clj
  - Other options:
    - I could just make a totally ordinary fn and make users use that at the
    cljs repl -- ie just print it out, and then do
    user> (x b) ; backward
    user> (x 3) ; drill to item 3
    ...and so on.
    - I could make users run in a specific env, eg planck or node. Unfortunately,
      reading input seems to be wildly inconsistent across browsers (eg
      SpiderMonkey has `read-line`), so I couldn't even make something that
      worked in all browser repls :(
    - I could make users copy-paste over into clj :(
