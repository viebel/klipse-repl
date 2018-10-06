
[![Clojars Project](https://img.shields.io/clojars/v/viebel/klipse-repl.svg)](https://clojars.org/viebel/klipse-repl)

# Rationale

Provide a REPL that is super-friendly to beginners without bothering them with details that are hard to grasp when you are a Clojure beginner.

For instance,

1. `def` forms return the value of the var instead of the var itself
2. `defn` forms return a message saying that a function has been created instead of returning the var of the function created



## How to install

If you want to try this really quickly
[install the Clojure CLI tools](https://clojure.org/guides/getting_started)
and then invoke this:

```shell
clojure -Sdeps "{:deps {viebel/klipse-repl {:mvn/version \"0.1.1\"}}}" -m klipse-repl.main
```

That should start a Clojure REPL that takes its input from the Rebel readline editor.

Note that I am using the `clojure` command and not the `clj` command
because the latter wraps the process with another readline program (rlwrap).

If you happen to like it, you will probably find it more convenient to specify an alias in your `$HOME/.clojure/deps.edn`

```clojure
{
 ...
 :aliases {:rebel {:extra-deps {viebel/klipse-repl {:mvn/version "0.1.1"}}
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

## def and defn

The features of this section can be disabled by the `--no-easy-defs` flag.

1. `def` forms return the value of the var instead of the var itself
2. `defn` forms return a message saying that a function has been created instead of returning the var of the function created


## Live dependency update

In order to enable the features listed in this section, you need to pass the `--cool-forms flag`.

### refresh-deps

add a dependency in your global or local `deps.den` and call `(refresh-deps)`

### add-deps 

add one or more dependcies on the fly:

~~~clojure
user=> (add-deps '{tupelo {:mvn/version "0.9.103"}})
~~~

## Classpath

Display classpath as a collection with `(classpath)`

## Socket REPL

You can launch the REPL as a socket REPL server and connect with it remotely

```shell
$ clojure -A:klipse-repl --port 9876
launching socket repl on port 9876
Welcome to Klipse REPL (Read-Eval-Print Loop)
Clojure 1.9.0
user=> (def a 1)
```

And then from another terminal, you connect with `nc` or `telnet`:

~~~bash
$ nc localhost 9876
Welcome to Klipse REPL (Read-Eval-Print Loop)
Clojure 1.9.0
user=> a
1
~~~

The state of the REPL is share between the host and all its remote connections.

# Credits

This REPL runs on top of the awesome [rebel-readline](https://github.com/bhauman/rebel-readline).
