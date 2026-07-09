TODO: DO NOT RELEASE UNTIL COMPLETED

- Update Essence Block Shader to work
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/blockentityrenderer/EssenceBlockEntityRenderer.java
  - common/src/main/resources/assets/the_bumblezone/shaders
- Make TradeHintParticle work again or move it into the Bee Queen Renderer instead
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/particles/TradeHintParticle.java
- entity renderers need to be fixed. Good luck and start crying
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/rendering
- GUIs/screens needs fixing. Also cryable
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/screens
- Bumblezone armor rendering needs to be redone. Bruh
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/armor
  - neoforge/src/main/java/com/telepathicgrunt/the_bumblezone/mixin/neoforge/client/BzArmorMixin.java
  - neoforge/src/main/java/com/telepathicgrunt/the_bumblezone/neoforge/NeoForgeClientEventManager.java#createArmorExtension
- Fix fluid rendering. Hahahaha...
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/rendering/fluids
- Fix the Fabric fluid mixin code
  - fabric/src/main/java/com/telepathicgrunt/the_bumblezone/mixin/fabric/entities/EntityMixin.java
  - fabric/src/main/java/com/telepathicgrunt/the_bumblezone/mixin/fabric/entities/EntityAccessor.java
- Glistering Honey Crystal block's baked/unbaked model code needs updating on Fabric. Faaaaaaaaaaabrrrrriiiiiiiiiiiiccccc
  - fabric/src/main/java/com/telepathicgrunt/the_bumblezone/client/fabric
- Update optional mod compat versions to latest 26.1.2 version and then if they are on 26.1.2, update the compat code so it works again
  - common/src/main/gradle.properties
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/modcompat
  - fabric/src/main/java/com/telepathicgrunt/the_bumblezone/modcompat/fabric
  - neoforge/src/main/java/com/telepathicgrunt/the_bumblezone/modcompat/neoforge


### **(V.7.9.0 Changes) (26.1.2 Minecraft)**

#### Misc:
Ported to 26.1.2