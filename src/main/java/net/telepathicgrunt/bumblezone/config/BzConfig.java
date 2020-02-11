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

	    public static boolean aggressiveBees = true;

	    
	    public static class ServerConfig
	    {
		    public final ForgeConfigSpec.BooleanValue aggressiveBees;

	        ServerConfig(ForgeConfigSpec.Builder builder) 
	        {

	            builder.push("Bees Options");
	            
	            aggressiveBees = builder
	                    .comment("\r\n Determines whether bees become angry if you take honey from\r\n "
	                    		+" Filled Porous Honeycomb Blocks or pick up Honey blocks inside the Bumblezone dimension.\r\n"
	                    		+" Note: Peaceful mode will always override the bee aggressive setting.")
	                    .translation("the_bumblezone.config.bees.aggressivebees")
	                    .define("aggressiveBees", false);
	            		
	            builder.pop();
	        }
	            		
	    } 
	    
	    public static void refreshServer()
	    {
	    	aggressiveBees = SERVER.aggressiveBees.get();
	    }
}
