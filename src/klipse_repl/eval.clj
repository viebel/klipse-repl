(ns klipse-repl.eval
  (:require [clojure.test :refer [with-test is]]))


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
