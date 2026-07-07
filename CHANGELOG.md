TODO: DO NOT RELEASE UNTIL COMPLETED

- Update Essence Block Shader to work
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/blockentityrenderer/EssenceBlockEntityRenderer.java
  - common/src/main/resources/assets/the_bumblezone/shaders
- Make TradeHintParticle work again 
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/particles/TradeHintParticle.java
- Item model are redone and need to be setup
  - common/src/main/resources/assets/the_bumblezone/models/item
- Item properties for model state needs setup
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/items
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/BumblezoneClient.java#registerItemProperties
- entity renderers need to be fixed
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/rendering
- GUIs/screens needs fixing
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/screens
- DimensionTeleportingScreen needs hookup on fabric
  - fabric/src/main/java/com/telepathicgrunt/the_bumblezone/mixin/fabric/client/LevelLoadingScreenMixin.java
- Bumblezone armor rendering needs to be redone
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/armor
  - neoforge/src/main/java/com/telepathicgrunt/the_bumblezone/mixin/neoforge/client/BzArmorMixin.java
  - neoforge/src/main/java/com/telepathicgrunt/the_bumblezone/neoforge/NeoForgeClientEventManager.java#createArmorExtension
- Music manager needs to be completely redone
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/MusicHandler.java
- Fix fluid rendering
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/rendering/fluids
- Figure out what to do about neoForge fluid type code. (Possibly just copy fabric's platform service code in meantime)
    - neoforge/src/main/java/com/telepathicgrunt/the_bumblezone/services/neoforge/NeoPlatformService.java#getFluidHeight
    - neoforge/src/main/java/com/telepathicgrunt/the_bumblezone/services/neoforge/NeoPlatformService.java#isEyesInNoFluid
- Fix the Fabric fluid mixin code
  - fabric/src/main/java/com/telepathicgrunt/the_bumblezone/mixin/fabric/entities/EntityMixin.java
  - fabric/src/main/java/com/telepathicgrunt/the_bumblezone/mixin/fabric/entities/EntityAccessor.java
- Replace the custom RenderType creation/registration code with the appropriate closest replacements
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/BumblezoneClient.java$ENTITY_CUTOUT_EMISSIVE_RENDER_TYPE
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/BumblezoneClient.java$ENTITY_TRANSPARENT_EMISSIVE_RENDER_TYPE
- Paralysis effect of causing visual entity shaking needs new solution due to entity render states. 
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/mixin/client/LivingEntityRendererMixin.java
- Glistering Honey Crystal block's baked/unbaked model code needs updating on Fabric
  - fabric/src/main/java/com/telepathicgrunt/the_bumblezone/client/fabric
- Update optional mod compat versions to latest and then if they are on 26.1.2, update the compat code so it works again
  - common/src/main/gradle.properties
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/modcompat
  - fabric/src/main/java/com/telepathicgrunt/the_bumblezone/modcompat/fabric
  - neoforge/src/main/java/com/telepathicgrunt/the_bumblezone/modcompat/neoforge


### **(V.7.9.0 Changes) (26.1.2 Minecraft)**

#### Misc:
Ported to 26.1.2