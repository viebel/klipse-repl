(ns klipse-repl.eval
  (:require
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
  (defn custom-eval [x]
    (let [res (eval x)] 
      (if (seq? x)
        (cond 
          (= 'def (first x)) @res
          (= 'defn (first x)) (symbol (str "Function " (:name (meta res)) " created"))
          :else res)
        res)))
  (is (= (custom-eval '(def foo 42)) 42)
      (= (custom-eval '(defn foo [])) (symbol "Function foo created"))))
