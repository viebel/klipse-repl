(ns run-tests
  (:require [clojure.test :refer [run-all-tests]]))

(require 'klipse-repl.core)
(clojure.test/run-all-tests #"klipse-repl.*")

