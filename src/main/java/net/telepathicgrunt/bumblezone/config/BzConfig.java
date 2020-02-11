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
	  /*
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
	    public static boolean dayNightCycle = true;
	    public static double fogBrightnessPercentage = 100;
	    
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
		    public final BooleanValue dayNightCycle;
		    public final DoubleValue fogBrightnessPercentage;

	        ServerConfig(ForgeConfigSpec.Builder builder) 
	        {

	            
	            builder.push("The Bumblezone Dimension Options");
	            

		            dayNightCycle = builder
			                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    		+" Determines if the day/night cycle active in the Bumblezone dimension.\r\n "
			                    		+" The cycle will be visible by the change in color of the fog. \r\n"
			                    		+" If kept on, the day/night cycle will match the Overworld's \r\n"
			                    		+" cycle even when players sleep to skip night in the Overworld.\r\n"
			                    		+" \r\n"
			                    		+" If this setting is set to false, the cycle \r\n"
			                    		+" will be stuck at \"noon\" for the dimension.\r\n")
			                    .translation("the_bumblezone.config.bees.daynightcycle")
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
		                    .translation("the_bumblezone.config.bees.fogbrightnesspercentage")
		                    .defineInRange("fogBrightnessPercentage", 100D, 0D, 100000D);
	            
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
			                    		+" How much extra unrecoverable health boost the bees gets.\r\n")
			                    .translation("the_bumblezone.config.bees.absorptionboostlevel")
			                    .defineInRange("absorptionBoostLevel", 2, 1, Integer.MAX_VALUE);
			            
			            
			            strengthBoostLevel = builder
			                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
			                    		+" How strong the bees attacks become. \r\n"
			                    		+" (5 or higher will instant kill you without armor).\r\n")
			                    .translation("the_bumblezone.config.bees.strengthboostlevel")
			                    .defineInRange("strengthBoostLevel", 3, 1, Integer.MAX_VALUE);

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
	    	dayNightCycle = SERVER.dayNightCycle.get();
	    	fogBrightnessPercentage = SERVER.fogBrightnessPercentage.get();
	    }
}
