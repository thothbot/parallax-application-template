![Parallax 3D library](https://github.com/thothbot/parallax/wiki/images/logo.png)

Application template for [Parallax SDK](https://github.com/thothbot/parallax)

[Wiki](https://github.com/thothbot/parallax/wiki)
| [Bugs](https://github.com/thothbot/parallax/issues)

### How to start

Download Parallax application template, using git ([more info](https://help.github.com/articles/cloning-a-repository/))

```sh
git clone https://github.com/thothbot/parallax-application-template.git
```

Parallax 2.0 SDK uses [gradle](http://gradle.org) to build, run or test your applications. Gradle wrapper is included in your application template main directory already and can be called by typing `gradlew` in a OS console.

This application template uses Parallax snapshots. Basically gradle downloads new snapshots if available, if not you can update dependencies by:

```
gradlew build --refresh-dependencies
```

If you want to use stable version of Parallax (when ready), just update your `build.gradle` config:

```
// parallaxVersion = '2.0-SNAPSHOT'
// changed to stable version
parallaxVersion = '2.0'
```

### Parallax cross-platform application structure

#### /core

In the folder `/core` are located common assets and cross-platform code.

Assets are located in the `/core/assets` folder. There can be textures, fonts, models etc.
Links to all files should be relative to the `/core/assets` folder, this is needed because all assets are copied to required platform when needed. Common case to use assets in your application is:

```java
// Your file is /core/assets/crate.gif
new Texture("crate.gif");
```

Cross-platform code is located in the '/core/src'. There is required class which implements `Animation` interface of `AnimationAdapter` abstract class. Common case to write simple application is

```java
public class MyAnimation extends AnimationAdapter {
    public void onStart(RenderingContext context) {
    };

    public void onUpdate(RenderingContext context) {
    };
}
```

#### /gwt

In the `/gwt` folder is locaded code for GWT which is compiled to Java Script, which can be launched in any modern browsers.

To run your application in GWT [super dev mode](http://www.gwtproject.org/articles/superdevmode.html) just type in console:

```
gradlew :gwt:superDev
```

After that open ```127.0.0.1:8080/gwt``` in your browser.

#### /android

coming soon

### Issues

**In case of:**
```
Execution failed for task ':core:compileJava'.
> Could not find tools.jar
```
You need to create:
* a System Variable `JAVA_HOME` in Windows Environment Variables with path to the latest JDK folder.
* or set `org.gradle.java.home` to the location of JDK in `gradle.properties`. The `gradle.properties`
file looked up by gradle is located in the users home directory, in the `.gradle` subdirectory.
(eg. `c:/users/me/.gradle/gradle.properties`).
For example: `org.gradle.java.home = c:/Program Files/Java/jdk1.7.0_03`
