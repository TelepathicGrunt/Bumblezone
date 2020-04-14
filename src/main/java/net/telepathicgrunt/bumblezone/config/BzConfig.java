package net.telepathicgrunt.bumblezone.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BzConfig
{
	/**
	* Config to control all sorts of settings.
	*/
	
	public static final ServerConfig SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;
	static {
	    final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
	    SERVER_SPEC = specPair.getRight();
	    SERVER = specPair.getLeft();
	}
	
	//bee aggression
	public static boolean allowWrathOfTheHiveOutsideBumblezone = false;
	public static boolean showWrathOfTheHiveParticles = true;
	public static boolean aggressiveBees = true;
	public static int aggressionTriggerRadius = 64;
	public static int howLongWrathOfTheHiveLasts = 350;
	public static int speedBoostLevel = 1;
	public static int absorptionBoostLevel = 2;
	public static int strengthBoostLevel = 3;
	
	//dimension
	public static int movementFactor = 10;
	public static boolean dayNightCycle = true;
	public static double fogBrightnessPercentage = 100;
	public static boolean forceExitToOverworld = false;
	
	//mod compatibility
	public static boolean spawnHoneySlimeMob = true;
	public static boolean allowHoneyWandCompat = true;
	public static boolean hivePlanksWorldgen = true;
	public static boolean waxBlocksWorldgen = true;
	public static boolean crystallizedHoneyWorldgen = true;
	public static boolean spawnBeesourcefulBeesMob = true;
	public static boolean spawnBesourcefulHoneycombVariants = true;
	
	public static class ServerConfig
	{
		//bee aggression
	    public final BooleanValue allowWrathOfTheHiveOutsideBumblezone;
	    public final BooleanValue showWrathOfTheHiveParticles;
	    public final BooleanValue aggressiveBees;
	    public final IntValue aggressionTriggerRadius;
	    public final IntValue howLongWrathOfTheHiveLasts;
	    public final IntValue speedBoostLevel;
	    public final IntValue absorptionBoostLevel;
	    public final IntValue strengthBoostLevel;
	    
	    //dimension
	    public final IntValue movementFactor;
	    public final BooleanValue dayNightCycle;
	    public final DoubleValue fogBrightnessPercentage;
	    public final BooleanValue forceExitToOverworld;
	
	
	    //mod compatibility
	    public final BooleanValue spawnHoneySlimeMob;
	    public final BooleanValue allowHoneyWandCompat;
	    public final BooleanValue hivePlanksWorldgen;
	    public final BooleanValue waxBlocksWorldgen;
	    public final BooleanValue crystallizedHoneyWorldgen;
	    public final BooleanValue spawnBeesourcefulBeesMob;
	    public final BooleanValue spawnBesourcefulHoneycombVariants;
	    
	    ServerConfig(ForgeConfigSpec.Builder builder) 
	    {
	
	        
	        builder.push("The Bumblezone Dimension Options");
	
	        
	        movementFactor = builder
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
	                .defineInRange("movementFactor", 10, 1, 1000);
	        
	
	            dayNightCycle = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Determines if the day/night cycle active in the Bumblezone dimension.\r\n "
		                    		+" The cycle will be visible by the change in color of the fog. \r\n"
		                    		+" If kept on, the day/night cycle will match the Overworld's \r\n"
		                    		+" cycle even when players sleep to skip night in the Overworld.\r\n"
		                    		+" \r\n"
		                    		+" If this setting is set to false, the cycle \r\n"
		                    		+" will be stuck at \"noon\" for the dimension.\r\n")
		                    .translation("the_bumblezone.config.dimension.daynightcycle")
		                    .define("dayNightCycle", true);
	
	            
	            fogBrightnessPercentage = builder
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
	                    .defineInRange("fogBrightnessPercentage", 100D, 0D, 100000D);
	
	            
	            forceExitToOverworld = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Makes leaving The Bumblezone dimension always places you back\r\n "
		                    		+" at the Overworld regardless of which dimension you originally \r\n"
		                    		+" came from. Use this option if this dimension becomes locked in  \r\n"
		                    		+" with another dimension so you are stuck teleporting between the \r\n"
		                    		+" two and cannot get back to the Overworld.\r\n")
		                    .translation("the_bumblezone.config.dimension.forceexittooverworld")
		                    .define("forceExitToOverworld", false);
	
	        
	        builder.pop();
	        
	        builder.push("Wrath of the Hive Options");
	
	        	builder.push("Bees Aggression Options");
	        	
		            allowWrathOfTheHiveOutsideBumblezone = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Determines if Wrath of the Hive can be applied to players outside\r\n"
		                    		+" the Bumblezone dimension when they pick up Honey blocks, take honey\r\n"
		                    		+" from Filled Porous Honey blocks, or drink Honey Bottles.\r\n")
		                    .translation("the_bumblezone.config.bees.allowwrathofthehiveoutsidebumblezone")
		                    .define("allowWrathOfTheHiveOutsideBumblezone", false);
		            
		            
		            showWrathOfTheHiveParticles = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Show the orangish particles when you get Wrath of the Hive\r\n"
		                    		+" after you angered the bees in the Bumblezone dimension.\r\n")
		                    .translation("the_bumblezone.config.bees.showwrathofthehiveparticles")
		                    .define("showWrathOfTheHiveParticles", true);
		
		            
		            aggressiveBees = builder
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
		                    .define("aggressiveBees", true);
		
		            
		            aggressionTriggerRadius = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How far away the bee can be to become angry and hunt you down if\r\n "
		                    		+" you take their honey from the Bumblezone dimension.\r\n"
		                    		+" \r\n"
		                    		+" Will also affect the bee's aggression range toward bears in the dimension.\r\n")
		                    .translation("the_bumblezone.config.bees.aggressiontriggerradius")
		                    .defineInRange("aggressionTriggerRadius", 64, 1, 200);
	           
		            
		            howLongWrathOfTheHiveLasts = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How long bees will keep their effects for (speed, absorption, strength).\r\n"
		                    		+" Note: This is not in seconds at all. And bee's anger will remain.\r\n"
		                    		+" Only the boosts given to the bees will be gone.\r\n")
		                    .translation("the_bumblezone.config.bees.howlongwrathofthehivelasts")
		                    .defineInRange("howLongWrathOfTheHiveLasts", 350, 1, Integer.MAX_VALUE);
	
		            
	            builder.pop();
	            
	            builder.push("Bees Effects Options");
		            
		            
		            speedBoostLevel = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How fast bees move along the ground (Not while flying).\r\n"
		                    		+" You will see this a lot when bees are about to attack\r\n"
		                    		+" you, they tend to touch the floor and the speed boost\r\n"
		                    		+" makes them dash forward at you. Set this to higher for\r\n"
		                    		+" faster dash attacks from bees.\r\n")
		                    .translation("the_bumblezone.config.bees.speedboostlevel")
		                    .defineInRange("speedBoostLevel", 1, 1, Integer.MAX_VALUE);
		            
		            
		            absorptionBoostLevel = builder
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
		                    .defineInRange("absorptionBoostLevel", 1, 1, Integer.MAX_VALUE);
		            
		            
		            strengthBoostLevel = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" How strong the bees attacks become. \r\n"
		                    		+" (5 or higher will instant kill you without armor).\r\n")
		                    .translation("the_bumblezone.config.bees.strengthboostlevel")
		                    .defineInRange("strengthBoostLevel", 3, 1, Integer.MAX_VALUE);
	
	            builder.pop();
	        
	        builder.pop();
	        
	        
	        builder.push("Mod Compatibility Options");
	
	            builder.push("Buzzier Bees Options");
	            
		            spawnHoneySlimeMob = builder
			                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    		+" Spawn Buzzier Bees's Honey Slime mob instead of Vanilla Slime.\r\n")
			                    .translation("the_bumblezone.config.modcompat.buzzierbees.spawnhoneyslimemob")
			                    .define("spawnHoneySlimeMob", true);
	
		            allowHoneyWandCompat = builder
			                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    		+" Allow Honey Wand to take honey from Filled Porous Honeycomb Block \r\n"
			                    		+" and put honey into Porous Honeycomb Block without angering bees.\r\n")
			                    .translation("the_bumblezone.config.modcompat.buzzierbees.allowhoneywandcompat")
			                    .define("allowHoneyWandCompat", true);
		            
		            hivePlanksWorldgen = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Place Hive Planks blocks at the top and bottom of the dimension \r\n"
		                    		+" so it is like the dimension is actually in a Bee Nest block.\r\n")
		                    .translation("the_bumblezone.config.modcompat.buzzierbees.hiveplanksworldgen")
		                    .define("hivePlanksWorldgen", true);
		            
		            waxBlocksWorldgen = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Place Buzzier Bees's Wax Blocks on the surface of land /r/n"
		                    		+" around sea level and below too.\r\n")
		                    .translation("the_bumblezone.config.modcompat.buzzierbees.waxblocksworldgen")
		                    .define("waxBlocksWorldgen", true);
		            
		            crystallizedHoneyWorldgen = builder
		                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
		                    		+" Place Buzzier Bees's Crystallized Honey Blocks on the /r/n"
		                    		+" surface of land around sea level and above.\r\n")
		                    .translation("the_bumblezone.config.modcompat.buzzierbees.crystallizedhoneyworldgen")
		                    .define("crystallizedHoneyWorldgen", true);
		            
	            builder.pop();
	            

	            builder.push("Buzzier Bees Options");
	            
	            	spawnBeesourcefulBeesMob = builder
			                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    		+" Spawn Beesourceful's ore and ender bees in The Bumblezone alongside\r\n"
			                    		+" regular bees at a 1/15th chance when spawning regular bees.\r\n")
			                    .translation("the_bumblezone.config.modcompat.beesourceful.spawnbeesourcefulbeesmob")
			                    .define("spawnBeesourcefulBeesMob", true);
	            
	            	spawnBesourcefulHoneycombVariants = builder
			                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    		+" Spawn Beesourceful's various honeycomb variants in The Bumblezone\r\n"
			                    		+" at all kinds of heights and height bands. Start exploring to find \r\n"
			                    		+" where they spawn! Especially waaaay above for the Ender Honeycomb! \r\n")
			                    .translation("the_bumblezone.config.modcompat.beesourceful.spawnbesourcefulhoneycombvariants")
			                    .define("spawnBesourcefulHoneycombVariants", true);

	            builder.pop();
	            
	        builder.pop();
	    }
	        		
	} 
	
	public static void refreshServer()
	{
		//bee aggression
		allowWrathOfTheHiveOutsideBumblezone = SERVER.allowWrathOfTheHiveOutsideBumblezone.get();
		showWrathOfTheHiveParticles = SERVER.showWrathOfTheHiveParticles.get();
		aggressiveBees = SERVER.aggressiveBees.get();
		aggressionTriggerRadius = SERVER.aggressionTriggerRadius.get();
		howLongWrathOfTheHiveLasts = SERVER.howLongWrathOfTheHiveLasts.get();
		speedBoostLevel = SERVER.speedBoostLevel.get();
		absorptionBoostLevel = SERVER.absorptionBoostLevel.get();
		strengthBoostLevel = SERVER.strengthBoostLevel.get();
		
		//dimension
		movementFactor = SERVER.movementFactor.get();
		dayNightCycle = SERVER.dayNightCycle.get();
		fogBrightnessPercentage = SERVER.fogBrightnessPercentage.get();
		forceExitToOverworld = SERVER.forceExitToOverworld.get();
		
	    //mod compatibility
		spawnHoneySlimeMob = SERVER.spawnHoneySlimeMob.get();
		allowHoneyWandCompat = SERVER.allowHoneyWandCompat.get();
		hivePlanksWorldgen = SERVER.hivePlanksWorldgen.get();
		waxBlocksWorldgen = SERVER.waxBlocksWorldgen.get();
		crystallizedHoneyWorldgen = SERVER.crystallizedHoneyWorldgen.get();
		spawnBeesourcefulBeesMob = SERVER.spawnBeesourcefulBeesMob.get();
		spawnBesourcefulHoneycombVariants = SERVER.spawnBesourcefulHoneycombVariants.get();
	}
}
