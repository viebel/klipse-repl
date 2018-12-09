
[![Clojars Project](https://img.shields.io/clojars/v/viebel/klipse-repl.svg)](https://clojars.org/viebel/klipse-repl)

# Rationale

Provide a REPL that is super-friendly to beginners without bothering them with details that are hard to grasp when you are a Clojure beginner.

For instance,

1. `def` forms return the value of the var instead of the var itself
2. `defn` forms return a message saying that a function has been created instead of returning the var of the function created



# How to install

If you want to try this really quickly
[install the Clojure CLI tools](https://clojure.org/guides/getting_started)
and then invoke this:

```shell
clojure -Sdeps "{:deps {viebel/klipse-repl {:mvn/version \"0.1.5\"}}}" -m klipse-repl.main
```

That should start a Clojure REPL that takes its input from the Rebel readline editor.

Note that I am using the `clojure` command and not the `clj` command
because the latter wraps the process with another readline program (rlwrap).

If you happen to like it, you will probably find it more convenient to specify an alias in your `$HOME/.clojure/deps.edn`

```clojure
{
 ...
 :aliases {:klipse-repl {:extra-deps {viebel/klipse-repl {:mvn/version "0.1.9"}}
                         :main-opts  ["-m" "klipse-repl.main"]}}
}
```

And then run with a simpler:

```shell
$ clojure -A:klipse-repl
```

Still, I am using the `clojure` command and not the `clj` command
because the latter wraps the process with another readline program (rlwrap).


# Features

## Gentle message when creating variable and functions

In a ususal REPL, when you create a `var` or a function, the REPL displays the fully qualified name of the `var` that has been created, for instance `#'user/foo`. This can be a bit confusing for Clojure beginners who have no idea what a namespace is and what is the meaning of the `#'` symbol. In the Klipse REPL, we display a gentle message:

1. `def` forms return the value of the var
2. `defn` forms return a message displaying the name of the function and the arguments it expects.

~~~clojure
user=> (def my-var 42)
42
user=> (defn foo [x] (* 42 x))
Updated function foo ([x])
~~~

The features of this section can be disabled by the `--no-easy-defs` flag.

## Autocompletion, indentation, coloring etc...

All of the great features of Bruce Hauman's [rebel-readline](https://github.com/bhauman/rebel-readline) are available in this REPL for the simple reason that this REPL is built on top of [rebel-readline](https://github.com/bhauman/rebel-readline).


## Live dependency update

In a usual REPL, when you want to add dependecies, you have to update your `deps.edn` file and restart the REPL. The Klipse REPL supports hot loading of dependencies in two ways:

1. update `deps.edn` and call `(refresh-classpath)` without restarting the REPL.
2. add dependencies on the fly with `add-deps`.

### Refresh the classpath

Add dependencies in your global or local `deps.edn` and call `(refresh-classpath)` without restarting the REPL.

### Add dependencies on the fly 

Add one or more dependencies on the fly - following `deps.edn` format. Let's say you want to try a cool Clojure library like [cuerdas](https://funcool.github.io/cuerdas/latest/) that provides many string manipulation functions. Instead of modifying your `deps.edn` file and restarting the REPL, you can call `add-deps` inside the Klipse REPL, just like this:


~~~clojure
user=> (add-deps '{funcool/cuerdas {:mvn/version "2.0.5"}})
nil
user=> (require '[cuerdas.core :as str])
nil
user=> (str/strip-tags "<p>just <b>some</b> text</p>")
"just some text"
~~~

### Display the classpath

Display the classpath as a collection with `(classpath)`

In order to enable the features listed in this section, you need to pass the `--cool-forms flag`.

## Remote connection

You can launch the REPL as a socket REPL server and connect with it remotely

```shell
$ clojure -A:klipse-repl --port 9876
launching socket repl on port 9876
Welcome to Klipse REPL (Read-Eval-Print Loop)
Clojure 1.9.0
user=> (def a 1)
1
```

And then from another terminal, you connect with `nc` or `telnet`:

~~~bash
$ nc localhost 9876
Welcome to Klipse REPL (Read-Eval-Print Loop)
Clojure 1.9.0
user=> a
1
~~~

Be aware that the state of the REPL is shared between the host and all its remote connections.


# Credits

This REPL runs on top of the awesome [rebel-readline](https://github.com/bhauman/rebel-readline) by Bruce Hauman.
