![The banner logo for The Bumblezone with a friendly and hostile bee shown below the mod name](https://user-images.githubusercontent.com/40846040/211122783-e4bde0e7-f721-48aa-8095-54dd8319ed40.png)

# See the wiki for more details about this bee-tastic mod!

***

## MAVEN

For developers that want to add Bumblezone to their mod's workspace:

<blockquote>
repositories {

&nbsp;&nbsp;&nbsp;maven {

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;url "https://nexus.resourcefulbees.com/repository/maven-public/"

&nbsp;&nbsp;&nbsp;}

}
</blockquote>

&nbsp;

Don't forget to change \<modversion> with the actual latest version of this mod.

<blockquote>
dependencies {

...

&nbsp;&nbsp;&nbsp;&nbsp;modImplementation "com.telepathicgrunt:Bumblezone-Quilt:\<modversion>+1.19"

}</blockquote>
