Intellij Setup for Development
--------------------------------

In this project, the backend services and components are mainly built using 
Java/Kotlin (JVM). Thus, it is preferable to adopt 
**JetBrains Intellij IDEA** as the main development platform.

For developer productivity, it is crucial to setup Intellij IDE with 
various tool automation to make the development workflow easier while
still ensuring that produced code meet certain quality requirements and 
is consistent to agreed coding standard and style.

#### Intellij Plugins

Download the following plugins for Intellij 2018.1.x:

- [SaveActions](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=42637) (part of the Ultimate Edition)
- [File Watchers](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=45106) (Version: 181.4668.7)
- [SonarLint](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=45676) (Version: 3.4.0.2532)
- [CheckStyle-IDEA](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=44868) (Version: 5.18.6)
- [FindBugs-IDEA](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=29582) (Version: 1.0.1)
- [PMDPlugin](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=43058) (Version: 1.8.4)
- [Kotlin](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=44362) (Version: 1.2.31-release-IJ2018.1-1)
- [Protobuf Support](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=37332) (Version: 0.10.2)
- [NodeJS](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=45105) (Version: 181.4668.7)
- [Gerrit](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=44224) (Version: 1.0.6-146)

- [Prettier](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=45734) (Version: 181.5087.4)
- [Go](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=45394) (Version: 181.4668.90)
- [Python](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=45346) (Version: 181.4668.68)


Copy the files to `$HOME\.IntelliJIdea2018.1/config/plugins`. 
Alternatively, you can perform the following for each plugins to install:

1. Open the Settings / Preferences dialog (e.g. Ctrl+Alt+S).
2. In the left-hand pane, select Plugins.
3. In the right-hand part, on the Plugins page, click Install plugin from disk.
4. In the dialog that opens, select the plugin archive file, and click OK.
5. In the Settings / Preferences dialog, click Apply or OK.
6. If suggested, restart IntelliJ IDEA.

#### Intellij Configuration for Better Kotlin Experience

1. Go to Intellij Settings
2. Navigate to Editor > General > Appearance > [x] Show parameter hints > Configure
3. check / enable these options
    - Show property type hints
    - Show local variable type hints
    - Show function return type hints
    
<img here>



#### Java Coding Standard

We follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) with the following exceptions:
- Indentation is +4 spaces instead of +2 spaces
- Column limit or max line length is 120 instead of 100

The coding style is enforced by the use of CheckStyle during compile / build.
The checkstyle configuration file can be found at `$rootDir/gradle/config/google_checks.xml`.

To detect known bad practices and potential bugs, we employed static code 
analyzers such as `PMD` `SpotBugs`, and `Sonarqube` (Sonar Way Quality Profile).

Most potential issues should be identified by `Sonarqube` while `PMD` augmented the rules 
with other rules that `Sonarqube` has yet to implement. 

`SpotBugs`, which looks at the byte codes generated from compilation, is used for detecting performance and security related issues.


#### Kotlin Coding Standard  

We follow code style from [kotlinlang.org](https://kotlinlang.org/docs/reference/coding-conventions.html) and those in
the Google [Android Kotlin Style Guide](https://android.github.io/kotlin-guides/style.html).    

Similar to Java, we have the following deviations from the style guide:
- Indentation is +4 spaces instead of +2 spaces
- Column limit or max line length is 120 instead of 100

These exceptions are configured via `.editorconfig`.

The coding style is enforced by the use of `Ktlint` during compile / build.

To detect known bad practices and potential bugs, we employed `Delekt` for static code 
analysis.
 
The same set of coding issues should also be identified by `Sonarqube` (with Delekt plugin, aka `Delekt Way`).

#### Javascript / React Coding Standard  

We follow [Airbnb](https://github.com/airbnb/javascript) style guide for both ES6 and React components.

The style is enforced with the use of `eslint`. Common bad practices are also checked via `eslint`. 

 
