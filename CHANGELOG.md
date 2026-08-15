TODO: DO NOT RELEASE UNTIL COMPLETED

- Make TradeHintParticle work again or move it into the Bee Queen Renderer instead (moving to bee queen renderer may be best way to go)
  - common/src/main/java/com/telepathicgrunt/the_bumblezone/client/particles/TradeHintParticle.java
- Fix bee chestplate animations not working
  - Do `/give @s the_bumblezone:bumble_bee_chestplate_1` and `/give @s the_bumblezone:bumble_bee_chestplate_2` to get both chestplate variants
  - Holding jump button will allow flight briefly. Wings should flutter when applying flight.
- Fix Cosmic Crystal laser beam not being full bright on all sides
- Fix Cosmic Crystal not facing correct direction during some attacks
  - Do `/give @s the_bumblezone:essence_of_the_bees` and consume right by holding right click.
  - Then do `/setblock ~3 ~ ~ the_bumblezone:essence_block_white` and touch the block. Cosmic Crystals will spawn.

- Test the everliving hell out of EVERYTHING (music needs checking)
- Commission new textures for spawn eggs
- run oxipng to shrink texture sizes


### **(V.7.9.0 Changes) (26.1.2 Minecraft)**

#### Misc:
Ported to 26.1.2

Special thanks to all those who helped me out with questions about the porting!

Also thanks to XFactHD who did the Framed Blocks compatibility porting work and GizmoTheMoonPig for porting entity renderers!

#### Mod Compat:
Brand-new compatibility has been added for Reliable Recipe Viewer mod (RRV).