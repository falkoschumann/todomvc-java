![Java CI](https://github.com/falkoschumann/todomvc-java/workflows/Java%20CI/badge.svg)

# TodoMVC

TODO: Description

## Installation

TODO: Installation

## Usage

TODO: Usage

## Contributing

- Distribution mit eingebetteter JRE erzeugen: `./gradlew jpackage`
- Native App erzeugen: `./gradlew nativeBuild`
    - Für signierten Build unter macOS muss in der `gradle.properties` die
      Property `mac.signingKeyUserName` auf den Namen des Schlüssels gesetzten
      werden z.B. `Max Mustermann (AB1234567890)`.
    - Für produktive Installer für Windows müssen die Properties
      `win.upgradeUuid` und `win.distributed.upgradeUuid` in der
      `gradle.properties` geändert werden.
- Der Code Style [Google Java Style Guide][1] wird beim Build geprüft.
- Code formatieren: `./gradlew spotlessApply`
- [Project Lombok][2] wird verwendet, um Boilerplate Code zu reduzieren, es
  werden nur stabile Features verwendet, zum Beispiel: `@NonNull`, `@Data`,
  `@Value` oder `@Builder`.
- Hinweis: Die Nutzung von `lombok.var` bricht die Kompatibilität mit Java 10
  und höher.

[1]: https://google.github.io/styleguide/javaguide.html
[2]: https://projectlombok.org
