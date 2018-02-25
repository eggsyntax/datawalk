(defproject datawalk "0.1.13-SNAPSHOT"
  :description "A single-purpose tool for rapid REPL exploration of complex data structures"
  :url "https://github.com/eggsyntax/datawalk"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.908"]]
  :plugins [[lein-cljsbuild "1.1.7"]]
  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-to "war/javascripts/main.js"  ; default: target/cljsbuild-main.js
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :lein-release {:deploy-via :lein-install}
  :profiles {:dev {:dependencies [
                                  ;; [org.clojure/test.check "0.9.0"]
                                  [org.clojure/tools.nrepl "0.2.10"]
                                  [com.cemerick/piggieback "0.2.2"]]

                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
