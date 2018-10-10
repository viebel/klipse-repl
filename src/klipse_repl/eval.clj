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


(with-test
  (defn eval-defn [form]
    (try
      (let [func-name (dbg (second form))
            func-exists? (dbg (resolve func-name))
            created-or-updated (if func-exists? "updated" "created")
            res (eval form)]
        (symbol (str "Function " (:name (meta res)) " " created-or-updated)))
      (catch Exception e
        (eval form))))
  (is (= (eval-defn '(defn foo222 [])) (symbol "Function foo created")) ))

(defn eval-def [form]
  (deref (eval form)))

(with-test
  (defn custom-eval [x]
    (if (= 'defn (first x))
      (if (seq? x)
        (cond
          (= 'defn (first x)) (eval-defn x)
          (= 'def (first x)) (eval-def x)
          :else (eval x))
        (eval x))))
  (is (= (custom-eval '(def foo 42)) 42)
      (= (custom-eval '(defn foo [])) (symbol "Function foo created"))))
