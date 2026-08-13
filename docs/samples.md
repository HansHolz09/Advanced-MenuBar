# Sample applications

The repository contains shared IntelliJ IDEA run configurations in the
[`.run` directory](https://github.com/HansHolz09/Advanced-MenuBar/tree/main/.run). They are the
easiest way to launch the samples in the environments users will actually distribute:

- **AWT Sample Distributable** — packaged Compose Desktop/AWT sample;
- **Tao Sample Distributable** — packaged Nucleus Tao sample;
- **Tao Sample GraalVM** — GraalVM Native Image variant of the Tao sample.

The equivalent Gradle commands are:

```shell
./gradlew :sample-awt:runDistributable
./gradlew :sample-tao:runDistributable
./gradlew :sample-tao:runGraalvmNative
```

For a quicker development launch without building a distributable, use:

```shell
./gradlew :sample-awt:run
./gradlew :sample-tao:run
```

The AWT sample demonstrates native, default, full, compatibility, and Swing menus; live custom
menus; multiple windows; localization; icons; checkboxes; editable text and a
`SelectionContainer`; theme changes; and both native text context-menu variants. Run it on Windows
or Linux to inspect platform accelerators and Swing capability differences.

The Tao sample demonstrates the same AppKit menu and editable/read-only text context-menu behavior
in a Nucleus Tao window without AWT.
Packaged and GraalVM runs provide a more representative environment for macOS system integrations
than the development `run` task.
