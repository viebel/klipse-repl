(ns klipse-repl.repl
  (:require
   [gadjett.collections :refer [apply-with-map]]
   [gadjett.core :refer [dbg]]
   [clojure.core.server :as clojure-server]
   [io.aviso.repl :as pretty]
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

(defn create-repl* [{:keys [easy-defs] :as opts} prompt?]
  (let [args {:init (fn []
                      (println "Welcome to Klipse REPL (Read-Eval-Print Loop)")
                      (println "Clojure" (clojure-version))
                      (repl-init opts))
              :eval (fn [x]
                      (if easy-defs ((eval `custom-eval) x) (eval x)))
              :prompt (when-not prompt? (fn []))}]
    (apply-with-map clojure-main/repl args)))

(defn create-repl [{:keys [port rebel pretty] :as opts}]
  (when pretty
    (pretty/install-pretty-exceptions))
  (when port
    (launch-socket-repl-server port))
  (if rebel
    (with-readline-in
      (line-reader/create
       (rebel-service/create))
      (create-repl* opts false))
    (create-repl* opts true)))
