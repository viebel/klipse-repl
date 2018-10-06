(ns klipse-repl.eval
  (:require
   [clojure.test :refer [with-test is]]
   [clojure.main :refer [repl-requires]]))


(defn repl-init
  "Initialize repl in user namespace and make standard repl requires."
  []
  (in-ns 'user)
  (apply require repl-requires)
  (require '[gadjett.core :refer [dbg dbgdef]])
  (require '[klipse-repl.deps :refer [refresh-classpath add-deps]])
  (require '[klipse-repl.classpath :refer [classpath]]))

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
