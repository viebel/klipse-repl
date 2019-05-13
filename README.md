
[![Clojars Project](https://img.shields.io/clojars/v/viebel/klipse-repl.svg)](https://clojars.org/viebel/klipse-repl)

# Rationale

Provide a REPL that is super-friendly to beginners without bothering them with details that are hard to grasp when you are a Clojure beginner.

For instance,

1. `def` forms return the value of the var instead of the var itself
2. `defn` forms return a message saying that a function has been created instead of returning the var of the function created



# How to install

If you want to try the Klipse REPL really quickly, [install the Clojure CLI tools](https://clojure.org/guides/getting_started)
and then invoke this:

```shell
clojure -Sdeps "{:deps {viebel/klipse-repl {:mvn/version \"0.2.3\"}}}" -m klipse-repl.main
```

That should start the Klipse REPL and displays a welcome message like this one:

```shell
Welcome to Klipse REPL (Read-Eval-Print Loop)
Clojure 1.10.0-beta3
user=>
```

Note that I am using the `clojure` command and not the `clj` command because the latter wraps the process with another readline program (rlwrap) which is not necessary because the history is handled by the Klipse REPL itself.

If you happen to like it, you will probably find it more convenient to specify an alias in your `$HOME/.clojure/deps.edn`

```clojure
{
 ...
 :aliases {:klipse-repl {:extra-deps {viebel/klipse-repl {:mvn/version "0.2.3"}}
                         :main-opts  ["-m" "klipse-repl.main"]}}
}
```

And then run with a simpler:

```shell
> clojure -A:klipse-repl
Welcome to Klipse REPL (Read-Eval-Print Loop)
Clojure 1.10.0-beta3
user=>
```

Note that I am using the `clojure` command and not the `clj` command because the latter wraps the process with another readline program (rlwrap) which is not necessary because the history is handled by the Klipse REPL itself.


# Features

## Prevents terminal hang on infinite sequences

It occurs sometimes, that you type in the REPL an expression that generated sequence like `(range)`. In the usual REPL, it will display an infinite sequence of numbers on the screen until you quit the REPL. What a frustration! Now you have to start your REPL session over again.

In the Klipse REPL, we limit to 1000 the number of items of each collection that are displayed. It prevents the REPL to hang when you evaluate an infinite sequence like `(range)`.

In case, you need a different limit for the number of items to be displayed, you can pass your number to the command line arguments `--print-length`. For instance, to limit it to 10, you run:

```shell
> clojure -A:klipse-repl --print-length 10
Welcome to Klipse REPL (Read-Eval-Print Loop)
Clojure 1.10.0-beta3
user=> (range)
(0 1 2 3 4 5 6 7 8 9 ...)
```


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

All of the great features of Bruce Hauman's [rebel-readline](https://github.com/bhauman/rebel-readline) are available in this REPL for the simple reason that this REPL is built on top of [rebel-readline](https://github.com/bhauman/rebel-readline). Some of them are:

1. autocompletion when pressing `TAB`
2. indentation of multi-line expressions
3. coloring of forms

See [rebel-readline](https://github.com/bhauman/rebel-readline) for the full list of features.

## Link to online documentation

[clojuredocs.org](https://clojuredocs.org/) is one of the best resources for Clojure beginers as it provides examples of usage of the Clojure forms. The `doc` macro proviced by Klipse REPL adds a link to the entry of the form in `clojuredocs`. For instance, take a look at the last line of the output of `(doc inc)`:

~~~clojure
user=> (doc inc)
-------------------------
clojure.core/inc
([x])
  Returns a number one greater than num. Does not auto-promote
  longs, will throw on overflow. See also: inc'
-------------------------
Online doc: https://clojuredocs.org/clojure.core/inc
~~~

## Live dependency update

In a usual REPL, when you want to add dependencies, you have to update your `deps.edn` file and restart the REPL. The Klipse REPL supports hot loading of dependencies in two ways:

1. update `deps.edn` and call `(refresh-classpath)` without restarting the REPL.
2. add dependencies on the fly with `add-deps`.

In order to enable the features listed in this section, you need to pass the `--cool-forms flag`, like this:

```shell
clojure -A:klipse-repl --cool-forms
```

### Refresh the classpath

Add dependencies in your global or local `deps.edn` and call `(refresh-classpath)` without restarting the REPL.

### Display the classpath

Display the classpath as a collection with `(classpath)`.

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
