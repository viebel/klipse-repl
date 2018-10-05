(ns klipse-repl.classpath
  (:refer-clojure :exclude (add-classpath))
  (:require [clojure.java.io :as io]
            [dynapath.util :as dp]))

(defn classloader-hierarchy
  "Returns a seq of classloaders, with the tip of the hierarchy first.
   Uses the current thread context ClassLoader as the tip ClassLoader
   if one is not provided."
  ([] (classloader-hierarchy (.. Thread currentThread getContextClassLoader)))
  ([tip]
    (->> tip
      (iterate #(.getParent %))
      (take-while boolean))))

(defn modifiable-classloader?
  "Returns true iff the given ClassLoader is of a type that satisfies
   the dynapath.dynamic-classpath/DynamicClasspath protocol, and it can
   be modified."
  [cl]
  (dp/addable-classpath? cl))

(defn a-modifiable-class-loader []
  (->> (classloader-hierarchy)
       (filter modifiable-classloader?)
       last))

(defn add-classpath
  "A corollary to the (deprecated) `add-classpath` in clojure.core. This implementation
   requires a java.io.File or String path to a jar file or directory, and will attempt
   to add that path to the right classloader (with the search rooted at the current
   thread's context classloader).
   Inspired by https://github.com/cemerick/pomegranate"
  ([jar-or-dir classloader]
   (if-not (dp/add-classpath-url classloader (.toURL (.toURI (io/file jar-or-dir))))
     (throw (IllegalStateException. (str classloader " is not a modifiable classloader")))))
  ([jar-or-dir]
   (if-let [cl (a-modifiable-class-loader)]
     (add-classpath jar-or-dir cl)
     (throw (IllegalStateException. (str "Could not find a suitable classloader to modify from "
                                         (classloader-hierarchy)))))))

(defn classpath []
  (->> (dp/all-classpath-urls (.. Thread currentThread getContextClassLoader))
       (map str)))

(comment
  (classpath))
