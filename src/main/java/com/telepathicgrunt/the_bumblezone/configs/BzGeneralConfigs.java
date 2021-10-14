package com.telepathicgrunt.the_bumblezone.configs;

import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;

public class BzGeneralConfigs {

    public static class BzGeneralConfigsValues {
        public ConfigHelper.ConfigValueListener<Double> beeheemothSpeed;

        public BzGeneralConfigsValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {

            builder.push("Beehemoth Options");

            beeheemothSpeed = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                            " Base speed for the Beehemoth when being rode by a player.\n")
                    .translation("the_bumblezone.config.beeheemothSpeed")
                    .define("beeheemothSpeed", 0.95D));

            builder.pop();
        }
    }
}
