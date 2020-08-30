package net.telepathicgrunt.bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.utils.ConfigHelper;
import net.telepathicgrunt.bumblezone.utils.ConfigHelper.ConfigValueListener;

@Mod.EventBusSubscriber
public class BzModCompatibilityConfigs
{
	public static class BzModCompatibilityConfigValues
	{
	    public ConfigValueListener<Boolean> allowPotionOfBeesCompat;
	    public ConfigValueListener<Boolean> allowSplashPotionOfBeesCompat;

	    public ConfigValueListener<Boolean> spawnProductiveBeesBeesMob;
	    public ConfigValueListener<Boolean> spawnProductiveBeesHoneycombVariants;
	    public ConfigValueListener<Integer> PBGreatHoneycombRarityBeeDungeon;
	    public ConfigValueListener<Double> PBOreHoneycombSpawnRateBeeDungeon;
	    public ConfigValueListener<Integer> PBGreatHoneycombRaritySpiderBeeDungeon;
	    public ConfigValueListener<Double> PBOreHoneycombSpawnRateSpiderBeeDungeon;

	    public BzModCompatibilityConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {

	        builder.push("Mod Compatibility Options");

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
	    }
	}
}
