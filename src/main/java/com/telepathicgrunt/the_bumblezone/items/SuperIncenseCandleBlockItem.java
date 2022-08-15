package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.IncenseCandleBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public class SuperIncenseCandleBlockItem extends BlockItem {

    public SuperIncenseCandleBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack stack, BlockState state) {
        if (level.isClientSide() && stack.hasTag() && level.getBlockEntity(pos) instanceof IncenseCandleBlockEntity incenseCandleBlockEntity) {
            CompoundTag blockEntityTag = stack.getOrCreateTag().getCompound("BlockEntityTag");

            int color = blockEntityTag.contains(IncenseCandleBlockEntity.COLOR_TAG) ? blockEntityTag.getInt(IncenseCandleBlockEntity.COLOR_TAG) : IncenseCandleBlockEntity.DEFAULT_COLOR;
            incenseCandleBlockEntity.setColor(color);

            ResourceLocation rl = new ResourceLocation(blockEntityTag.getString(IncenseCandleBlockEntity.STATUS_EFFECT_TAG));
            Optional<MobEffect> mobEffect = Registry.MOB_EFFECT.getOptional(rl);
            incenseCandleBlockEntity.setMobEffect(mobEffect.orElse(null));
        }
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

    @Override
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (itemStack.hasTag()) {
            CompoundTag blockEntityTag = itemStack.getOrCreateTag().getCompound("BlockEntityTag");
            if (blockEntityTag.contains(IncenseCandleBlockEntity.STATUS_EFFECT_TAG)) {
                ResourceLocation rl = new ResourceLocation(blockEntityTag.getString(IncenseCandleBlockEntity.STATUS_EFFECT_TAG));
                Optional<MobEffect> mobEffect = Registry.MOB_EFFECT.getOptional(rl);
                if (mobEffect.isPresent()) {
                    components.add(formatComponent(Component.translatable("item.the_bumblezone.incense_candle.status_effect", mobEffect.get().getDisplayName())));
                    components.add(formatComponent(Component.translatable("item.the_bumblezone.incense_candle.amplifier", blockEntityTag.getInt(IncenseCandleBlockEntity.AMPLIFIER_TAG))));
                    components.add(formatComponent(Component.translatable("item.the_bumblezone.incense_candle.range", blockEntityTag.getInt(IncenseCandleBlockEntity.RANGE_TAG))));

                    if (blockEntityTag.contains(IncenseCandleBlockEntity.INFINITE_TAG) && blockEntityTag.getBoolean(IncenseCandleBlockEntity.INFINITE_TAG)) {
                        components.add(formatComponent(Component.translatable("item.the_bumblezone.incense_candle.max_duration", Component.translatable("item.the_bumblezone.incense_candle.infinite"))));
                    }
                    else if (blockEntityTag.contains(IncenseCandleBlockEntity.MAX_DURATION_TAG)) {
                        components.add(formatComponent(Component.translatable("item.the_bumblezone.incense_candle.max_duration", formatTime(blockEntityTag.getInt(IncenseCandleBlockEntity.MAX_DURATION_TAG)))));
                    }
                    else {
                        components.add(formatComponent(Component.translatable("item.the_bumblezone.incense_candle.max_duration", formatTime(IncenseCandleBlockEntity.DEFAULT_MAX_DURATION))));
                    }

                    if (mobEffect.get().isInstantenous()) {
                        components.add(formatComponent(Component.translatable("item.the_bumblezone.incense_candle.apply_interval", formatTime(IncenseCandleBlockEntity.getInstantEffectThresholdTime(blockEntityTag.getInt(IncenseCandleBlockEntity.AMPLIFIER_TAG))))));
                    }

                    int lingerTime = blockEntityTag.getInt(IncenseCandleBlockEntity.LINGER_TIME_TAG);
                    if (lingerTime > 20) {
                        components.add(formatComponent(Component.translatable("item.the_bumblezone.incense_candle.lingering_time", formatTime(lingerTime))));
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
