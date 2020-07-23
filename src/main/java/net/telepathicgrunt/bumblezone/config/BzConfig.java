package net.telepathicgrunt.bumblezone.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.utils.ConfigHelper;
import net.telepathicgrunt.bumblezone.utils.ConfigHelper.ConfigValueListener;

@Mod.EventBusSubscriber
public class BzConfig
{
	public static class BzConfigValues
	{
	    // bee aggression
	    public ConfigValueListener<Boolean> allowWrathOfTheHiveOutsideBumblezone;
	    public ConfigValueListener<Boolean> showWrathOfTheHiveParticles;
	    public ConfigValueListener<Boolean> aggressiveBees;
	    public ConfigValueListener<Integer> aggressionTriggerRadius;
	    public ConfigValueListener<Integer> howLongWrathOfTheHiveLasts;
	    public ConfigValueListener<Integer> speedBoostLevel;
	    public ConfigValueListener<Integer> absorptionBoostLevel;
	    public ConfigValueListener<Integer> strengthBoostLevel;

	    // dimension
	    public ConfigValueListener<Boolean> dayNightCycle;
	    public ConfigValueListener<Double> fogBrightnessPercentage;
	    public ConfigValueListener<Boolean> ambienceNoise;
	    
	    // teleportation
	    public ConfigValueListener<Integer> teleportationMode;
	    public ConfigValueListener<Boolean> generateBeenest;
	    public ConfigValueListener<Integer> movementFactor;
	    public ConfigValueListener<Boolean> forceExitToOverworld;
	    public ConfigValueListener<String> requiredBlockUnderHive;
	    public ConfigValueListener<Boolean> warnPlayersOfWrongBlockUnderHive;
	    public ConfigValueListener<Boolean> allowTeleportationWithModdedBeehives;
	    public ConfigValueListener<Boolean> seaLevelOrHigherExitTeleporting;

	    // mod compatibility
	    public ConfigValueListener<Boolean> spawnHoneySlimeMob;
	    public ConfigValueListener<Boolean> allowHoneyWandCompat;
	    public ConfigValueListener<Boolean> allowBottledBeeCompat;
	    public ConfigValueListener<Boolean> hivePlanksWorldgen;
	    public ConfigValueListener<Boolean> waxBlocksWorldgen;
	    public ConfigValueListener<Boolean> crystallizedHoneyWorldgen;
	    public ConfigValueListener<Boolean> allowRegularCandlesBeeDungeon;
	    public ConfigValueListener<Boolean> allowScentedCandlesBeeDungeon;
	    public ConfigValueListener<Boolean> allowScentedCandlesSpiderBeeDungeon;
	    public ConfigValueListener<Integer> powerfulCandlesRarityBeeDungeon;
	    public ConfigValueListener<Integer> powerfulCandlesRaritySpiderBeeDungeon;

	    public ConfigValueListener<Boolean> spawnBeesourcefulBeesMob;
	    public ConfigValueListener<Boolean> spawnBeesourcefulHoneycombVariants;
	    public ConfigValueListener<Integer> greatHoneycombRarityBeeDungeon;
	    public ConfigValueListener<Double> oreHoneycombSpawnRateBeeDungeon;
	    public ConfigValueListener<Integer> greatHoneycombRaritySpiderBeeDungeon;
	    public ConfigValueListener<Double> oreHoneycombSpawnRateSpiderBeeDungeon;

	    public ConfigValueListener<Boolean> allowPotionOfBeesCompat;
	    public ConfigValueListener<Boolean> allowSplashPotionOfBeesCompat;

	    public ConfigValueListener<Boolean> spawnProductiveBeesBeesMob;
	    public ConfigValueListener<Boolean> spawnProductiveBeesHoneycombVariants;
	    public ConfigValueListener<Integer> PBGreatHoneycombRarityBeeDungeon;
	    public ConfigValueListener<Double> PBOreHoneycombSpawnRateBeeDungeon;
	    public ConfigValueListener<Integer> PBGreatHoneycombRaritySpiderBeeDungeon;
	    public ConfigValueListener<Double> PBOreHoneycombSpawnRateSpiderBeeDungeon;


	    // dungeons
	    public ConfigValueListener<Integer> beeDungeonRarity;
	    public ConfigValueListener<Integer> spiderInfestedBeeDungeonRarity;
	    public ConfigValueListener<Double> spawnerRateSpiderBeeDungeon;

	    // general mechanics
	    public ConfigValueListener<Boolean> dispensersDropGlassBottles;
	    public ConfigValueListener<Boolean> clearUnwantedBiomeFeatures;
	    public ConfigValueListener<Boolean> clearUnwantedBiomeMobs;

	    // mob controls
	    public ConfigValueListener<Double> phantomSpawnrate;
	    public ConfigValueListener<Double> endermanSpawnrate;
	    public ConfigValueListener<Double> spiderSpawnrate;
	    public ConfigValueListener<Double> caveSpiderSpawnrate;


	    public BzConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {
  
	        builder.push("The Bumblezone Dimension Options");

	        	clearUnwantedBiomeFeatures = subscriber.subscribe(builder
	                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	                    		+" EXPERIMENTAL!\r\n "
	                    		+" Will attempt to remove all biome features from Bumblezone's \r\n"
	                    		+" biomes and attempt to re-add them after all mods finish set-up.\r\n"
	                    		+" Use this if another mod is adding features/structures to Bumblezone's\r\n"
	                    		+" biomes and those mods didn't give a config to blacklist Bumblezone's\r\n"
	                    		+" biomes.\r\n"
	                    		+" (Be sure to let those mods know to add feature/structure configs though!)\r\n")
	                    .translation("the_bumblezone.config.dimension.clearunwantedbiomefeatures")
	                    .define("clearUnwantedBiomeFeatures", false));

	        	clearUnwantedBiomeMobs = subscriber.subscribe(builder
	                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	                    		+" EXPERIMENTAL!\r\n "
	                    		+" Will attempt to remove all biome features from Bumblezone's \r\n"
	                    		+" biomes and attempt to re-add them after all mods finish set-up.\r\n"
	                    		+" Use this if another mod is adding mobs to Bumblezone's\r\n"
	                    		+" biomes and those mods didn't give a config to blacklist Bumblezone's\r\n"
	                    		+" biomes.\r\n"
	                    		+" (Be sure to let those mods know to add mob configs though!)\r\n")
	                    .translation("the_bumblezone.config.dimension.clearunwantedbiomemobs")
	                    .define("clearUnwantedBiomeMobs", false));

	        	ambienceNoise = subscriber.subscribe(builder
	                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	                    		+" Turns on/off the buzzing ambience sound in Bumblezone's dimension.\r\n")
	                    .translation("the_bumblezone.config.dimension.ambiencenoise")
	                    .define("ambienceNoise", true));

	            dayNightCycle = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Determines if the day/night cycle active in the Bumblezone dimension.\r\n "
		                    		+" The cycle will be visible by the change in color of the fog. \r\n"
		                    		+" If kept on, the day/night cycle will match the Overworld's \r\n"
		                    		+" cycle even when players sleep to skip night in the Overworld.\r\n"
		                    		+" \r\n"
		                    		+" If this setting is set to false, the cycle \r\n"
		                    		+" will be stuck at \"noon\" for the dimension.\r\n")
		                    .translation("the_bumblezone.config.dimension.daynightcycle")
		                    .define("dayNightCycle", true));
	
	            
	            fogBrightnessPercentage = subscriber.subscribe(builder
	                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	                    		+" How bright the fog is in the Bumblezone dimension. \r\n"
	                    		+" This will always affect the fog whether you have the \r\n"
	                    		+" day/night cycle on or off.\r\n"
	                    		+" \r\n"
	                    		+" The brightness is represented as a percentage so if the \r\n"
	                    		+" cycle is off, 0 will be pitch black, 50 will be half as \r\n"
	                    		+" bright, 100 will be normal orange brightness, and \r\n"
	                    		+" 100000 will be white. When the cycle is on, 0 will be \r\n"
	                    		+" but will not be completely black during daytime.\r\n")
	                    .translation("the_bumblezone.config.dimension.fogbrightnesspercentage")
	                    .defineInRange("fogBrightnessPercentage", 100D, 0D, 100000D));
	
	            
                     teleportationMode = subscriber.subscribe(builder
                                     .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                                   		+" Which mode of teleportation should be used when \r\n"
                                   		+" leaving The Bumblezone dimension. \r\n"
                                		+" \r\n"
                                		+" Mode 1: Coordinates will be converted to the other \r\n"
                                		+" dimension's coordinate scale and the game will look for\r\n"
                                		+" a Beenest/Beehive at the new spot to spawn players at. \r\n"
                                		+" If none is found, players will still be placed at the spot.\r\n"
                                		+" \r\n"
                                		+" Mode 2: Will always spawn players at the original spot \r\n"
                                		+" in the non-BZ dimension where they threw the Enderpearl \r\n"
                                		+" at a Beenest/Beehive. Will place air if the spot is now filled \r\n"
                                		+" with solid blocks. \r\n"
                                		+" \r\n"
                                		+" Mode 3: Coordinates will be converted to the other \r\n"
                                		+" dimension's coordinate scale and the game will look for\r\n"
                                		+" a Beenest/Beehive at the new spot to spawn players at. \r\n"
                                		+" If none is found, players will spawn at the original spot\r\n"
                                		+" in the non-BZ dimension where they threw the Enderpearl \r\n"
                                		+" at a Beenest/Beehive. Will place air if the spot is now filled \r\n"
                                		+" with solid blocks. \r\n")
                                    .translation("the_bumblezone.config.dimension.teleportationmode")
                                    .defineInRange("teleportationMode", 1, 1, 3));
                
                     
                     movementFactor = subscriber.subscribe(builder
                                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                                		+" Determines how the coordinates gets translated when entering \r\n"
                                		+" and leaving the Bumblezone. The default ratio is 10 which means\r\n"
                                		+" for every block you traverse in the dimension, it is equal to\r\n"
                                		+" traveling 10 blocks in the Overworld. For comparison, the Nether\r\n"
                                		+" has a 8 to 1 ratio with the Overworld. \r\n"
                                		+" \r\n"
                                		+" The scaling of coordinates will take into account other dimension's\r\n"
                                		+" coordinate ratios so it'll work for any dimension correctly.\r\n"
                                		+" \r\n"
                                		+" Note: Changing this in an already made world will change where Bee Nests will\r\n"
                                		+" take you in the dimension and exiting will place you in a different spot too.\r\n"
                                		+" \r\n"
                                		+" ONLY FOR TELEPORTATION MODE 1 AND 3.\r\n")
                                    .translation("the_bumblezone.config.dimension.movementfactor")
                                    .defineInRange("movementFactor", 10, 1, 1000));
                     
                    generateBeenest = subscriber.subscribe(builder
        	                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
        	                    		+" Will a Beenest generate if no Beenest is  \r\n"
        	                    		+" found when leaving The Bumblezone dimension.\r\n"
                                    		+" \r\n"
                                    		+" ONLY FOR TELEPORTATION MODE 1.\r\n")
        	                    .translation("the_bumblezone.config.dimension.generatebeenest")
        	                    .define("generateBeenest", true));
    	            
	            forceExitToOverworld = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Makes leaving The Bumblezone dimension always places you back\r\n "
		                    		+" at the Overworld regardless of which dimension you originally \r\n"
		                    		+" came from. Use this option if this dimension becomes locked in  \r\n"
		                    		+" with another dimension so you are stuck teleporting between the \r\n"
		                    		+" two and cannot get back to the Overworld.\r\n")
		                    .translation("the_bumblezone.config.dimension.forceexittooverworld")
		                    .define("forceExitToOverworld", false));
	            

	            requiredBlockUnderHive = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" If a resource location of a block is specified here,\r\n "
		                    		+" then teleporting to Bumblezone will need that block under\r\n"
		                    		+" the Bee Nest/Beehive you threw the Enderpearl at.\r\n"
		                    		+" \r\n"
		                    		+" Example: minecraft:emerald_block will require you to place an\r\n"
		                    		+" Emerald Block under the Bee Nest/Beehive and then throw an\r\n"
		                    		+" Enderpearl at it to teleport to Bumblezone dimension.\r\n"
		                    		+" \r\n"
		                    		+" By default, no resource location is specified so any\r\n"
		                    		+" block can be under the Bee Nest/Beehive to teleport to dimension.\r\n")
		                    .translation("the_bumblezone.config.dimension.requiredblockunderhive")
		                    .define("requiredBlockUnderHive", ""));

	            seaLevelOrHigherExitTeleporting = subscriber.subscribe(builder
	                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	                    		+" Should exiting The Bumblezone always try and place you \r\n"
	                    		+" above sealevel in the target dimension? (Will only look \r\n"
	                    		+" for beehives above sealevel as well when placing you)"
                        		+" \r\n"
                        		+" ONLY FOR TELEPORTATION MODE 1 AND 3.\r\n")
	                    .translation("the_bumblezone.config.dimension.sealevelorhigherexitteleporting")
	                    .define("seaLevelOrHigherExitTeleporting", true));
	            
	            warnPlayersOfWrongBlockUnderHive = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" If requiredBlockUnderHive has a block specified and this config\r\n "
		                    		+" is set to true, then player will get a warning if they throw \r\n"
		                    		+" an Enderpearl at a Bee Nest/Beehive but the block under it is \r\n"
		                    		+" not the correct required block. It will also tell the player what \r\n"
		                    		+" block is needed under the Bee Nest/Beehive to teleport to the dimension.\r\n")
		                    .translation("the_bumblezone.config.dimension.warnplayersofwrongblockunderhive")
		                    .define("warnPlayersOfWrongBlockUnderHive", true));

	            
	            allowTeleportationWithModdedBeehives = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Should teleporting to and from The Bumblezone work \r\n"
		                    		+" with modded Bee Nests and modded Beehives as well. \r\n")
		                    .translation("the_bumblezone.config.dimension.allowteleportationwithmoddedbeehives")
		                    .define("allowTeleportationWithModdedBeehives", true));
	        
		builder.pop();
		
		builder.push("Dungeon Options");

    			beeDungeonRarity = subscriber.subscribe(builder
		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		            		+" How rare Bee Dungeons are. Higher numbers means more rare.\r\n"
		            		+" Default rate is 1. Setting to 1001 will disable Bee Dungeons.\r\n")
		            .translation("the_bumblezone.config.dungeons.beedungeonrarity")
		            .defineInRange("beeDungeonRarity", 1, 1, 1001));
		
    			spiderInfestedBeeDungeonRarity = subscriber.subscribe(builder
    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
    		            		+" How rare Spider Infested Bee Dungeons are. Higher numbers means more rare.\r\n"
    		            		+" Default rate is 8. Setting to 1001 will disable Bee Dungeons.\r\n")
    		            .translation("the_bumblezone.config.dungeons.spiderinfestedbeedungeonrarity")
    		            .defineInRange("spiderInfestedBeeDungeonRarity", 8, 1, 1001));

	    		spawnerRateSpiderBeeDungeon = subscriber.subscribe(builder
    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
    		            		+" How rare are Spider/Cave Spider Spawners in Spider Infested Bee Dungeons.\r\n"
    		            		+" 0 is no spawners, 1 is maximum spawners, and default is 0.2D\r\n")
    		            .translation("the_bumblezone.config.dungeons.spawnerratespiderbeedungeon")
    		            .defineInRange("spawnerRateSpiderBeeDungeon", 0.2D, 0D, 1D));
	    		
	        builder.pop();
	        
	        builder.push("Wrath of the Hive Options");
	
	        	builder.push("Bees Aggression Options");
	        	
		            allowWrathOfTheHiveOutsideBumblezone = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Determines if Wrath of the Hive can be applied to players outside\r\n"
		                    		+" the Bumblezone dimension when they pick up Honey blocks, take honey\r\n"
		                    		+" from Filled Porous Honey blocks, or drink Honey Bottles.\r\n")
		                    .translation("the_bumblezone.config.bees.allowwrathofthehiveoutsidebumblezone")
		                    .define("allowWrathOfTheHiveOutsideBumblezone", false));
		            
		            
		            showWrathOfTheHiveParticles = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Show the orangish particles when you get Wrath of the Hive\r\n"
		                    		+" after you angered the bees in the Bumblezone dimension.\r\n")
		                    .translation("the_bumblezone.config.bees.showwrathofthehiveparticles")
		                    .define("showWrathOfTheHiveParticles", true));
		
		            
		            aggressiveBees = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Turn off or on the ability to get Wrath of the Hive effect.\r\n"
		                    		+" \r\n"
		                    		+" The effect gets applied when you pick up Honey blocks, take\r\n"
		                    		+" honey from Filled Porous Honeycomb Blocks, or drink a\r\n"
		                    		+" Honey Bottle inside the Bumblezone dimension.\r\n"
		                    		+" Basically, bees become REALLY angry.\r\n"
		                    		+" \r\n"
		                    		+" In addition, the bees can see you through walls and will have \r\n"
		                    		+" speed, absorption, and strength effects applied to them.\r\n"
		                    		+" \r\n"
		                    		+" Will also affect the bee's aggression toward bears in the dimension.\r\n"
		                    		+" Note: Peaceful mode will always override the bee aggressive setting.\r\n")
		                    .translation("the_bumblezone.config.bees.aggressivebees")
		                    .define("aggressiveBees", true));
		
		            
		            aggressionTriggerRadius = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How far away the bee can be to become angry and hunt you down if\r\n "
		                    		+" you take their honey from the Bumblezone dimension.\r\n"
		                    		+" \r\n"
		                    		+" Will also affect the bee's aggression range toward bears in the dimension.\r\n")
		                    .translation("the_bumblezone.config.bees.aggressiontriggerradius")
		                    .defineInRange("aggressionTriggerRadius", 64, 1, 200));
	           
		            
		            howLongWrathOfTheHiveLasts = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How long bees will keep their effects for (speed, absorption, strength).\r\n"
		                    		+" Note: This is in ticks. 20 ticks = 1 second.\r\n")
		                    .translation("the_bumblezone.config.bees.howlongwrathofthehivelasts")
		                    .defineInRange("howLongWrathOfTheHiveLasts", 350, 1, Integer.MAX_VALUE));
	
		            
	            builder.pop();
	            
	            builder.push("Bees Effects Options");
		            
		            
		            speedBoostLevel = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How fast bees move along the ground (Not while flying).\r\n"
		                    		+" You will see this a lot when bees are about to attack\r\n"
		                    		+" you, they tend to touch the floor and the speed boost\r\n"
		                    		+" makes them dash forward at you. Set this to higher for\r\n"
		                    		+" faster dash attacks from bees.\r\n"
		                    		+" \r\n"
		                    		+" NOTE: The actual level of the effect is 1 more than the value set here.\r\n")
		                    .translation("the_bumblezone.config.bees.speedboostlevel")
		                    .defineInRange("speedBoostLevel", 1, 0, Integer.MAX_VALUE));
		            
		            
		            absorptionBoostLevel = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How much extra health bees get that always instantly regenerates.\r\n"
		                    		+" This means you need to deal more damage than the extra health gives\r\n"
		                    		+" order to actually damage the bee's real health bar.\r\n"
		                    		+" \r\n"
		                    		+" NOTE: The actual level of the effect is 1 more than the value set here.\r\n"
		                    		+" \r\n"
		                    		+" For example, Absorpton 2 here makes bees get 4 extra padding of hearts.\r\n"
		                    		+" Your attacks need to deal 4 1/2 or more damage to actually be able to\r\n"
		                    		+" kill the bee. This means using Bane of Arthropod 5 is needed to kill bees\r\n"
		                    		+" if you set the absorption to a higher value like 2 or 3.\r\n"
		                    		+" If you set this to like 5 or something, bees may be invicible! Game over.\r\n")
		                    .translation("the_bumblezone.config.bees.absorptionboostlevel")
		                    .defineInRange("absorptionBoostLevel", 1, 0, Integer.MAX_VALUE));
		            
		            
		            strengthBoostLevel = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How strong the bees attacks become. \r\n"
		                    		+" (5 or higher will instant kill you without armor).\r\n"
		                    		+" \r\n"
		                    		+" NOTE: The actual level of the effect is 1 more than the value set here.\r\n")
		                    .translation("the_bumblezone.config.bees.strengthboostlevel")
		                    .defineInRange("strengthBoostLevel", 3, 0, Integer.MAX_VALUE));
	
	            builder.pop();
	        
	        builder.pop();
	        
            builder.push("Mob Spawning Options");

	            phantomSpawnrate = subscriber.subscribe(builder
	                .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	                        +" Controls Phantoms spawnrate in the Bumblezone.\r\n"
	                        +" 1 for max spawnrate and 0 for no spawning. 0.5 is half the rate.")
	                .translation("the_bumblezone.config.mobs.phantomspawnrate")
	                .defineInRange("phantomSpawnrate", 1D, 0, 1D));

	            endermanSpawnrate = subscriber.subscribe(builder
	                .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	                        +" Controls Endermen spawnrate in the Bumblezone.\r\n"
	                        +" 1 for max spawnrate and 0 for no spawning. 0.5 is half the rate.")
	                .translation("the_bumblezone.config.mobs.endermanspawnrate")
	                .defineInRange("endermanSpawnrate", 1D, 0, 1D));

	            spiderSpawnrate = subscriber.subscribe(builder
	                .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	                        +" Controls Spider spawnrate in the Bumblezone.\r\n"
	                        +" 1 for max spawnrate and 0 for no spawning. 0.5 is half the rate.")
	                .translation("the_bumblezone.config.mobs.spiderspawnrate")
	                .defineInRange("spiderSpawnrate", 1D, 0, 1D));
	            
	            caveSpiderSpawnrate = subscriber.subscribe(builder
		                .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                        +" Controls Cave Spider spawnrate in the Bumblezone.\r\n"
		                        +" 1 for max spawnrate and 0 for no spawning. 0.5 is half the rate.")
		                .translation("the_bumblezone.config.mobs.cavespiderspawnrate")
		                .defineInRange("caveSpiderSpawnrate", 1D, 0, 1D));
            builder.pop();
	        
	        builder.push("Mod Compatibility Options");
	
	            builder.push("Buzzier Bees Options");
	            
		            	spawnHoneySlimeMob = subscriber.subscribe(builder
			            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    	+" Spawn Buzzier Bees's Honey Slime mob instead of Vanilla Slime.\r\n")
			            .translation("the_bumblezone.config.modcompat.buzzierbees.spawnhoneyslimemob")
			            .define("spawnHoneySlimeMob", true));
	
		            	allowHoneyWandCompat = subscriber.subscribe(builder
			            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    	+" Allow Honey Wand to take honey from Filled Porous Honeycomb Block \r\n"
			                    	+" and put honey into Porous Honeycomb Block without angering bees.\r\n")
			            .translation("the_bumblezone.config.modcompat.buzzierbees.allowhoneywandcompat")
			            .define("allowHoneyWandCompat", true));
	
		            	allowBottledBeeCompat = subscriber.subscribe(builder
			            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    	+" Allow Bottled Bee item to turn Empty Honeycomb Brood blocks back \r\n"
			                    	+" into Honeycomb Brood Blocks with a larva in it.\r\n")
			            .translation("the_bumblezone.config.modcompat.buzzierbees.allowbottledbeecompat")
			            .define("allowBottledBeeCompat", true));
		            
		            	hivePlanksWorldgen = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Place Hive Planks blocks at the top and bottom of the dimension \r\n"
		                    		+" so it is like the dimension is actually in a Bee Nest block.\r\n")
		                    .translation("the_bumblezone.config.modcompat.buzzierbees.hiveplanksworldgen")
		                    .define("hivePlanksWorldgen", true));
		            
		            	waxBlocksWorldgen = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Place Buzzier Bees's Wax Blocks on the surface of land /r/n"
		                    		+" around sea level and below too.\r\n")
		                    .translation("the_bumblezone.config.modcompat.buzzierbees.waxblocksworldgen")
		                    .define("waxBlocksWorldgen", true));
		            
		            	crystallizedHoneyWorldgen = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Place Buzzier Bees's Crystallized Honey Blocks on the /r/n"
		                    		+" surface of land around sea level and above.\r\n")
		                    .translation("the_bumblezone.config.modcompat.buzzierbees.crystallizedhoneyworldgen")
		                    .define("crystallizedHoneyWorldgen", true));


		    		allowRegularCandlesBeeDungeon = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Allow Bee Dungeons to have normal unscented candles./r/n")
		                    .translation("the_bumblezone.config.modcompat.buzzierbees.allowregularcandlesbeedungeon")
		                    .define("allowRegularCandlesBeeDungeon", true));

		    		allowScentedCandlesBeeDungeon = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Allow Bee Dungeons to have scented candles that gives status effects./r/n")
		                    .translation("the_bumblezone.config.modcompat.buzzierbees.allowscentedcandlesbeedungeon")
		                    .define("allowScentedCandlesBeeDungeon", true));

		    		allowScentedCandlesSpiderBeeDungeon = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Allow Spider Infested Bee Dungeons to have scented candles that gives status effects./r/n")
		                    .translation("the_bumblezone.config.modcompat.buzzierbees.allowscentedcandlesspiderbeedungeon")
		                    .define("allowScentedCandlesSpiderBeeDungeon", true));
		    		
		    		powerfulCandlesRarityBeeDungeon = subscriber.subscribe(builder
	    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	    		            		+" How rare are powerful candles in Bee Dungeons. \r\n"
	    		            		+" Higher numbers means more rare.\r\n"
	    		            		+" Default rate is 2.\r\n")
	    		            .translation("the_bumblezone.config.dungeons.powerfulcandlesraritybeedungeon")
	    		            .defineInRange("powerfulCandlesRarityBeeDungeon", 2, 0, 10));
		    		
		    		powerfulCandlesRaritySpiderBeeDungeon = subscriber.subscribe(builder
	    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	    		            		+" How rare are powerful candles in Spider Infested Bee Dungeons. \r\n"
	    		            		+" Higher numbers means more rare.\r\n"
	    		            		+" Default rate is 2.\r\n")
	    		            .translation("the_bumblezone.config.dungeons.powerfulcandlesrarityspiderbeedungeon")
	    		            .defineInRange("powerfulCandlesRaritySpiderBeeDungeon", 0, 0, 10));
		    		
	            builder.pop();
	            

	            builder.push("Beesourceful Options");
	            
	            		spawnBeesourcefulBeesMob = subscriber.subscribe(builder
			                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    		+" Spawn Beesourceful's ore and ender bees in The Bumblezone alongside\r\n"
			                    		+" regular bees at a 1/15th chance when spawning regular bees.\r\n")
			                    .translation("the_bumblezone.config.modcompat.beesourceful.spawnbeesourcefulbeesmob")
			                    .define("spawnBeesourcefulBeesMob", true));
	            
	            		spawnBeesourcefulHoneycombVariants = subscriber.subscribe(builder
			                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    		+" Spawn Beesourceful's various honeycomb variants in The Bumblezone\r\n"
			                    		+" at all kinds of heights and height bands. Start exploring to find \r\n"
			                    		+" where they spawn! Especially waaaay above for the Ender Honeycomb! \r\n"
			                    		+" \r\n"
			                    		+" NOTE: Will require a restart of the world to take effect. \r\n")
			                    .translation("the_bumblezone.config.modcompat.beesourceful.spawnbeesourcefulhoneycombvariants")
			                    .define("spawnBeesourcefulHoneycombVariants", true));

	        		oreHoneycombSpawnRateBeeDungeon = subscriber.subscribe(builder
	    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	    		            		+" How much of Bee Dungeons is made of ore-based honeycombs.\r\n"
	    		            		+" 0 is no or honeycombs, 1 is max ore honeycombs, and default is 0.3D\r\n")
	    		            .translation("the_bumblezone.config.beesourceful.orehoneycombspawnratebeedungeon")
	    		            .defineInRange("oreHoneycombSpawnRateBeeDungeon", 0.3D, 0D, 1D));
		    		
	        		greatHoneycombRarityBeeDungeon = subscriber.subscribe(builder
	    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	    		            		+" How rare good ore-based Honeycombs (diamonds, ender, emerald, etc) are \r\n"
	    		            		+" in Bee Dungeons. \r\n"
	    		            		+" Higher numbers means more rare. Default rate is 3.\r\n")
	    		            .translation("the_bumblezone.config.beesourceful.greathoneycombraritybeedungeon")
	    		            .defineInRange("greatHoneycombRarityBeeDungeon", 2, 1, 1001));

	        		oreHoneycombSpawnRateSpiderBeeDungeon = subscriber.subscribe(builder
	    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	    		            		+" How much of Spider Infested Bee Dungeons is made of ore-based honeycombs.\r\n"
	    		            		+" 0 is no or honeycombs, 1 is max ore honeycombs, and default is 0.4D\r\n")
	    		            .translation("the_bumblezone.config.beesourceful.orehoneycombspawnratespiderbeedungeon")
	    		            .defineInRange("oreHoneycombSpawnRateSpiderBeeDungeon", 0.4D, 0D, 1D));
		    		
	        		greatHoneycombRaritySpiderBeeDungeon = subscriber.subscribe(builder
	    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	    		            		+" How rare good ore-based Honeycombs (diamonds, ender, emerald, etc) are \r\n"
	    		            		+" in Spider Infested Bee Dungeons. \r\n"
	    		            		+" Higher numbers means more rare. Default rate is 2.\r\n")
	    		            .translation("the_bumblezone.config.beesourceful.greathoneycombrarityspiderbeedungeon")
	    		            .defineInRange("greatHoneycombRaritySpiderBeeDungeon", 2, 1, 1001));
	            builder.pop();
	            
	            builder.push("Productive Bees Options");
	            
	            		spawnProductiveBeesBeesMob = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Spawn Productive Bees in The Bumblezone alongside regular\r\n"
		                    		+" bees at a 1/15th chance when spawning regular bees.\r\n")
		                    .translation("the_bumblezone.config.modcompat.productivebees.spawnproductivebeesbeesmob")
		                    .define("spawnProductiveBeesBeesMob", true));

	            		spawnProductiveBeesHoneycombVariants = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Spawn Productive Bees's various honeycomb variants in The Bumblezone\r\n"
		                    		+" at all kinds of heights and height bands. Start exploring to find \r\n"
		                    		+" where they spawn! Also, due to Beesourceful also having some of the \r\n"
		                    		+" same honeycomb variants, The Bumblezone will pick Beesourceful's \r\n"
		                    		+" honeycombs to spawn instead of Beesourceful's when spawning a \r\n"
		                    		+" honeycomb that's in both mods.\r\n"
		                    		+" \r\n"
		                    		+" NOTE: Will require a restart of the world to take effect. \r\n")
		                    .translation("the_bumblezone.config.modcompat.productivebees.spawnproductivebeeshoneycombvariants")
		                    .define("spawnProductiveBeesHoneycombVariants", true));


	        		PBOreHoneycombSpawnRateBeeDungeon = subscriber.subscribe(builder
	    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	    		            		+" How much of Bee Dungeons is made of ore-based honeycombs.\r\n"
	    		            		+" 0 is no or honeycombs, 1 is max ore honeycombs, and default is 0.3D\r\n"
		                    		+" \r\n"
		                    		+" NOTE: This only takes effect if Productive Bees is on and Beesourceful is not present. \r\n")
	    		            .translation("the_bumblezone.config.productivebees.pborehoneycombspawnratebeedungeon")
	    		            .defineInRange("PBOreHoneycombSpawnRateBeeDungeon", 0.3D, 0D, 1D));
		    		
	        		PBGreatHoneycombRarityBeeDungeon = subscriber.subscribe(builder
	    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	    		            		+" How rare good ore-based Honeycombs (diamonds, ender, emerald, etc) are \r\n"
	    		            		+" in Bee Dungeons. \r\n"
	    		            		+" Higher numbers means more rare. Default rate is 3.\r\n"
		                    		+" \r\n"
		                    		+" NOTE: This only takes effect if Productive Bees is on and Beesourceful is not present. \r\n")
	    		            .translation("the_bumblezone.config.productivebees.pbgreathoneycombraritybeedungeon")
	    		            .defineInRange("PBGreatHoneycombRarityBeeDungeon", 2, 1, 1001));
	        		
	        		PBOreHoneycombSpawnRateSpiderBeeDungeon = subscriber.subscribe(builder
	    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	    		            		+" How much of Spider Infested Bee Dungeons is made of ore-based honeycombs.\r\n"
	    		            		+" 0 is no or honeycombs, 1 is max ore honeycombs, and default is 0.1D\r\n"
		                    		+" \r\n"
		                    		+" NOTE: This only takes effect if Productive Bees is on and Beesourceful is not present. \r\n")
	    		            .translation("the_bumblezone.config.productivebees.pborehoneycombspawnratespiderbeedungeon")
	    		            .defineInRange("PBOreHoneycombSpawnRateSpiderBeeDungeon", 0.1D, 0D, 1D));
		    		
	        		PBGreatHoneycombRaritySpiderBeeDungeon = subscriber.subscribe(builder
	    		            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
	    		            		+" How rare good ore-based Honeycombs (diamonds, ender, emerald, etc) are \r\n"
	    		            		+" in Spider Infested Bee Dungeons. \r\n"
	    		            		+" Higher numbers means more rare. Default rate is 2.\r\n"
		                    		+" \r\n"
		                    		+" NOTE: This only takes effect if Productive Bees is on and Beesourceful is not present. \r\n")
	    		            .translation("the_bumblezone.config.productivebees.pbgreathoneycombrarityspiderbeedungeon")
	    		            .defineInRange("PBGreatHoneycombRaritySpiderBeeDungeon", 2, 1, 1001));
	        		
	            builder.pop();
	            
	            
	            builder.push("Potion of Bees Options");

	            		allowPotionOfBeesCompat = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Allow Potion of Bees item to turn Empty Honeycomb Brood blocks \r\n"
			                    	+" back into Honeycomb Brood Blocks with a larva in it. (affects Despenser too)\r\n")
		                    .translation("the_bumblezone.config.modcompat.potionofbees.allowpotionofbeescompat")
		                    .define("allowPotionOfBeesCompat", true));
	            		
	            		allowSplashPotionOfBeesCompat = subscriber.subscribe(builder
			            .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    	+" Allow Splash Potion of Bees item to turn Empty Honeycomb Brood \r\n"
				                +" blocks back into Honeycomb Brood Blocks with a larva in it when \r\n"
				                +" the potion is thrown and splashed near the block. (affects Despenser too)\r\n")
			            .translation("the_bumblezone.config.modcompat.productivebees.allowsplashpotionofbeescompat")
			            .define("allowSplashPotionOfBeesCompat", true));
        
	            builder.pop();
	            
	        builder.pop();
	            
	        builder.push("General Mechanics Options");

	        	dispensersDropGlassBottles = subscriber.subscribe(builder
		        .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		        	+" Should Dispensers always drop the Glass Bottle when using specific \r\n"
			 	+" bottle items on certain The Bumblezone blocks? \r\n"
			 	+" \r\n"
			 	+" Example: Using Honey Bottle to feed Honeycomb Brood Blocks will grow the \r\n"
		 		+" larva and have a Glass Bottle to either drop or put back into Dispenser. \r\n")
		        .translation("the_bumblezone.config.modcompat.potionofbees.dispensersDropGlassBottles")
		        .define("dispensersDropGlassBottles", false));
	            		
        
	        builder.pop();
	    }
	}
}
