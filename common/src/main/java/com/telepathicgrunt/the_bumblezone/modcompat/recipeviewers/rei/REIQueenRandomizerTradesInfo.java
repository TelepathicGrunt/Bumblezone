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

public class REIQueenRandomizerTradesInfo extends BasicDisplay {

	private final TagKey<Item> inOutTag;
	private final int weight;
	private final int groupWeight;

	public REIQueenRandomizerTradesInfo(List<EntryIngredient> inputs, List<EntryIngredient> outputs, TagKey<Item> inOutTag, int weight, int groupWeight) {
		super(inputs, outputs);
		this.inOutTag = inOutTag;
		this.weight = weight;
		this.groupWeight = groupWeight;
	}

	public int getWeight() {
		return this.weight;
	}

	public int getGroupWeight() {
		return this.groupWeight;
	}

	public TagKey<Item> getInOutTag() {
		return this.inOutTag;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REICompat.QUEEN_RANDOMIZE_TRADES;
	}

	public static final MapCodec<REIQueenRandomizerTradesInfo> PART_1 = RecordCodecBuilder.mapCodec(instance -> instance.group(
			EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(REIQueenRandomizerTradesInfo::getInputEntries),
			EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(REIQueenRandomizerTradesInfo::getOutputEntries),
			TagKey.codec(Registries.ITEM).fieldOf("inout_tag").forGetter(REIQueenRandomizerTradesInfo::getInOutTag),
			Codec.INT.fieldOf("weight").forGetter(REIQueenRandomizerTradesInfo::getWeight),
			Codec.INT.fieldOf("group_weight").forGetter(REIQueenRandomizerTradesInfo::getGroupWeight)
	).apply(instance, REIQueenRandomizerTradesInfo::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, REIQueenRandomizerTradesInfo> PART_2 = StreamCodec.composite(
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REIQueenRandomizerTradesInfo::getInputEntries,
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REIQueenRandomizerTradesInfo::getOutputEntries,
			TagKey.streamCodec(Registries.ITEM),
			REIQueenRandomizerTradesInfo::getInOutTag,
			ByteBufCodecs.INT,
			d -> d.weight,
			ByteBufCodecs.INT,
			d -> d.groupWeight,
			REIQueenRandomizerTradesInfo::new
	);

	public static final DisplaySerializer<REIQueenRandomizerTradesInfo> SERIALIZER = DisplaySerializer.of(PART_1, PART_2);

	@Override
	public @Nullable DisplaySerializer<REIQueenRandomizerTradesInfo> getSerializer() {
		return SERIALIZER;
	}
}