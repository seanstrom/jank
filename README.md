# The jank programming language [![Build](https://github.com/jank-lang/jank/actions/workflows/build.yml/badge.svg)](https://github.com/jank-lang/jank/actions/workflows/build.yml) [![codecov](https://codecov.io/gh/jank-lang/jank/branch/main/graph/badge.svg)](https://codecov.io/gh/jank-lang/jank) [![Sponsor](https://img.shields.io/static/v1?label=Sponsor&message=%E2%9D%A4&logo=GitHub&link=https://github.com/sponsors/jeaye&color=red)](https://github.com/sponsors/jeaye)

jank is a general-purpose programming language which embraces the interactive,
functional, value-oriented nature of Clojure, the desire for native compilation
and minimal runtimes of C++, and the gradual, structural typing of languages
like TypeScript. jank aims to be strongly compatible with Clojure. While
Clojure's default host is the JVM and its interop is with Java, jank's host is
LLVM and its interop is with C++ or LLVM IR.

https://jank-lang.org

For the current progress of jank and its usability, see the tables here: https://jank-lang.org/progress/

The current tl;dr for jank's usability is: **getting there, but not ready for
use yet**.

## Latest binaries
There are pre-compiled binaries for Ubuntu 22.04, which are built to follow the
`main` branch. You can download a tarball with everything you need here: https://github.com/jank-lang/jank/releases/tag/latest

## Building locally
### Dependencies
For Debian-based distros, this should be all you need:

```bash
$ sudo apt-get install -y curl git zip build-essential entr libssl-dev libdouble-conversion-dev pkg-config ninja-build python3-pip cmake debhelper devscripts gnupg zlib1g-dev
```

For macOS, try this:

```bash
$ brew install curl git zip entr openssl double-conversion pkg-config ninja python cmake gnupg zlib
```

Clone the repo as follows:

```bash
$ git clone --recurse-submodules https://github.com/jank-lang/jank.git

# If you didn't recurse submodules when cloning, you'll need to run this.
$ git submodule update --recursive --init
```

### Compiling Cling
Note that you must compile Cling/Clang/LLVM. This can take an hour or two,
depending on your machine. Building jank itself should take less than a minute.

```
$ mkdir -p build
$ cd build
$ ../bin/build-cling
$ export CC=$PWD/cling-build/bin/clang; export CXX=$PWD/cling-build/bin/clang++;
```

You then need to provide it to `./bin/configure` by using `-Djank_cling_build_dir=cling-build`.


### Compiling jank
* `./bin/configure` -- For setting up the project.
* `./bin/compile` -- For one-off compilation.
* `./bin/test` -- For one-off testing.
* `./bin/watch` -- For hot reloading on save.
* `./bin/install` -- For packaging.

#### Release
A typical release build just needs the following:

```bash
$ ./bin/configure -GNinja -DCMAKE_BUILD_TYPE=Release -Djank_cling_build_dir=cling-build
$ ./bin/compile
```

#### Debug
To make a debug build, specify the build type when configuring.

```bash
$ ./bin/configure -GNinja -DCMAKE_BUILD_TYPE=Debug -Djank_cling_build_dir=cling-build -Djank_tests=on
$ ./bin/compile

# When developing, continuously run the tests locally.
$ ./bin/watch ./bin/test
```

### Packaging
There's also a script for installing jank and all its necessary dependencies.
Note that this includes a lot of header files, which are necessary for jank's
JIT compilation.

```bash
$ ./bin/configure -GNinja -DCMAKE_BUILD_TYPE=Release -Djank_cling_build_dir=cling-build
$ ./bin/install
```

## Sponsors
If you'd like your name, company, or logo here, you can sponsor this project.
[![Sponsor](https://img.shields.io/static/v1?label=Sponsor&message=%E2%9D%A4&logo=GitHub&link=https://github.com/sponsors/jeaye&color=red)](https://github.com/sponsors/jeaye)

## In the news
<table>
  <tr>
    <td>The REPL Interview</td>
    <td>devmio Interview</td>
    <td>Compiler Spotlight</td>
  </tr>
  <tr>
    <td>
      <a href="https://www.therepl.net/episodes/44/">
        <img src="https://user-images.githubusercontent.com/1057635/193151333-449385c2-9ddb-468e-b715-f149d173e310.svg" height="100px">
      </a>
    </td>
    <td>
      <a href="https://devm.io/programming/jank-programming-language">
        <img src="https://user-images.githubusercontent.com/1057635/193151345-7ad97eb4-f0f9-485a-acbb-fbe796bb7919.svg" width="300px">
      </a>
    </td>
    <td>
      <a href="https://compilerspotlight.substack.com/p/language-showcase-jank">
        <img src="https://user-images.githubusercontent.com/1057635/193154279-4b57dd8b-0985-4e35-85a2-d25b046232c5.png" width="350px">
      </a>
    </td>
  </tr>
 </table>
