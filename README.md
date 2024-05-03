![The banner logo for The Bumblezone with a friendly and hostile bee shown below the mod name](https://github.com/TelepathicGrunt/Bumblezone/assets/40846040/968c8470-6ff7-4b11-8f45-da255d1de7ca)

# See the wiki for more details about this bee-tastic mod!

***

## COMPILEONLY MAVEN

For developers that want to add Bumblezone to their mod's workspace:

```gradle
repositories {
  maven {
     url "https://nexus.resourcefulbees.com/repository/telepathicgrunt/"
  }
}
```

&nbsp;

Don't forget to change <modversion> with the actual latest version of this mod like `7.2.0` for example. (See older branches's README.md for their stuff)

```gradle
dependencies {
   ...
   NEOFORGE: 
     compileOnly fg.deobf("com.telepathicgrunt:Bumblezone:<modversion>+1.20.2-neoforge")
     
   FABRIC/QUILT: 
     modCompileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.20.2-fabric"
     
   ARCH COMMON MODULE: 
     modCompileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.20.2-common"
}
```

***

## IMPLEMENTATION MAVEN

For developers that want to add Bumblezone to their mod's workspace:

```gradle
repositories {
    maven {
        url = 'https://nexus.resourcefulbees.com/repository/maven-public/'
        content {
            includeGroup 'com.telepathicgrunt'
            includeGroup 'earth.terrarium.athena'
        }
    }
    
    // Fabric/Quilt only
    maven {
        url = 'https://api.modrinth.com/maven/'
        content {
            includeGroup 'maven.modrinth'
        }
    }
    maven {
        name = "Ladysnake Mods"
        url = 'https://maven.ladysnake.org/releases'
    }
}
```

&nbsp;

Don't forget to change <modversion> with the actual latest version of this mod like `7.2.0` for example. (See older branches's README.md for their stuff)

```gradle
dependencies {
   ...
   NEOFORGE: 
     implementation fg.deobf("com.telepathicgrunt:Bumblezone:<modversion>+1.20.2-neoforge")
     implementation fg.deobf("earth.terrarium.athena:athena-forge-1.20.1:3.1.1")
     
   FABRIC/QUILT: 
     modImplementation "com.telepathicgrunt:Bumblezone:<modversion>+1.20.2-fabric"
     modImplementation "earth.terrarium.athena:athena-fabric-1.20.1:3.1.1"
     modImplementation "maven.modrinth:midnightlib:1.4.1-fabric" 
   
   ARCH COMMON MODULE: 
     modCompileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.20.2-common"
}
```
