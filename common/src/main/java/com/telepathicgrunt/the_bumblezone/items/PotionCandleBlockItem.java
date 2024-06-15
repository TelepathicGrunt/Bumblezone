package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.PotionCandleBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public class PotionCandleBlockItem extends BlockItem {

    public PotionCandleBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack itemStack, BlockState state) {
        CustomData customData = itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (level.isClientSide() && customData != null && !customData.isEmpty() && level.getBlockEntity(pos) instanceof PotionCandleBlockEntity potionCandleBlockEntity) {
            CompoundTag blockEntityTag = customData.copyTag();

            int color = blockEntityTag.contains(PotionCandleBlockEntity.COLOR_TAG) ? blockEntityTag.getInt(PotionCandleBlockEntity.COLOR_TAG) : PotionCandleBlockEntity.DEFAULT_COLOR;
            potionCandleBlockEntity.setColor(color);

            ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(blockEntityTag.getString(PotionCandleBlockEntity.STATUS_EFFECT_TAG));
            Optional<Holder.Reference<MobEffect>> optionalMobEffectReference = BuiltInRegistries.MOB_EFFECT.getHolder(rl);
            optionalMobEffectReference.ifPresent(potionCandleBlockEntity::setMobEffect);
        }
        return super.updateCustomBlockEntityTag(pos, level, player, itemStack, state);
    }

    @Override
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> components, TooltipFlag tooltipFlag) {
        CustomData customData = itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (customData != null && !customData.isEmpty()) {
            CompoundTag blockEntityTag = customData.copyTag();
            if (blockEntityTag.contains(PotionCandleBlockEntity.STATUS_EFFECT_TAG)) {
                ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(blockEntityTag.getString(PotionCandleBlockEntity.STATUS_EFFECT_TAG));
                Optional<MobEffect> mobEffect = BuiltInRegistries.MOB_EFFECT.getOptional(rl);
                if (mobEffect.isPresent()) {
                    components.add(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.status_effect", mobEffect.get().getDisplayName())));
                    components.add(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.amplifier", blockEntityTag.getInt(PotionCandleBlockEntity.AMPLIFIER_TAG))));
                    components.add(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.range", blockEntityTag.getInt(PotionCandleBlockEntity.RANGE_TAG))));

                    if (blockEntityTag.contains(PotionCandleBlockEntity.INFINITE_TAG) && blockEntityTag.getBoolean(PotionCandleBlockEntity.INFINITE_TAG)) {
                        components.add(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.max_duration", Component.translatable("item.the_bumblezone.potion_candle.infinite"))));
                    }
                    else if (blockEntityTag.contains(PotionCandleBlockEntity.MAX_DURATION_TAG)) {
                        components.add(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.max_duration", formatTime(blockEntityTag.getInt(PotionCandleBlockEntity.MAX_DURATION_TAG)))));
                    }
                    else {
                        components.add(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.max_duration", formatTime(PotionCandleBlockEntity.DEFAULT_MAX_DURATION))));
                    }

                    if (mobEffect.get().isInstantenous()) {
                        components.add(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.apply_interval", formatTime(PotionCandleBlockEntity.getInstantEffectThresholdTime(blockEntityTag.getInt(PotionCandleBlockEntity.AMPLIFIER_TAG))))));
                    }

                    int lingerTime = blockEntityTag.getInt(PotionCandleBlockEntity.LINGER_TIME_TAG);
                    if (lingerTime > 20) {
                        components.add(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.lingering_time", formatTime(lingerTime))));
                    }
                }
            }
        }
    }

    private static MutableComponent formatComponent(MutableComponent component) {
        return component.withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
    }

    private static String formatTime(long duration) {
        long totalSeconds = duration / 20;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
