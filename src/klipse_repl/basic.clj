(ns klipse-repl.basic
  (:require
   [clojure.main :as clojure-main]))

(defn -main []
  (clojure-main/repl
   :init (fn []
           (println "Welcome to a basic REPL")
           (println "Clojure" (clojure-version)))))
