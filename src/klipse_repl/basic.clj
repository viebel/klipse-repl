(ns klipse-repl.basic
  (:require
   [clojure.main :as clojure-main]))

(defn print-welcome-message! []
  (println "Welcome to a basic REPL")
  (println "Clojure" (clojure-version)))

(defn create-repl []
  (clojure-main/repl
   :init (fn []
           (print-welcome-message!))))

(defn -main []
  (create-repl)) 
