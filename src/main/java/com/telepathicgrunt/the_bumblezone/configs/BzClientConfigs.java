package com.telepathicgrunt.the_bumblezone.configs;

import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;

public class BzClientConfigs {

    public static class BzClientConfigsValues {
        public ConfigHelper.ConfigValueListener<Double> lgbtBeeRate;
        public ConfigHelper.ConfigValueListener<Boolean> enableLgbtBeeRenderer;

        public BzClientConfigsValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {

            lgbtBeeRate = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                            " Rate for how often a bee will have an LGBT+ coat!\n")
                    .translation("the_bumblezone.config.lgbtBeeRate")
                    .define("lgbt+ bee rates", 0.01D));

            enableLgbtBeeRenderer = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                            " Enable replacing the bee renderer for LGBT+ skins.",
                            " Set this to false if the render is messing with other mod's bee renderers.\n")
                    .translation("the_bumblezone.config.enableLgbtBeeRenderer")
                    .define("Enable lgbt+ bee renderer", false));
        }
    }
}
