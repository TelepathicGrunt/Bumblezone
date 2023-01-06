# MAVEN

For developers that want to add Bumblezone to their mod's workspace:

<blockquote>repositories {

&nbsp; maven {

&nbsp; &nbsp; url "https://nexus.resourcefulbees.com/repository/telepathicgrunt/"

&nbsp; }

}</blockquote>

&nbsp;

Don't forget to change \<modversion> with the actual latest version of this mod like `4.1.2` for example.

<blockquote>dependencies {


&nbsp; ...


&nbsp; implementation fg.deobf("com.telepathicgrunt:Bumblezone:\<modversion>+1.18.1")


}</blockquote>

&nbsp;

**Add the mixingradle to your buildscript's dependencies block. These will allow Blame's mixins to work. After you add the properties lines, refresh Gradle and run `genEclipseRuns` or `genIntellijRuns` or `genVSCodeRuns` based on what IDE you are using.**

https://github.com/SpongePowered/Mixin/wiki/Mixins-on-Minecraft-Forge#step-1---adding-the-mixingradle-plugin

<blockquote>buildscript {

&nbsp; &nbsp; ...

&nbsp; &nbsp; dependencies {

&nbsp; &nbsp; &nbsp; &nbsp; classpath group: 'net.minecraftforge.gradle', name: 'ForgeGradle', version: '5.1.+', changing: true

&nbsp; &nbsp; &nbsp; &nbsp; // MixinGradle:

&nbsp; &nbsp; &nbsp; &nbsp; classpath 'org.spongepowered:mixingradle:0.7.+'

&nbsp; &nbsp; }

}</blockquote>

**____________________________________________________________________________**

&nbsp;

![The banner logo for The Bumblezone with a friendly and hostile bee shown below the mod name.](https://i.imgur.com/ovPqrFL.png)

