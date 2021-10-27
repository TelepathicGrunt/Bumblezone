package com.telepathicgrunt.the_bumblezone.configs;

import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;

public class BzClientConfigs {

    public static class BzClientConfigsValues {
        public ConfigHelper.ConfigValueListener<Double> lgbtBeeRate;

        public BzClientConfigsValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {

            lgbtBeeRate = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                            " Rate for how often a bee will have an LGBT+ coat!\n")
                    .translation("the_bumblezone.config.beeheemothSpeed")
                    .define("lgbt+ bee rates", 0.01D));

        }
    }
}
