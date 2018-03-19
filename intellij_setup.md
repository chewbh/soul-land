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

Download the following plugins for Intellij 2017.3.x:

- [SaveActions](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=42637) (part of the Ultimate Edition)
- [File Watchers](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=42137) (Version: 173.4301.12)
- [SonarLint](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=43883) (Version: 3.2.1.2406)
- [CheckStyle-IDEA](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=44107) (Version: 5.18.4)
- [FindBugs-IDEA](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=29582) (Version: 1.0.1)
- [PMDPlugin](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=43058) (Version: 1.8.4)
- [Kotlin](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=43774) (Version: 1.2.30-release-IJ2017.3-1)
- [Protobuf Support](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=37332) (Version: 0.10.2)
- [NodeJS](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=42139) (Version: 173.4301.12)
- [Gerrit](https://plugins.jetbrains.com/plugin/download?rel=true&updateId=44224) (Version: 1.0.6-146)


Copy the files to `$HOME\.IntelliJIdea2017.3/config/plugins`. 
Alternatively, you can perform the following for each plugins to install:

1. Open the Settings / Preferences dialog (e.g. Ctrl+Alt+S).
2. In the left-hand pane, select Plugins.
3. In the right-hand part, on the Plugins page, click Install plugin from disk.
4. In the dialog that opens, select the plugin archive file, and click OK.
5. In the Settings / Preferences dialog, click Apply or OK.
6. If suggested, restart IntelliJ IDEA.


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

