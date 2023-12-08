package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityTickEvent;
import com.telepathicgrunt.the_bumblezone.mixin.effects.MobEffectInstanceAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HoneyBeeLeggings extends BeeArmor {

    public HoneyBeeLeggings(ArmorMaterial material, ArmorItem.Type armorType, Properties properties, int variant) {
        super(material, armorType, properties, variant, false);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(BzTags.BEE_ARMOR_REPAIR_ITEMS);
    }

    // Runs on Forge
    public void onArmorTick(ItemStack itemstack, Level level, Player player) {
        this.bz$onArmorTick(itemstack, level, player);
    }

    @Override
    public void bz$onArmorTick(ItemStack itemstack, Level level, Player player) {
        if (player.isSpectator()) {
            return;
        }

        if (player.getCooldowns().isOnCooldown(itemstack.getItem())) {
            return;
        }

        RandomSource random = player.getRandom();
        boolean isPollinated = isPollinated(itemstack);
        boolean isSprinting = player.isSprinting();
        int beeWearablesCount = BeeArmor.getBeeThemedWearablesCount(player);

        pollenBehavior(itemstack, level, player, random, isPollinated, isSprinting, beeWearablesCount);

        effectBehavior(itemstack, level, player, random, beeWearablesCount);

        spawnParticles(level, player, random, isPollinated, isSprinting, beeWearablesCount);
    }

    private static void pollenBehavior(ItemStack itemstack, Level level, LivingEntity livingEntity, RandomSource random, boolean isPollinated, boolean isSprinting, int beeWearablesCount) {
        if(!level.isClientSide()) {
            boolean ejectPollen = livingEntity.isShiftKeyDown() && isPollinated;
            if (!ejectPollen && isPollinated && level.getGameTime() % 3 == 0) {
                if (livingEntity.getBlockStateOn().is(BzTags.HONEY_BEE_BOOTS_REMOVES_POLLEN_BLOCKS)) {
                    ejectPollen = true;
                    if(livingEntity instanceof ServerPlayer serverPlayer) {
                        BzCriterias.HONEY_BEE_LEGGINGS_POLLEN_REMOVAL_TRIGGER.get().trigger(serverPlayer);
                    }
                }

                if (!ejectPollen) {
                    BlockState state = level.getBlockState(livingEntity.blockPosition());
                    if (state.getFluidState().is(BzTags.HONEY_BEE_BOOTS_REMOVES_POLLEN_FLUIDS)) {
                        ejectPollen = true;
                        if(livingEntity instanceof ServerPlayer serverPlayer) {
                            BzCriterias.HONEY_BEE_LEGGINGS_POLLEN_REMOVAL_TRIGGER.get().trigger(serverPlayer);
                        }
                    }
                }
            }

            if (ejectPollen) {
                removeAndSpawnPollen(level, livingEntity.position(), itemstack);
                if(!level.isClientSide() && random.nextFloat() < 0.1f) {
                    itemstack.hurtAndBreak(1, livingEntity, (playerEntity) -> playerEntity.broadcastBreakEvent(EquipmentSlot.LEGS));
                }
            }
            else if(!livingEntity.isShiftKeyDown() && !isPollinated) {
                BlockState withinBlock = level.getBlockState(livingEntity.blockPosition());
                if(withinBlock.is(BzBlocks.PILE_OF_POLLEN.get())) {
                    setPollinated(itemstack);
                    int newLevel = withinBlock.getValue(PileOfPollen.LAYERS) - 1;
                    if(newLevel == 0) {
                        level.setBlock(livingEntity.blockPosition(), Blocks.AIR.defaultBlockState(), 3);
                    }
                    else {
                        level.setBlock(livingEntity.blockPosition(), withinBlock.setValue(PileOfPollen.LAYERS, newLevel), 3);
                    }
                }
                else if(random.nextFloat() < (((beeWearablesCount - 1) * 0.015f) + (isSprinting ? 0.015f : 0.00333f)) && withinBlock.is(BlockTags.FLOWERS)) {
                    setPollinated(itemstack);
                    if(livingEntity instanceof ServerPlayer serverPlayer) {
                        BzCriterias.HONEY_BEE_LEGGINGS_FLOWER_POLLEN_TRIGGER.get().trigger(serverPlayer);
                        serverPlayer.awardStat(BzStats.HONEY_BEE_LEGGINGS_FLOWER_POLLEN_RL.get());
                    }
                }
            }
        }
    }

    private static void effectBehavior(ItemStack itemstack, Level level, LivingEntity livingEntity, RandomSource random, int beeWearablesCount) {
        MobEffectInstance slowness = livingEntity.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
        if (slowness != null && (beeWearablesCount >= 3 || level.getGameTime() % 2 == 0)) {
            for (int i = 0; i <= Math.max(beeWearablesCount - 2, 1); i++) {
                if (slowness.getDuration() > 0) {
                    ((MobEffectInstanceAccessor) slowness).callTickDownDuration();
                }
            }
            if(!level.isClientSide() &&
                    random.nextFloat() < 0.004f &&
                    itemstack.getMaxDamage() - itemstack.getDamageValue() > 1)
            {
                itemstack.hurtAndBreak(1, livingEntity, (playerEntity) -> playerEntity.broadcastBreakEvent(EquipmentSlot.LEGS));
            }
        }
    }

    private static void spawnParticles(Level world, LivingEntity livingEntity, RandomSource random, boolean isPollinated, boolean isSprinting, int beeWearablesCount) {
        if(world.isClientSide() && isPollinated && (isSprinting || random.nextFloat() < (beeWearablesCount >= 3 ? 0.03f : 0.025f))) {
            int particles = beeWearablesCount >= 3 ? 2 : 1;
            for(int i = 0; i < particles; i++){
                double speedYModifier = isSprinting ? 0.05D : 0.02D;
                double speedXZModifier = isSprinting ? 0.03D : 0.02D;
                double xOffset = (random.nextFloat() * 0.1) - 0.05;
                double yOffset = (random.nextFloat() * 0.1) + 0.25;
                double zOffset = (random.nextFloat() * 0.1) - 0.05;
                Vec3 pos = livingEntity.position();

                world.addParticle(
                        BzParticles.POLLEN_PARTICLE.get(),
                        true,
                        pos.x() + xOffset,
                        pos.y() + yOffset,
                        pos.z() + zOffset,
                        random.nextGaussian() * speedXZModifier,
                        ((random.nextGaussian() + 0.25D) * speedYModifier),
                        random.nextGaussian() * speedXZModifier);
            }
        }
    }

    public static void armorStandTick(EntityTickEvent event) {
        LivingEntity livingEntity = event.entity();
        if (livingEntity instanceof ArmorStand armorStand) {
            ItemStack leggings = armorStand.getItemBySlot(EquipmentSlot.LEGS);

            if (leggings.getItem() instanceof HoneyBeeLeggings) {
                Level level = armorStand.level();
                RandomSource random = armorStand.getRandom();
                boolean isPollinated = isPollinated(leggings);
                boolean isSprinting = armorStand.isSprinting();
                int beeWearablesCount = BeeArmor.getBeeThemedWearablesCount(armorStand);

                pollenBehavior(leggings, level, armorStand, random, isPollinated, isSprinting, beeWearablesCount);
            }
        }
    }

    public static ItemStack getEntityBeeLegging(Entity entity) {
        for(ItemStack armor : entity.getArmorSlots()) {
            if(armor.getItem() instanceof HoneyBeeLeggings) {
                return armor;
            }
        }
        return ItemStack.EMPTY;
    }

    public static void setPollinated(ItemStack itemStack) {
        if (itemStack.getItem() instanceof HoneyBeeLeggings) {
            itemStack.getOrCreateTag().putBoolean("pollinated", true);
        }
    }

    public static void clearPollinated(ItemStack itemStack) {
        if (itemStack.getItem() instanceof HoneyBeeLeggings) {
            itemStack.getOrCreateTag().putBoolean("pollinated", false);
        }
    }

    public static boolean isPollinated(ItemStack itemStack) {
        return itemStack.getItem() instanceof HoneyBeeLeggings &&
                itemStack.hasTag() &&
                itemStack.getTag().getBoolean("pollinated");
    }

    public static void removeAndSpawnPollen(Level world, Vec3 position, ItemStack itemStack) {
        if (itemStack.getItem() instanceof HoneyBeeLeggings) {
            PollenPuff.spawnItemstackEntity(world, position.add(new Vec3(0, 0.25f, 0)), new ItemStack(BzItems.POLLEN_PUFF.get(), 1));
            clearPollinated(itemStack);
        }
    }
}