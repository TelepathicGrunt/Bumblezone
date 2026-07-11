package com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.rei;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class REIQueenTradesInfo extends BasicDisplay {

	private final TagKey<Item> inputTag;
	private final TagKey<Item> outputTag;
	private final int xpReward;
	private final int weight;
	private final int groupWeight;

	public REIQueenTradesInfo(List<EntryIngredient> inputs, TagKey<Item> inputTag, List<EntryIngredient> outputs, TagKey<Item> outputTag, int xp, int weight, int groupWeight) {
		super(inputs, outputs);
		this.inputTag = inputTag;
		this.outputTag = outputTag;
		this.xpReward = xp;
		this.weight = weight;
		this.groupWeight = groupWeight;
	}

	public int getXpReward() {
		return this.xpReward;
	}

	public int getWeight() {
		return this.weight;
	}

	public int getGroupWeight() {
		return this.groupWeight;
	}

	public TagKey<Item> getInputTag() {
		return this.inputTag;
	}

	public TagKey<Item> getOutputTag() {
		return this.outputTag;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REICompat.QUEEN_TRADES;
	}

	public static final MapCodec<REIQueenTradesInfo> PART_1 = RecordCodecBuilder.mapCodec(instance -> instance.group(
			EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(REIQueenTradesInfo::getInputEntries),
			TagKey.codec(Registries.ITEM).fieldOf("input_tag").forGetter(REIQueenTradesInfo::getInputTag),
			EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(REIQueenTradesInfo::getOutputEntries),
			TagKey.codec(Registries.ITEM).fieldOf("output_tag").forGetter(REIQueenTradesInfo::getOutputTag),
			Codec.INT.fieldOf("xp").forGetter(REIQueenTradesInfo::getXpReward),
			Codec.INT.fieldOf("weight").forGetter(REIQueenTradesInfo::getWeight),
			Codec.INT.fieldOf("group_weight").forGetter(REIQueenTradesInfo::getGroupWeight)
	).apply(instance, REIQueenTradesInfo::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, REIQueenTradesInfo> PART_2 = StreamCodec.composite(
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REIQueenTradesInfo::getInputEntries,
			TagKey.streamCodec(Registries.ITEM),
			REIQueenTradesInfo::getInputTag,
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REIQueenTradesInfo::getOutputEntries,
			TagKey.streamCodec(Registries.ITEM),
			REIQueenTradesInfo::getOutputTag,
			ByteBufCodecs.INT,
			d -> d.xpReward,
			ByteBufCodecs.INT,
			d -> d.weight,
			ByteBufCodecs.INT,
			d -> d.groupWeight,
			REIQueenTradesInfo::new
	);

	public static final DisplaySerializer<REIQueenTradesInfo> SERIALIZER = DisplaySerializer.of(PART_1, PART_2);

    @Override
    public @Nullable DisplaySerializer<REIQueenTradesInfo> getSerializer() {
        return SERIALIZER;
    }
}
