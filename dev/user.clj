(ns user
  (:require [hawk.core :as hawk]
            [clojure.java.io :as io]
            [clojure.tools.namespace.repl :as repl]))
(comment
  (println "auto-reload files is on")
  (repl/set-refresh-dirs (io/file "src"))

  (hawk/watch! [{:paths ["src"]
                 :handler (fn [ctx e]
                            (println "reload")
                            (future
                              (repl/refresh)))}]))


