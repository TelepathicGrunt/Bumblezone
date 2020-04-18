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
		//bee aggression
		public ConfigValueListener<Boolean> allowWrathOfTheHiveOutsideBumblezone;
		public ConfigValueListener<Boolean> showWrathOfTheHiveParticles;
		public ConfigValueListener<Boolean> aggressiveBees;
		public ConfigValueListener<Integer> aggressionTriggerRadius;
		public ConfigValueListener<Integer> howLongWrathOfTheHiveLasts;
		public ConfigValueListener<Integer> speedBoostLevel;
		public ConfigValueListener<Integer> absorptionBoostLevel;
		public ConfigValueListener<Integer> strengthBoostLevel;
		
		//dimension
		public ConfigValueListener<Integer> movementFactor;
		public ConfigValueListener<Boolean> dayNightCycle;
		public ConfigValueListener<Double> fogBrightnessPercentage;
		public ConfigValueListener<Boolean> forceExitToOverworld;
		public ConfigValueListener<String> requiredBlockUnderHive;
		public ConfigValueListener<Boolean> warnPlayersOfWrongBlockUnderHive;
		public ConfigValueListener<Boolean> allowTeleportationWithModdedBeehives;
		
		//mod compatibility
		public ConfigValueListener<Boolean> spawnHoneySlimeMob;
		public ConfigValueListener<Boolean> allowHoneyWandCompat;
		public ConfigValueListener<Boolean> hivePlanksWorldgen;
		public ConfigValueListener<Boolean> waxBlocksWorldgen;
		public ConfigValueListener<Boolean> crystallizedHoneyWorldgen;
		public ConfigValueListener<Boolean> spawnBeesourcefulBeesMob;
		public ConfigValueListener<Boolean> spawnBesourcefulHoneycombVariants;

		public BzConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber)
		{
	        builder.push("The Bumblezone Dimension Options");
	
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
	                		+" take you in the dimension and exiting will place you in a different spot too.\r\n")
	                .translation("the_bumblezone.config.dimension.movementfactor")
	                .defineInRange("movementFactor", 10, 1, 1000));
	        
	
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
		                    .define("allowTeleportationWithModdedBeehives", false));
	            
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
		                    		+" Note: This is not in seconds at all. And bee's anger will remain.\r\n"
		                    		+" Only the boosts given to the bees will be gone.\r\n")
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
		                    		+" faster dash attacks from bees.\r\n")
		                    .translation("the_bumblezone.config.bees.speedboostlevel")
		                    .defineInRange("speedBoostLevel", 1, 1, Integer.MAX_VALUE));
		            
		            
		            absorptionBoostLevel = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How much extra health bees get that always instantly regenerates.\r\n"
		                    		+" This means you need to deal more damage than the extra health gives\r\n"
		                    		+" order to actually damage the bee's real health bar.\r\n"
		                    		+" \r\n"
		                    		+" For example, Absorpton 1 here makes bees get 4 extra padding of hearts.\r\n"
		                    		+" Your attacks need to deal 4 1/2 or more damage to actuall be able to\r\n"
		                    		+" kill the bee. This means using Bane of Arthropod 5 is needed to kill bees\r\n"
		                    		+" if you set the absorption to a higher value like 2 or 3.\r\n"
		                    		+" If you set this to like 5 or something, bees may be invicible! Game over.\r\n")
		                    .translation("the_bumblezone.config.bees.absorptionboostlevel")
		                    .defineInRange("absorptionBoostLevel", 1, 1, Integer.MAX_VALUE));
		            
		            
		            strengthBoostLevel = subscriber.subscribe(builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How strong the bees attacks become. \r\n"
		                    		+" (5 or higher will instant kill you without armor).\r\n")
		                    .translation("the_bumblezone.config.bees.strengthboostlevel")
		                    .defineInRange("strengthBoostLevel", 3, 1, Integer.MAX_VALUE));
	
	            builder.pop();
	        
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
		            
	            builder.pop();
	            

	            builder.push("Buzzier Bees Options");
	            
	            	spawnBeesourcefulBeesMob = subscriber.subscribe(builder
			                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    		+" Spawn Beesourceful's ore and ender bees in The Bumblezone alongside\r\n"
			                    		+" regular bees at a 1/15th chance when spawning regular bees.\r\n")
			                    .translation("the_bumblezone.config.modcompat.beesourceful.spawnbeesourcefulbeesmob")
			                    .define("spawnBeesourcefulBeesMob", true));
	            
	            	spawnBesourcefulHoneycombVariants = subscriber.subscribe(builder
			                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    		+" Spawn Beesourceful's various honeycomb variants in The Bumblezone\r\n"
			                    		+" at all kinds of heights and height bands. Start exploring to find \r\n"
			                    		+" where they spawn! Especially waaaay above for the Ender Honeycomb! \r\n")
			                    .translation("the_bumblezone.config.modcompat.beesourceful.spawnbesourcefulhoneycombvariants")
			                    .define("spawnBesourcefulHoneycombVariants", true));

	            builder.pop();
	            
	        builder.pop();
	    }		
	} 
}
