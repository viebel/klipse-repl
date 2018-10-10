(ns klipse-repl.eval
  (:require
   [gadjett.core :refer [dbg]]
   [clojure.test :refer [with-test is]]
   [clojure.main :refer [repl-requires]]))


(defn repl-init
  "Initialize repl in user namespace and make standard repl requires."
  [{:keys [cool-forms]}]
  (in-ns 'user)
  (apply require repl-requires)
  (if cool-forms
    (do
      (require '[gadjett.core :refer [dbg dbgdef]])
      (require '[klipse-repl.deps :refer [refresh-classpath add-deps]])
      (require '[klipse-repl.classpath :refer [classpath]])
      (println "some cool forms are available in this REPL"))
    (println "cool forms are disabled. Enable them with --cool-forms")))

(defn ns-unmap-in-current-ns [name]
  (ns-unmap (symbol (str *ns*)) name))

(comment (ns-unmap-in-current-ns 'foo))

(with-test
  (defn eval-defn [[_ func-name :as form]]
    (try
      (let [func-exists? (resolve func-name)
            created-or-updated (if func-exists? "updated" "created")
            res (eval form)]
        (symbol (str "Function " (:name (meta res)) " " created-or-updated)))
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
