### **(V.7.19.0 Changes) (1.20.1 Minecraft)**

#### Structures:
Tweaked what modded blocks shows in Hanging Gardens

#### Items:
Adjusted packet logic for syncing horse owner to clients from server so that it no longer broadcasts to all players.
 Reduces server load every time a horse is spawned into client's loaded chunks.

Optimized Buzzing Briefcase menu clicks to not deserialize all bees in briefcase before validating if the button's action can proceed.

Optimized Buzzing Briefcase menu clicks to not send button click to server if player clicks an action button without the required inventory item it needs.

#### Blocks:
Ensured that `crystallineFlowerConsumeExperienceUI` and `crystallineFlowerConsumeItemUI` config options are respected on serverside for Crystalline Flower.
