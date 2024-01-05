# An0mCorpses
A simple lootable corpses plugin - **ONLY works on 1.16.5** (Spigot and forks)

### [Download it now!](https://github.com/An0m/An0mCorpses/releases/latest/download/An0mCorpses.jar)

![generic](https://raw.githubusercontent.com/An0m/An0mCorpses/main/images/Generic.png)

## Features:
 - Estensive events and API
 - Support for different block heights (Exibits [A](https://raw.githubusercontent.com/An0m/An0mCorpses/main/images/Height.png) & [B](https://raw.githubusercontent.com/An0m/An0mCorpses/main/images/Height2.png))
 - Always place on solid blocks (min 0.5 x 0.5 non passable blocks) - Aka: fix spawning INSIDE [doors](https://raw.githubusercontent.com/An0m/An0mCorpses/main/images/Doors.png), trapdoors, ecc.
 - Keep player death drops in the corpse [inventory](https://raw.githubusercontent.com/An0m/An0mCorpses/main/images/Generic.png) (open on click)
 - *Automatic corpses removal (scheduled)
 - *Hold player exp (given to the first opener)
 - *Don't spawn corpse in fire or lava
 - *Remove corpse when their inventory is emptied
   
*Editable or toggleable in config

---

Or you can just check the [config](https://github.com/An0m/An0mCorpses/blob/main/src/main/resources/config.yml) for yourself!

Note: the corpses keeps the chunk loaded

---

## Maven? Here you go!
```xml
<repositories>
    <repository>
        <id>an0m-repo</id>
        <url>https://an0m.dev/maven/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>dev.an0m</groupId>
        <artifactId>An0mCorpses</artifactId>
        <version>LATEST</version> <!--Or just use a specific version-->
    </dependency>
</dependencies>
```
