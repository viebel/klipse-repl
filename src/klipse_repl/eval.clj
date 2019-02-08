(ns klipse-repl.eval
  (:require
   [clojure.string :as string]
   [gadjett.core :refer [dbg]]
   [clojure.test :refer [with-test is]]))

(defn repl-init
  "Initialize repl in user namespace and make standard repl requires."
  [{:keys [cool-forms]}]
  (in-ns 'user)
  (require '[klipse-repl.eval :refer [doc]])
  (require '[clojure.repl :refer (source apropos dir pst find-doc)])
  (require '[clojure.java.javadoc :refer (javadoc)])
  (require '[clojure.pprint :refer (pp pprint)])
  
  (when cool-forms
    (require '[gadjett.core :refer [dbg dbgdef]])
    (require '[klipse-repl.deps :refer [refresh-classpath add-deps]])
    (require '[klipse-repl.classpath :refer [classpath]])
    (println " Debugging: (dbg an-expression)")
    (println "            (dbgdef an-expression)")
    (println " Classpath: (classpath)")
    (println "            (refresh-classpath)")
    (println "            (add-deps deps-coordinates)")))

(defn ns-unmap-in-current-ns [name]
  (ns-unmap (symbol (str *ns*)) name))

(comment (ns-unmap-in-current-ns 'foo))

(with-test
  (defn eval-defn [[_ func-name :as form]]
    (try
      (let [func-exists? (resolve func-name)
            created-or-updated (if func-exists? "Updated" "Created")
            res (eval form)]
        (symbol (str created-or-updated " function " (:name (meta res)) " " (:arglists (meta res)))))
      (catch Exception e
        (println "an exception occured" e)
        (eval form))))
  (is (do
        (ns-unmap-in-current-ns 'foo)
        (= (eval-defn '(defn foo [])) (symbol "Function foo created"))) ))

(defn eval-def [form]
  (deref (eval form)))

(comment
  (symbol (str *ns*)))

(with-test
  (defn custom-eval [x]
    (if (seq? x)
      (cond
        (= 'defn (first x)) (eval-defn x)
        (= 'def (first x)) (eval-def x)
        :else (eval x))
      (eval x)))
  (is (= (custom-eval '(def foo 42)) 42))
  (is (= (do
        (ns-unmap-in-current-ns 'foo)
        (custom-eval '(defn foo [])))
      (symbol "Function foo created")))
  (is (= (do
           (ns-unmap-in-current-ns 'foo)
           (custom-eval '(defn foo []))
           (custom-eval '(defn foo [])))
         (symbol (->> "Function foo updated")))))

(defn safe-resolve [s]
  (some-> s
          symbol
          (-> resolve (try (catch Throwable e nil)))))

(def safe-meta (comp meta safe-resolve))

(defn resolve-meta [var-str]
  (or (safe-meta var-str)
      (when-let [ns' (some-> var-str symbol find-ns)]
        (assoc (meta ns')
               :ns var-str))))

(defn url-for [ns name]
  (cond
    (.startsWith (str ns) "clojure.")
    (cond-> "https://clojuredocs.org/"
      ns (str ns)
      name (str "/" (string/replace name #"\?$" "_q")))
    :else nil))

(defn doc-url [var-str]
  (when-let [{:keys [ns name]} (resolve-meta var-str)]
    (url-for (str ns) (str name))))

(defn online-doc [var-str]
  (when-let [url (doc-url var-str)]
    (do (println "-------------------------")
        (println "Online doc:" url))))

(defmacro doc
  "Prints documentation for a var or special form given its name,
  or for a spec if given a keyword"
  [var]
  `(do (clojure.repl/doc ~var)
       (online-doc ~(str var))))

(comment
  (doc mape)
  (doc-url "clojure.string/replace")
  (resolve-meta "clojure.string/replace"))
