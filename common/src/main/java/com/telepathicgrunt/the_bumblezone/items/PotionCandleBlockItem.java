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
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PotionCandleBlockItem extends BlockItem {

    public PotionCandleBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack itemStack, BlockState state) {
        CustomData customData = itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (level.isClientSide() && customData != null && !customData.isEmpty() && level.getBlockEntity(pos) instanceof PotionCandleBlockEntity potionCandleBlockEntity) {
            CompoundTag blockEntityTag = customData.copyTag();

            int color = blockEntityTag.contains(PotionCandleBlockEntity.COLOR_TAG) ? blockEntityTag.getIntOr(PotionCandleBlockEntity.COLOR_TAG, PotionCandleBlockEntity.DEFAULT_COLOR) : PotionCandleBlockEntity.DEFAULT_COLOR;
            potionCandleBlockEntity.setColor(color);

            Identifier rl = Identifier.tryParse(blockEntityTag.getStringOr(PotionCandleBlockEntity.STATUS_EFFECT_TAG, ""));
            Optional<Holder.Reference<MobEffect>> optionalMobEffectReference = BuiltInRegistries.MOB_EFFECT.get(rl);
            optionalMobEffectReference.ifPresent(potionCandleBlockEntity::setMobEffect);
        }
        return super.updateCustomBlockEntityTag(pos, level, player, itemStack, state);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) {
        CustomData customData = itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (customData != null && !customData.isEmpty()) {
            CompoundTag blockEntityTag = customData.copyTag();
            if (blockEntityTag.contains(PotionCandleBlockEntity.STATUS_EFFECT_TAG)) {
                Identifier rl = Identifier.tryParse(blockEntityTag.getString(PotionCandleBlockEntity.STATUS_EFFECT_TAG).orElse(""));
                Optional<MobEffect> mobEffect = BuiltInRegistries.MOB_EFFECT.getOptional(rl);
                if (mobEffect.isPresent()) {
                    componentConsumer.accept(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.status_effect", mobEffect.get().getDisplayName())));
                    componentConsumer.accept(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.amplifier", blockEntityTag.getInt(PotionCandleBlockEntity.EFFECT_LEVEL_TAG))));
                    componentConsumer.accept(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.range", blockEntityTag.getInt(PotionCandleBlockEntity.RANGE_TAG))));

                    if (blockEntityTag.contains(PotionCandleBlockEntity.INFINITE_TAG) && blockEntityTag.getBoolean(PotionCandleBlockEntity.INFINITE_TAG).orElse(false)) {
                        componentConsumer.accept(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.max_duration", Component.translatable("item.the_bumblezone.potion_candle.infinite"))));
                    }
                    else if (blockEntityTag.contains(PotionCandleBlockEntity.MAX_DURATION_TAG)) {
                        componentConsumer.accept(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.max_duration", formatTime(blockEntityTag.getInt(PotionCandleBlockEntity.MAX_DURATION_TAG).orElse(0)))));
                    }
                    else {
                        componentConsumer.accept(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.max_duration", formatTime(PotionCandleBlockEntity.DEFAULT_MAX_DURATION))));
                    }

                    if (mobEffect.get().isInstantenous()) {
                        componentConsumer.accept(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.apply_interval", formatTime(PotionCandleBlockEntity.getInstantEffectThresholdTime(blockEntityTag.getInt(PotionCandleBlockEntity.EFFECT_LEVEL_TAG).orElse(0))))));
                    }

                    int lingerTime = blockEntityTag.getInt(PotionCandleBlockEntity.LINGER_TIME_TAG).orElse(0);
                    if (lingerTime > 20) {
                        componentConsumer.accept(formatComponent(Component.translatable("item.the_bumblezone.potion_candle.lingering_time", formatTime(lingerTime))));
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
