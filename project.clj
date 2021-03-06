(defproject viebel/klipse-repl "0.2.3"
  :url "https://github.com/viebel/klipse-repl"
  :license {:name "Eclipse Public License"
            :distribution :repo
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :description "Beginners friendly Clojure REPL"
  :min-lein-version "2.8.1"
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  :lein-tools-deps/config {:config-files [:install :user :project]}
  :plugins [[lein-tools-deps "0.4.1"]])
