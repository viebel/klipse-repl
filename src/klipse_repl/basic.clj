(ns klipse-repl.basic
  (:require
   [klipse-repl.eval :refer [custom-eval repl-init]]
   [clojure.main :as clojure-main]))

(defn print-welcome-message! []
  (println "Welcome to a basic REPL")
  (println "Clojure" (clojure-version)))

(defn create-repl []
  (clojure-main/repl
   :eval custom-eval
   :init (fn []
           (print-welcome-message!)
           (repl-init))))

(defn -main []
  (create-repl)) 
