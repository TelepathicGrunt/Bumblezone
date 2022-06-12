package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;


public class CarpenterBeeBoots extends BeeArmor {

    public CarpenterBeeBoots(ArmorMaterial material, EquipmentSlot slot, Properties properties, int variant) {
        super(material, slot, properties, variant, false);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(BzTags.BEE_ARMOR_REPAIR_ITEMS);
    }

    @Override
    public void onArmorTick(ItemStack beeBoots, Level world, Player player) {
        RandomSource random = world.random;
        boolean isAllBeeArmorOn = StinglessBeeHelmet.isAllBeeArmorOn(player);
        CompoundTag tag = beeBoots.getOrCreateTag();

        if(!world.isClientSide()) {
            int itemId = generateUniqueItemId(world, random, tag, tag.getInt("itemstackId"));
            int lastSentState = tag.getInt("lastSentState");

            if (player.isCrouching()) {
                BlockPos belowBlockPos = new BlockPos(player.position().add(0, -0.1d, 0));
                BlockState belowBlockState = world.getBlockState(belowBlockPos);

                if (belowBlockState.is(BzTags.CARPENTER_BEE_BOOTS_MINEABLES)) {
                    int miningStartTime = tag.getInt("miningStartTime");
                    if (miningStartTime == 0) {
                        miningStartTime = (int) world.getGameTime();

                        if (!world.isClientSide()) {
                            tag.putInt("miningStartTime", miningStartTime);
                        }
                    }

                    int timeDiff = ((int) (world.getGameTime()) - miningStartTime);
                    float miningProgress = (float) (timeDiff + 1);

                    float blockDestroyTime = belowBlockState.getDestroySpeed(world, belowBlockPos);
                    float playerMiningSpeed = getPlayerDestroySpeed(player, beeBoots, isAllBeeArmorOn ? 0.5f : 0.3F);
                    int finalMiningProgress = (int) ((miningProgress * playerMiningSpeed) / blockDestroyTime);

                    if (!(finalMiningProgress == 0 && playerMiningSpeed < 0.001f) && (finalMiningProgress != lastSentState)) {
                        world.destroyBlockProgress(itemId, belowBlockPos, finalMiningProgress);
                        tag.putInt("lastSentState", finalMiningProgress);
                    }

                    if (finalMiningProgress >= 10) {
                        world.destroyBlockProgress(itemId, belowBlockPos, -1);
                        BlockEntity blockEntity = belowBlockState.hasBlockEntity() ? world.getBlockEntity(belowBlockPos) : null;
                        belowBlockState.getBlock().playerDestroy(
                                world,
                                player,
                                belowBlockPos,
                                belowBlockState,
                                blockEntity,
                                beeBoots);
                        boolean blockBroken = world.destroyBlock(belowBlockPos, false, player);

                        if(world.random.nextFloat() < 0.045) {
                            beeBoots.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(EquipmentSlot.FEET));
                        }

                        if(blockBroken && player instanceof ServerPlayer serverPlayer) {
                            serverPlayer.awardStat(BzStats.CARPENTER_BEE_BOOTS_MINED_BLOCKS_RL);

                            if(serverPlayer.getStats().getValue(Stats.CUSTOM.get(BzStats.CARPENTER_BEE_BOOTS_MINED_BLOCKS_RL, StatFormatter.DEFAULT)) >= 200) {
                                BzCriterias.CARPENTER_BEE_BOOTS_MINED_BLOCKS_TRIGGER.trigger(serverPlayer);
                            }
                        }

                        tag.putInt("lastSentState", -1);
                        tag.putInt("miningStartTime", 0);
                    }
                }
                else {
                    world.destroyBlockProgress(itemId, belowBlockPos, -1);
                    tag.putInt("lastSentState", -1);
                    tag.putInt("miningStartTime", 0);
                }
            }
            else if (lastSentState != -1) {
                BlockPos belowBlockPos = new BlockPos(player.position().add(0, -0.1d, 0));
                world.destroyBlockProgress(itemId, belowBlockPos, -1);
                tag.putInt("lastSentState", -1);
                tag.putInt("miningStartTime", 0);
            }
        }

        int hangTime = tag.getInt("hangTime");
        if(!world.isClientSide() && hangTime > 0 && player.isOnGround()) {
            hangTime = 0;
            tag.putInt("hangTime", hangTime);
        }

        double playerDeltaY = player.getDeltaMovement().y();
        int hangCooldownTimer = tag.getInt("hangCooldownTimer");
        int maxHangTime = isAllBeeArmorOn ? 100 : 35;
        if (!player.getAbilities().flying &&
            !player.isPassenger() &&
            !player.onClimbable() &&
            !player.isOnGround() &&
            !player.isInWater() &&
            !player.isCrouching() &&
            playerDeltaY <= 0 &&
            playerDeltaY >= -1.2f &&
            hangCooldownTimer <= 0)
        {
            for (float xOffset = -0.45f; xOffset <= 0.45f; xOffset += 0.45f) {
                for (float zOffset = -0.45f; zOffset <= 0.45f; zOffset += 0.45f) {
                    if(xOffset != 0 && zOffset != 0) {
                        BlockPos posToCheck = new BlockPos(player.position().add(xOffset, 0.057f, zOffset));
                        BlockState sideBlockState = world.getBlockState(posToCheck);
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

                            if(!world.isClientSide()) {
                                if(hangTime >= maxHangTime) {
                                    tag.putInt("hangCooldownTimer", 10);
                                    tag.putInt("hangTime", 0);
                                }
                                else {
                                    tag.putInt("hangTime", hangTime + 1);

                                    if(player instanceof ServerPlayer serverPlayer) {
                                        serverPlayer.awardStat(BzStats.CARPENTER_BEE_BOOTS_WALL_HANG_TIME_RL);

                                        if(serverPlayer.getStats().getValue(Stats.CUSTOM.get(BzStats.CARPENTER_BEE_BOOTS_WALL_HANG_TIME_RL, StatFormatter.DEFAULT)) >= 4000) {
                                            BzCriterias.CARPENTER_BEE_BOOTS_WALL_HANGING_TRIGGER.trigger(serverPlayer);
                                        }
                                    }
                                }
                            }

                            if(world.random.nextFloat() < 0.001) {
                                beeBoots.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(EquipmentSlot.FEET));
                            }
                        }
                    }
                }
            }
        }
        else if(!world.isClientSide() && hangCooldownTimer > 0) {
            int newCoolDown = hangCooldownTimer - 1;
            tag.putInt("hangCooldownTimer", newCoolDown);

        }
    }

    public static float getPlayerDestroySpeed(Player player, ItemStack beeBoots, float currentSpeed) {
        int efficencyLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, beeBoots);
        if (efficencyLevel > 0) {
            currentSpeed += efficencyLevel / 4f;
        }

        if (MobEffectUtil.hasDigSpeed(player)) {
            currentSpeed *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }

        if (player.hasEffect(BzEffects.BEENERGIZED)) {
            currentSpeed *= 1.0F + ((float)(player.getEffect(BzEffects.BEENERGIZED).getAmplifier() + 1) );
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

        if (player.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(player)) {
            currentSpeed /= 5.0F;
        }

        if (!player.isOnGround()) {
            currentSpeed /= 5.0F;
        }

        return currentSpeed;
    }

    private static int generateUniqueItemId(Level world, RandomSource random, CompoundTag tag, int itemId) {
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
            tag.putInt("itemstackId", itemId);
        }
        return itemId;
    }

    public static ItemStack getEntityBeeBoots(Entity entity) {
        for(ItemStack armor : entity.getArmorSlots()) {
            if(armor.getItem() instanceof CarpenterBeeBoots) {
                return armor;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean canBeEnchanted(ItemStack stack, Enchantment enchantment) {
        return (stack.getItem() == BzItems.CARPENTER_BEE_BOOTS_1 || stack.getItem() == BzItems.CARPENTER_BEE_BOOTS_2) &&
                (enchantment.category == EnchantmentCategory.DIGGER);
    }
}