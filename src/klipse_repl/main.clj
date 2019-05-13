(ns klipse-repl.main
  (:require
   [gadjett.core :refer [dbg]]
   [klipse-repl.repl :refer [create-repl]]
   [clojure.string :as string]
   [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  [["-h" "--help"]
   [nil "--cool-forms"]
   [nil "--[no-]rebel" "enable/disable rebel readline"
    :default true]
   [nil "--print-length LENGTH" "how many items of each collection the REPL will print"
    :parse-fn #(Integer/parseInt %)
    :validate [#(<= 0 %) "Must be a non negative number"]
    :default 1000]
   [nil "--[no-]easy-defs" "enable/disable easy def feedback"
    :default true]
   ["-p" "--port PORT" "Port Number for the socket repl"
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]])

(defn usage [options-summary]
  (->> ["klipse-repl: A beginner friendly REPL"
        "Usage: clj -A:klipse-repl [options]"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn validate-args
   "Validate command line arguments. Either return a map indicating the program
  should exit (with a error message, and optional ok status), or a map
  indicating the action the program should take and the options provided."
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}

      errors
      {:exit-message (error-msg errors)}
      :else
      {:action "do" :options options})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [action options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (create-repl options))))
