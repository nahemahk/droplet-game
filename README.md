## Droplet Game

This is a game I made using as base [libGDX simple game](https://libgdx.com/wiki/start/a-simple-game).

### Controls

**WASD** - Move

**Shift** - Move faster while pressed

**F** - Toggle fullscreen

**Spacebar** - Pause game

**Enter** - Continue if game over

**ESC** - Exit game

## How to build

Requirements: [OpenJDK 17](https://www.azul.com/downloads/?version=java-17-lts&architecture=x86-64-bit&package=jdk#zulu) or newer versions.

### JAR (Java Runnable)

```gradlew.bat shadowJar```

*The JAR is located in desktop/build/lib*

### Windows Portable

```gradlew.bat jpackageImage```

### Linux Package / MacOS App

The first time you need to enable execution perms

```chmod +x gradlew```

Now you can enter

```./gradlew jpackageImage```

*Platform distributions are located in desktop/build/jpackage*
