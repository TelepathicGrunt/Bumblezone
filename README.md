![The banner logo for The Bumblezone with a friendly and hostile bee shown below the mod name](https://github.com/TelepathicGrunt/Bumblezone/assets/40846040/968c8470-6ff7-4b11-8f45-da255d1de7ca)

# See the wiki for more details about this bee-tastic mod!

***

The Bumblezone is LGPLv3 (LICENSE.txt) except for the assets under all resources/assets which is All Rights Reserved (LICENSE_ASSETS.txt)

***

## COMPILEONLY MAVEN

For developers that want to add Bumblezone to their mod's workspace:

```gradle
repositories {
    exclusiveContent {
        forRepository {
            maven {
                url = "https://nexus.resourcefulbees.com/repository/telepathicgrunt/"
            }
        }
        filter {
            includeGroupAndSubgroups("com.telepathicgrunt")
        }
    }
}
```

&nbsp;

Don't forget to change &lt;modversion> with the actual latest version of this mod like `7.8.11` for example. (See older branches's README.md for their stuff)

```gradle
dependencies {
   ...
   NEOFORGE: 
     compileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.21.5-neoforge"
     
   FABRIC: 
     compileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.21.5-fabric"
     
   COMMON MODULE: 
     compileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.21.5-common"
}
```

***

## IMPLEMENTATION MAVEN

For developers that want to add Bumblezone to their mod's workspace:

```gradle
repositories {
    exclusiveContent {
        forRepository {
            maven {
                url = "https://nexus.resourcefulbees.com/repository/maven-public/"
            }
        }
        filter {
            includeGroupAndSubgroups("com.telepathicgrunt")
            includeGroupAndSubgroups("com.teamresourceful.resourcefullib")
            includeGroupAndSubgroups("earth.terrarium.athena")
        }
    }
    
    // Fabric only
    exclusiveContent {
        forRepository {
            maven {
                url = "https://api.modrinth.com/maven/"
            }
        }
        filter {
            includeGroupAndSubgroups("maven.modrinth")
        }
    }
}
```

&nbsp;

Don't forget to change &lt;modversion> with the actual latest version of this mod like `7.8.11` for example. (See older branches's README.md for their stuff)

```gradle
dependencies {
   ...
   NEOFORGE: 
     implementation "com.telepathicgrunt:Bumblezone:<modversion>+1.21.5-neoforge"
     implementation "earth.terrarium.athena:athena-forge-1.21.4:4.2.0"
     implementation "com.teamresourceful.resourcefullib:resourcefullib-forge-1.21.5:3.5.0"
     
   FABRIC: 
     modImplementation "com.telepathicgrunt:Bumblezone:<modversion>+1.21.5-fabric"
     modImplementation "earth.terrarium.athena:athena-fabric-1.21.4:4.2.0"
     modImplementation "com.teamresourceful.resourcefullib:resourcefullib-fabric-1.21.5:3.5.0"
     modImplementation "maven.modrinth:midnightlib:1.7.1+1.21.4-fabric" 
   
   COMMON MODULE: 
     compileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.21.5-common"
}
```
