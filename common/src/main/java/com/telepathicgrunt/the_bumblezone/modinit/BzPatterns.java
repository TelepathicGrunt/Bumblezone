package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BannerPattern;

public class BzPatterns {
    public static final ResourcefulRegistry<BannerPattern> BANNER_PATTERNS = ResourcefulRegistries.create(BuiltInRegistries.BANNER_PATTERN, Bumblezone.MODID);

    public static final RegistryEntry<BannerPattern> BEE_BANNER_PATTERN = BANNER_PATTERNS.register("bee", () -> new BannerPattern("bumblezone_bee"));
    public static final RegistryEntry<BannerPattern> HONEYCOMBS_BANNER_PATTERN = BANNER_PATTERNS.register("honeycombs", () -> new BannerPattern("bumblezone_honeycombs"));
    public static final RegistryEntry<BannerPattern> SWORDS_BANNER_PATTERN = BANNER_PATTERNS.register("swords", () -> new BannerPattern("bumblezone_swords"));
    public static final RegistryEntry<BannerPattern> SUN_BANNER_PATTERN = BANNER_PATTERNS.register("sun", () -> new BannerPattern("bumblezone_sun"));
    public static final RegistryEntry<BannerPattern> PLUSES_BANNER_PATTERN = BANNER_PATTERNS.register("pluses", () -> new BannerPattern("bumblezone_pluses"));
    public static final RegistryEntry<BannerPattern> EYES_BANNER_PATTERN = BANNER_PATTERNS.register("eyes", () -> new BannerPattern("bumblezone_eyes"));
    public static final RegistryEntry<BannerPattern> PEACE_BANNER_PATTERN = BANNER_PATTERNS.register("peace", () -> new BannerPattern("bumblezone_peace"));
    public static final RegistryEntry<BannerPattern> ARROWS_BANNER_PATTERN = BANNER_PATTERNS.register("arrows", () -> new BannerPattern("bumblezone_arrows"));
}
