(ns klipse-repl.eval
  (:require
   [gadjett.core :refer [dbg]]
   [clojure.test :refer [with-test is]]
   [clojure.tools.namespace.repl :as repl]
   [clojure.java.io :as io]
   [clojure.main :refer [repl-requires]]))


(defn repl-init
  "Initialize repl in user namespace and make standard repl requires."
  [{:keys [cool-forms]}]
  (in-ns 'user)
  (apply require repl-requires)
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
        (symbol (str created-or-updated " function aaa ggggg " (:name (meta res)) " " (:arglists (meta res)))))
      (catch Exception e
        (println "an exception occured" e)
        (eval form))))
  (is (do
        (ns-unmap-in-current-ns 'foo)
        (= (eval-defn '(defn foo [])) (symbol "Function foo created"))) ))

(defn eval-defn-2 [x]
  (eval-defn x))

(defn eval-def [form]
  (deref (eval form)))

(comment
  (symbol (str *ns*)))


(with-test
  (defn custom-eval [x]
    (repl/set-refresh-dirs (io/file "src"))
    (repl/refresh)
    (println "ccc hhh koko")
    (if (seq? x)
      (cond
        (= 'defn (first x)) (eval-defn-2 x)
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
