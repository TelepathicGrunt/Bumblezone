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

Don't forget to change &lt;modversion> with the actual latest version of this mod like `7.5.0` for example. (See older branches's README.md for their stuff)

```gradle
dependencies {
   ...
   NEOFORGE: 
     compileOnly fg.deobf("com.telepathicgrunt:Bumblezone:<modversion>+1.20.6-neoforge")
     
   FABRIC/QUILT: 
     modCompileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.20.6-fabric"
     
   ARCH COMMON MODULE: 
     modCompileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.20.6-common"
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
            includeGroup 'com.teamresourceful.resourcefullib'
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

Don't forget to change &lt;modversion> with the actual latest version of this mod like `7.5.0` for example. (See older branches's README.md for their stuff)

```gradle
dependencies {
   ...
   NEOFORGE: 
     implementation fg.deobf("com.telepathicgrunt:Bumblezone:<modversion>+1.20.6-neoforge")
     implementation fg.deobf("earth.terrarium.athena:athena-forge-1.20.5:3.4.0")
     implementation fg.deobf("com.teamresourceful.resourcefullib:resourcefullib-forge-1.20.5:2.6.0-beta.7")
     
   FABRIC/QUILT: 
     modImplementation "com.telepathicgrunt:Bumblezone:<modversion>+1.20.6-fabric"
     modImplementation "earth.terrarium.athena:athena-fabric-1.20.5:3.4.0"
     implementation fg.deobf("com.teamresourceful.resourcefullib:resourcefullib-fabric-1.20.5:2.6.0-beta.7")
     modImplementation "maven.modrinth:midnightlib:1.5.5-fabric" 
   
   ARCH COMMON MODULE: 
     modCompileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.20.6-common"
}
```
