(ns klipse-repl.main
  (:require
   [klipse-repl.eval :refer [custom-eval]]
   [rebel-readline.core :refer [with-readline-in]]
   [rebel-readline.clojure.line-reader :as line-reader]
   [rebel-readline.clojure.service.local :as rebel-service]
   [clojure.main :as clojure-main]))

(defn -main []
  (with-readline-in
    (line-reader/create
     (rebel-service/create))
    (clojure-main/repl
     :init (fn [] (println "klipse-repl"))
     :eval custom-eval
     :prompt (fn []))))
