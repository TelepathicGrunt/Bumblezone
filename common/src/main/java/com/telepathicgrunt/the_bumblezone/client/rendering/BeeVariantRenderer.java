package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.google.common.collect.ImmutableList;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class BeeVariantRenderer {
    private static final List<String> LGBT_VARIANTS = ImmutableList.of("transbee", "asexualbee", "agenderbee", "aroacebee", "aromanticbee", "bisexualbee");
    private static final String UKRAINE_VARIANT = "ukrainebee";

    public static ResourceLocation getTextureLocation(Bee entity) {
        if(BzClientConfigs.enableAltBeeSkinRenderer) {
            UUID id = entity.getUUID();
            long most = id.getMostSignificantBits();

            final double lgbtChance = BzClientConfigs.lgbtBeeRate;
            boolean lgbt = (new Random(most + 1001)).nextDouble() < lgbtChance; // + 1001 so it doesn't align exactly with quark.

            final double ukraineChance = BzClientConfigs.ukraineBeeRate;
            boolean ukraine = (new Random(most + 2005)).nextDouble() < ukraineChance; // + 1001 so it doesn't align exactly with quark.

            if(entity.hasCustomName() || lgbt || ukraine) {
                String custName = entity.hasCustomName() ? entity.getCustomName().getString().trim() : "";
                String name = custName.toLowerCase(Locale.ROOT);

                if(!LGBT_VARIANTS.contains(name) && !UKRAINE_VARIANT.equals(name)) {
                    if(lgbt) name = LGBT_VARIANTS.get(Math.abs((int) (most % (LGBT_VARIANTS.size()))));
                    if(ukraine) name = UKRAINE_VARIANT;
                }

                if(LGBT_VARIANTS.contains(name) || UKRAINE_VARIANT.equals(name)) {
                    String type = "";
                    boolean angery = entity.hasStung();
                    boolean nectar = entity.hasNectar();

                    if(angery)
                        type = nectar ? "_angry_nectar" : "_angry";
                    else if(nectar)
                        type = "_nectar";

                    String path = String.format("textures/entity/bee_variants/%s/bee%s.png", name, type);
                    return new ResourceLocation(Bumblezone.MODID, path);
                }
            }
        }

        return null;
    }
}