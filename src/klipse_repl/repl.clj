(ns klipse-repl.repl
  (:require
   [clojure.core.server :as clojure-server]
   [klipse-repl.eval :refer [custom-eval repl-init]]
   [rebel-readline.core :refer [with-readline-in]]
   [rebel-readline.clojure.line-reader :as line-reader]
   [rebel-readline.clojure.service.local :as rebel-service]
   [clojure.main :as clojure-main]))

(defn launch-socket-repl-server [port]
  (println "launching socket repl on port" port)
  (clojure-server/start-server {:port port
                                :name 'socket-repl
                                :accept 'klipse-repl.basic/create-repl}))

(defn create-repl [{:keys [port easy-defs] :as opts}]
  (when port
    (launch-socket-repl-server port))
  (with-readline-in
    (line-reader/create
     (rebel-service/create))
    (clojure-main/repl
     :init (fn []
             (println "Welcome to Klipse REPL (Read-Eval-Print Loop)")
             (println "Clojure" (clojure-version))
             (repl-init opts))
     :eval (if easy-defs custom-eval eval)
     :prompt (fn [])))
  )
