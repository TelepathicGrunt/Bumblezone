package net.telepathicgrunt.bumblezone.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
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

	    public static boolean importModdedFeatures = false;

	    
	    public static class ServerConfig
	    {
	    					
		    public final ForgeConfigSpec.BooleanValue importModdedFeatures;

	        ServerConfig(ForgeConfigSpec.Builder builder) 
	        {

	            builder.push("Mod Compatibility Options");
	            
	            		importModdedFeatures = builder
	                    .comment("\r\n Attempt to add modded features from vanilla biomes into Ultra Amplified version of that biome.\r\n "
	                    		+"Only works if other mod added the feature by addFeature(...) to vanilla biome and registered the feature correctly without the 'minecraft' namespace.")
	                    .translation("ultraamplified.config.compatibility.importmoddedfeatures")
	                    .define("importModdedFeatures", false);
	            		
	            builder.pop();
	        }
	            		
	    } 
	    
	    public static void refreshServer()
	    {
	    	importModdedFeatures = SERVER.importModdedFeatures.get();
	    }
}
