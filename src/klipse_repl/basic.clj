(ns klipse-repl.basic
  (:require
   [klipse-repl.eval :refer [custom-eval repl-init]]
   [clojure.core.server :as clojure-server]
   [clojure.main :as clojure-main]))

(defn print-welcome-message! []
  (println "Welcome to a basic REPL")
  (println "Clojure" (clojure-version))
  (flush))

(defn create-repl []
  (clojure-main/repl
   :eval custom-eval
   :init (fn []
           (print-welcome-message!)
           (repl-init))))

(defn launch-socket-repl-server [port]
  (println "launching socket repl on port" port)
  (clojure-server/start-server {:port port
                                :name 'socket-repl
                                :accept 'klipse-repl.basic/create-repl}))

(defn repl [{:keys [port]}]
  (launch-socket-repl-server port)
  (create-repl))

(defn -main [& args]
  (repl {:port 6666})) 
