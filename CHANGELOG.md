TODO: DO NOT RELEASE UNTIL COMPLETED

- entity renderers need to be fixed. Good luck and start crying
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/rendering
  - common/src/main/resources/assets/the_bumblezone/shaders
- Make TradeHintParticle work again or move it into the Bee Queen Renderer instead (moving to bee queen renderer may be best way to go)
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/particles/TradeHintParticle.java
- Update optional mod compat versions to latest 26.1.2 version and then if they are on 26.1.2, update the compat code so it works again
  - common/src/main/gradle.properties
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/modcompat
  - fabric/src/main/java/com/telepathicgrunt/the_bumblezone/modcompat/fabric
  - neoforge/src/main/java/com/telepathicgrunt/the_bumblezone/modcompat/neoforge
- Search TODO and verify they are all completed


### **(V.7.9.0 Changes) (26.1.2 Minecraft)**

#### Misc:
Ported to 26.1.2

Special thanks to all those who helped me out with questions about the porting!

Also thanks to XFactHD who did the Framed Blocks compatibility porting work.

#### Mod Compat:
Brand new compatibility has been added for Reliable Recipe Viewer mod (RRV).