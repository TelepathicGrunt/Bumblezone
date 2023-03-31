package com.telepathicgrunt.the_bumblezone.world.features.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ItemFrameConfig implements FeatureConfiguration {
    public static final Codec<ItemFrameConfig> CODEC = RecordCodecBuilder.create((configInstance) -> configInstance.group(
            RegistryCodecs.homogeneousList(Registries.ITEM, BuiltInRegistries.ITEM.byNameCodec()).fieldOf("items_to_pick_from").forGetter(config -> config.itemToPickFrom),
            Attachment.CODEC.fieldOf("attachment").forGetter(config -> config.attachment)
    ).apply(configInstance, ItemFrameConfig::new));

    public final HolderSet<Item> itemToPickFrom;
    public final Attachment attachment;

    public ItemFrameConfig(HolderSet<Item> itemToPickFrom,
                           Attachment attachment)
    {
        this.itemToPickFrom = itemToPickFrom;
        this.attachment = attachment;
    }

    public enum Attachment implements StringRepresentable {
        FLOOR("floor"),
        CEILING("ceiling"),
        WALL("wall");

        public static final StringRepresentable.EnumCodec<Attachment> CODEC = StringRepresentable.fromEnum(Attachment::values);

        private final String name;

        Attachment(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
