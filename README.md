![The banner logo for The Bumblezone with a friendly and hostile bee shown below the mod name](https://github.com/TelepathicGrunt/Bumblezone/assets/40846040/968c8470-6ff7-4b11-8f45-da255d1de7ca)

# See the wiki for more details about this bee-tastic mod!

***

## COMPILEONLY MAVEN

For developers that want to add Bumblezone to their mod's workspace:

```
repositories {
  maven {
     url "https://nexus.resourcefulbees.com/repository/telepathicgrunt/"
  }
}
```

&nbsp;

Don't forget to change \<modversion> with the actual latest version of this mod like `7.0.18` for example. (See older branches's README.md for their stuff)

```
dependencies {
   ...
   FORGE: 
     compileOnly fg.deobf("com.telepathicgrunt:Bumblezone:<modversion>+1.20.1-forge")
     
   FABRIC: 
     modCompileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.20.1-fabric"
     
   QUILT: 
     modCompileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.20.1-quilt"
     
   ARCH COMMON MODULE: 
     modCompileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.20.1-common"
}
```

&nbsp;

**FORGE ONLY: Add the mixingradle to your buildscript's dependencies block. These will allow Bumblezone's mixins to work. After you add the properties lines, refresh Gradle and run `genEclipseRuns` or `genIntellijRuns` or `genVSCodeRuns` based on what IDE you are using.**

https://github.com/SpongePowered/Mixin/wiki/Mixins-on-Minecraft-Forge#step-1---adding-the-mixingradle-plugin

```
buildscript {
   ...
   dependencies {
      // MixinGradle:
      classpath 'org.spongepowered:mixingradle:0.7.+'
   }
}
```

***

## IMPLEMENTATION MAVEN

For developers that want to add Bumblezone to their mod's workspace:

```
repositories {
    maven {
        url = 'https://nexus.resourcefulbees.com/repository/maven-public/'
        content {
            includeGroup 'com.telepathicgrunt'
            includeGroup 'earth.terrarium.athena'
        }
    }
    maven {
        url = 'https://jitpack.io'
        content {
            includeGroup 'com.github.llamalad7.mixinextras'
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

Don't forget to change \<modversion> with the actual latest version of this mod like `7.0.18` for example. (See older branches's README.md for their stuff)

```
dependencies {
   ...
   FORGE: 
     implementation fg.deobf("com.telepathicgrunt:Bumblezone:<modversion>+1.20.1-forge")
     implementation fg.deobf("earth.terrarium.athena:athena-forge-1.20.1:3.1.1")
     
   FABRIC: 
     modImplementation "com.telepathicgrunt:Bumblezone:<modversion>+1.20.1-fabric"
     modImplementation "earth.terrarium.athena:athena-fabric-1.20.1:3.1.1"
     modImplementation "maven.modrinth:midnightlib:1.4.1-fabric" 
   
   QUILT: 
     modImplementation "com.telepathicgrunt:Bumblezone:<modversion>+1.20.1-quilt"
     modImplementation "earth.terrarium.athena:athena-fabric-1.20.1:3.1.1"
     modImplementation "maven.modrinth:midnightlib:1.4.1-fabric" 
     
   ARCH COMMON MODULE: 
     modCompileOnly "com.telepathicgrunt:Bumblezone:<modversion>+1.20.1-common"
}
```

&nbsp;

**FORGE ONLY: Add the mixingradle to your buildscript's dependencies block. These will allow Bumblezone's mixins to work. After you add the properties lines, refresh Gradle and run `genEclipseRuns` or `genIntellijRuns` or `genVSCodeRuns` based on what IDE you are using.**

https://github.com/SpongePowered/Mixin/wiki/Mixins-on-Minecraft-Forge#step-1---adding-the-mixingradle-plugin

```
buildscript {
   ...
   dependencies {
      // MixinGradle:
      classpath 'org.spongepowered:mixingradle:0.7.+'
   }
}
```