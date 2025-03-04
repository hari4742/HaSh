# 🐚 HaSh - Hari's Shell

**HaSh** _(Hari's Shell)_ is a custom POSIX-compliant shell built from scratch in Java as a learning project. It provides an interactive command-line experience with essential shell functionalities, inspired by Unix-like shells. The goal of this project is to explore system programming concepts, shell internals, and command parsing while gradually adding new features.

## ✨ Features

✅ **Built-in Commands:** Includes `exit`, `type`, `echo`, `cd`, and `pwd`.

✅ **POSIX-style Quoting & Escaping:** Supports single quotes, double quotes, and backslash escaping.

✅ **Redirection Operators:** Handles input/output redirection for seamless command execution.

✅ **Auto-Completion:** Suggests commands on tab press for a smoother experience.

✅ **External Program Execution:** Runs system commands and executables like `ls`, `cat`, etc.

✅ **Shell Command Parsing:** Processes complex shell commands with arguments and options.

✅ **REPL (Read-Eval-Print Loop):** Interactive session for executing multiple commands.

✅ **Maven Build:** Easily compile and run with Maven.

🚀 **Planned Features:**

🔹 Auto Completion for paths

🔹 Command History

🔹 Piping (`|`)

🔹 Job Control (background processes)

🔹 More Built-in Utilities

## 🛠 Installation

### 🏃 Run with Prebuilt JAR (Recommended)

1. Download the latest `hash.jar` from the [Releases](https://github.com/hari4742/HaSh/releases) section.

2. Open a terminal and run:

```sh
java -jar hash.jar
```

## 🏗️ Build from Source

If you want to modify or contribute:

```sh
git clone https://github.com/hari4742/HaSh.git
cd HaSh
mvn package
java -jar target/hash.jar
```

## 📺 Preview

When you launch HaSh, you’ll be greeted with:

```text
 _    _        _____ _
| |  | |      / ____| |
| |__| | __ _| (___ | |__
|  __  |/ _` |\___ \| '_ \
| |  | | (_| |____) | | | |
|_|  |_|\__,_|_____/|_| |_|

Welcome to HaSh - Hari's Shell!
```

## 🙌 Acknowledgments

- This project is build with guidance of codecrafters as part of the ["Build Your Own Shell" Challenge](https://app.codecrafters.io/courses/shell/overview).

- Special thanks to CodeCrafters. Learn more about them [here](./code-crafters.md).
