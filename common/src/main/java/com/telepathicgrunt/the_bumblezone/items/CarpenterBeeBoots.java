package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.datacomponents.CarpenterBeeBootsHangingData;
import com.telepathicgrunt.the_bumblezone.datacomponents.CarpenterBeeBootsMiningData;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CarpenterBeeBoots extends BeeArmor implements ItemExtension {

    private static final int HANGING_COOLDOWN_IN_TICKS = 10;

    public CarpenterBeeBoots(Holder<ArmorMaterial> material, ArmorItem.Type armorType, Properties properties, int variant) {
        super(material,
            armorType,
            properties.component(BzDataComponents.CARPENTER_BEE_BOOTS_MINING_DATA.get(), new CarpenterBeeBootsMiningData())
                    .component(BzDataComponents.CARPENTER_BEE_BOOTS_HANGING_DATA.get(), new CarpenterBeeBootsHangingData()),
            variant,
            false);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack itemStack) {
        if (itemStack.get(BzDataComponents.CARPENTER_BEE_BOOTS_MINING_DATA.get()) == null) {
            itemStack.set(BzDataComponents.CARPENTER_BEE_BOOTS_MINING_DATA.get(), new CarpenterBeeBootsMiningData());
        }
        if (itemStack.get(BzDataComponents.CARPENTER_BEE_BOOTS_HANGING_DATA.get()) == null) {
            itemStack.set(BzDataComponents.CARPENTER_BEE_BOOTS_HANGING_DATA.get(), new CarpenterBeeBootsHangingData());
        }
    }

    @Override
    public void bz$onArmorTick(ItemStack itemStack, Level level, Player player) {
        if (player.isSpectator()) {
            return;
        }

        if (player.getCooldowns().isOnCooldown(itemStack.getItem())) {
            return;
        }

        RandomSource random = player.getRandom();
        int beeWearablesCount = BeeArmor.getBeeThemedWearablesCount(player);

        if (!level.isClientSide()) {
            CarpenterBeeBootsMiningData carpenterBeeBootsMiningData = itemStack.get(BzDataComponents.CARPENTER_BEE_BOOTS_MINING_DATA.get());
            int itemId = generateUniqueItemId(level, random, carpenterBeeBootsMiningData.itemStackId());
            int lastSentState = carpenterBeeBootsMiningData.lastSentState();
            int miningStartTime = carpenterBeeBootsMiningData.miningStartTime();

            double xInBlock = Math.abs(player.position().x()) % 1;
            double zInBlock = Math.abs(player.position().z()) % 1;
            if (player.isShiftKeyDown() &&
                player.getLookAngle().y() < -0.9d &&
                xInBlock > 0.2d &&
                xInBlock < 0.8d &&
                zInBlock > 0.2d &&
                zInBlock < 0.8d &&
                GeneralUtils.isPermissionAllowedAtSpot(level, player, player.blockPosition().below(), false))
            {
                BlockPos belowBlockPos = BlockPos.containing(player.position().add(0, -0.1d, 0));
                BlockState belowBlockState = level.getBlockState(belowBlockPos);

                if (belowBlockState.is(BzTags.CARPENTER_BEE_BOOTS_MINEABLES)) {
                    if (miningStartTime == 0) {
                        miningStartTime = (int) level.getGameTime();
                    }

                    int timeDiff = ((int) (level.getGameTime()) - miningStartTime);
                    float miningProgress = (float) (timeDiff + 1);

                    float blockDestroyTime = belowBlockState.getDestroySpeed(level, belowBlockPos);
                    float playerMiningSpeed = getPlayerDestroySpeed(player, ((beeWearablesCount - 1) * 0.1F) + 0.3F);
                    int finalMiningProgress = (int) ((miningProgress * playerMiningSpeed) / blockDestroyTime);

                    if (!(finalMiningProgress == 0 && playerMiningSpeed < 0.001f) && (finalMiningProgress != lastSentState)) {
                        level.destroyBlockProgress(itemId, belowBlockPos, finalMiningProgress);
                        lastSentState = finalMiningProgress;
                    }

                    if (finalMiningProgress >= 10) {
                        level.destroyBlockProgress(itemId, belowBlockPos, -1);

                        // Post the block break event
                        BlockState state = level.getBlockState(belowBlockPos);
                        BlockEntity entity = level.getBlockEntity(belowBlockPos);

                        // Handle if the event is canceled
                        if (PlatformHooks.sendBlockBreakEvent(level, belowBlockPos, state, entity, player)) {
                            return;
                        }

                        boolean blockBroken = level.destroyBlock(belowBlockPos, false, player);

                        if (blockBroken) {
                            BlockEntity blockEntity = belowBlockState.hasBlockEntity() ? level.getBlockEntity(belowBlockPos) : null;

                            belowBlockState.getBlock().playerDestroy(
                                    level,
                                    player,
                                    belowBlockPos,
                                    belowBlockState,
                                    blockEntity,
                                    itemStack);

                            if(random.nextFloat() < 0.045) {
                                itemStack.hurtAndBreak(1, player, EquipmentSlot.FEET);
                            }

                            if(player instanceof ServerPlayer serverPlayer) {
                                serverPlayer.awardStat(BzStats.CARPENTER_BEE_BOOTS_MINED_BLOCKS_RL.get());

                                if(serverPlayer.getStats().getValue(Stats.CUSTOM.get(BzStats.CARPENTER_BEE_BOOTS_MINED_BLOCKS_RL.get(), StatFormatter.DEFAULT)) >= 64) {
                                    BzCriterias.CARPENTER_BEE_BOOTS_MINED_BLOCKS_TRIGGER.get().trigger(serverPlayer);
                                }
                            }
                            PlatformHooks.afterBlockBreakEvent(level, belowBlockPos, state, entity, player);
                        }

                        lastSentState = -1;
                        miningStartTime = 0;
                    }
                }
                else {
                    level.destroyBlockProgress(itemId, belowBlockPos, -1);
                    lastSentState = -1;
                    miningStartTime = 0;
                }
            }
            else if (lastSentState != -1) {
                BlockPos belowBlockPos = BlockPos.containing(player.position().add(0, -0.1d, 0));
                level.destroyBlockProgress(itemId, belowBlockPos, -1);
                lastSentState = -1;
                miningStartTime = 0;
            }

            if (carpenterBeeBootsMiningData.isDifferent(itemId, lastSentState, miningStartTime)) {
                itemStack.set(BzDataComponents.CARPENTER_BEE_BOOTS_MINING_DATA.get(),
                    new CarpenterBeeBootsMiningData(
                        itemId,
                        lastSentState,
                        miningStartTime
                    ));
            }
        }

        CarpenterBeeBootsHangingData carpenterBeeBootsHangingData = itemStack.get(BzDataComponents.CARPENTER_BEE_BOOTS_HANGING_DATA.get());
        long hangStartTime = carpenterBeeBootsHangingData.hangStartTime();
        long hangCooldownStartTime = carpenterBeeBootsHangingData.hangCooldownStartTime();
        if(!level.isClientSide() && hangStartTime != -1 && player.onGround()) {
            hangStartTime = -1;
        }

        double playerDeltaY = player.getDeltaMovement().y();
        int maxHangTime = ((beeWearablesCount - 1) * 22) + 35;
        if (!player.getAbilities().flying &&
            !player.isPassenger() &&
            !player.onClimbable() &&
            !player.onGround() &&
            !player.isInWater() &&
            !player.isShiftKeyDown() &&
            playerDeltaY <= 0 &&
            playerDeltaY >= -1.2f &&
            hangCooldownStartTime == -1)
        {
            for (float xOffset = -0.45f; xOffset <= 0.45f; xOffset += 0.45f) {
                for (float zOffset = -0.45f; zOffset <= 0.45f; zOffset += 0.45f) {
                    if(xOffset != 0 && zOffset != 0) {
                        BlockPos posToCheck = BlockPos.containing(player.position().add(xOffset, 0.057f, zOffset));
                        BlockState sideBlockState = level.getBlockState(posToCheck);
                        if (sideBlockState.is(BzTags.CARPENTER_BEE_BOOTS_CLIMBABLES)) {
                            double newDeltaY = Math.min(playerDeltaY * 0.9d + 0.07d, -0.0055);
                            player.setDeltaMovement(new Vec3(
                                player.getDeltaMovement().x(),
                                newDeltaY,
                                player.getDeltaMovement().z()
                            ));

                            if (newDeltaY >= -0.0135f) {
                                player.setJumping(false);
                                player.setOnGround(true);
                            }

                            if (newDeltaY >= -0.4f) {
                                player.fallDistance = 0;
                            }

                            if (!level.isClientSide()) {
                                if (hangStartTime == -1) {
                                    hangStartTime = level.getGameTime();
                                }

                                if ((level.getGameTime() - hangStartTime) >= maxHangTime) {
                                    hangCooldownStartTime = HANGING_COOLDOWN_IN_TICKS;
                                    hangStartTime = 0;
                                }
                                else if(player instanceof ServerPlayer serverPlayer) {
                                    serverPlayer.awardStat(BzStats.CARPENTER_BEE_BOOTS_WALL_HANG_TIME_RL.get());

                                    if(serverPlayer.getStats().getValue(Stats.CUSTOM.get(BzStats.CARPENTER_BEE_BOOTS_WALL_HANG_TIME_RL.get(), StatFormatter.DEFAULT)) >= 4000) {
                                        BzCriterias.CARPENTER_BEE_BOOTS_WALL_HANGING_TRIGGER.get().trigger(serverPlayer);
                                    }
                                }
                            }

                            if(random.nextFloat() < 0.001) {
                                itemStack.hurtAndBreak(1, player, EquipmentSlot.FEET);
                            }
                        }
                    }
                }
            }
        }
        else if (!level.isClientSide() &&
                hangCooldownStartTime != - 1 &&
                hangCooldownStartTime + HANGING_COOLDOWN_IN_TICKS > level.getGameTime())
        {
            hangCooldownStartTime = -1;
        }

        if (carpenterBeeBootsHangingData.isDifferent(hangStartTime, hangCooldownStartTime)) {
            itemStack.set(BzDataComponents.CARPENTER_BEE_BOOTS_HANGING_DATA.get(),
                new CarpenterBeeBootsHangingData(
                    hangStartTime,
                    hangCooldownStartTime
                ));
        }
    }

    public static float getPlayerDestroySpeed(Player player, float currentSpeed) {
        if (currentSpeed > 1.0F) {
            currentSpeed += (float)player.getAttributeValue(Attributes.MINING_EFFICIENCY);
        }

        if (MobEffectUtil.hasDigSpeed(player)) {
            currentSpeed *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }

        if (player.hasEffect(BzEffects.BEENERGIZED.holder())) {
            currentSpeed *= 1.0F + ((float)(player.getEffect(BzEffects.BEENERGIZED.holder()).getAmplifier() + 1));
        }

        if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float miningDecrease = switch (player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };

            currentSpeed *= miningDecrease;
        }

        currentSpeed *= (float)player.getAttributeValue(Attributes.BLOCK_BREAK_SPEED);
        if (player.isEyeInFluid(FluidTags.WATER)) {
            currentSpeed *= (float)player.getAttribute(Attributes.SUBMERGED_MINING_SPEED).getValue();
        }

        if (!player.onGround()) {
            currentSpeed /= 5.0F;
        }

        return currentSpeed;
    }

    private static int generateUniqueItemId(Level world, RandomSource random, int itemId) {
        if (itemId == 0 && !world.isClientSide()) {
            while (true) {
                boolean anymatch = false;
                itemId = random.nextInt(Integer.MAX_VALUE);
                for (Player worldPlayer : world.players()) {
                    if (worldPlayer.getId() == itemId) {
                        anymatch = true;
                        break;
                    }
                }
                if (!anymatch) {
                    break;
                }
            }
        }
        return itemId;
    }

    public static ItemStack getEntityBeeBoots(LivingEntity entity) {
        for(ItemStack armor : entity.getArmorSlots()) {
            if(armor.getItem() instanceof CarpenterBeeBoots) {
                return armor;
            }
        }
        return ItemStack.EMPTY;
    }
}