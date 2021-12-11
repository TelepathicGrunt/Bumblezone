package com.telepathicgrunt.the_bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class BzModCompatibilityConfigs {
	public static final ForgeConfigSpec GENERAL_SPEC;

	public static ForgeConfigSpec.BooleanValue allowHoneyFluidTanksFeedingCompat;

	static {
		ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
		setupConfig(configBuilder);
		GENERAL_SPEC = configBuilder.build();
	}

	private static void setupConfig(ForgeConfigSpec.Builder builder) {
		builder.push("Mod Compatibility Options");
				builder.push("General Compat Options");

				allowHoneyFluidTanksFeedingCompat = builder
								.comment(" \n-----------------------------------------------------\n\n"
										+" Will let you feed any item that has a Forge fluid capability attached and has ",
										"  any fluid that is tagged forge:fluid/honey inside. This works alongside the bee_feeding item tag.",
										"  An item can still be fed even if bee_feeding tag doesn't have the item as long as the ",
										" item fit the above conditions with the fluid capability and this config is set to true.\n")
								.translation("the_bumblezone.config.allowhoneyfluidtanksfeedingcompat")
								.define("allowHoneyFluidTanksFeedingCompat", true);

				builder.pop();

		builder.pop();
	}
}
