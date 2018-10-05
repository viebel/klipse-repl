(ns klipse-repl.deps
  (:require [clojure.java.io :as io]
            [clojure.tools.deps.alpha.reader :as reader]
            [klipse-repl.classpath :refer [add-classpath]]
            [clojure.tools.deps.alpha :as deps :refer [resolve-deps make-classpath]]))


(defn clj-config-dir []
  (when-let [env-var (System/getenv "CLJ_CONFIG")]
    (when-let [file (io/file env-var)]
      (when (.exists file)
        file))))

(defn user-config-directory []
  (or (clj-config-dir)
      (str (System/getenv "HOME") "/.clojure")))

(defn deps-files []
  [(io/file (user-config-directory) "deps.edn")
   (io/file "deps.edn")])

(defn combine-deps-files
  "Given a configuration for config-files and optional config-data, read
  and merge into a combined deps map."
  [{:keys [config-files config-data] :as opts}]
  (let [deps-map (reader/read-deps config-files)]
    (if config-data
      (reader/merge-deps [deps-map config-data])
      deps-map)))

(defn updated-classpath [config-files-and-data]
  (as->
   (combine-deps-files config-files-and-data) $
   (resolve-deps $ nil)
   (vals $)
   (mapcat :paths $)))


(defn update-classpath [config-files-and-data]
  (doseq [path (updated-classpath config-files-and-data)]
    (add-classpath path)))

(defn add-deps [deps-map]
  (update-classpath {:config-files (deps-files)
                     :config-data {:deps deps-map}}))

(defn refresh-classpath []
  (update-classpath {:config-files (deps-files)}))

(comment
  (add-deps '{viebel/gadjett {:mvn/version "0.5.2"}})
  (require 'tupelo.core)
  (str (io/file "/tmp/aa"))
  (refresh-classpath)
  )
