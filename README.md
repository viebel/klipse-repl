
# Rationale

Provide a REPL that is super-friendly to beginners without bothering them with details that are hard to grasp when you are a Clojure beginner.

For instance,

1. `def` forms returns the value of the var instead of the var itself
2. `defn` forms returns a message saying that a function has been created instead of returning the var of the function created


# Run Locally

~~~bash
clojure -m klipse-repl.main
~~~

# Features



# Deploy

~~~bash
clojure -Spom
mvn deploy
~~~
