package com.telepathicgrunt.the_bumblezone.items;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class CrystalCannon extends ProjectileWeaponItem implements Vanishable {
    public static final String TAG_CRYSTALS = "CrystalStored";

    public CrystalCannon(Properties properties) {
        super(properties.durability(80));
    }

    @Override
    public void releaseUsing(ItemStack crystalCannon, Level level, LivingEntity livingEntity, int currentDuration) {
        if (!level.isClientSide() && livingEntity instanceof Player player) {
            ItemStack mutableCrystalCannon = player.getItemInHand(InteractionHand.MAIN_HAND);

            int numberOfCrystals = getNumberOfCrystals(mutableCrystalCannon);
            int remainingDuration = this.getUseDuration(mutableCrystalCannon) - currentDuration;
            if (remainingDuration >= 10 && numberOfCrystals > 0) {
                int crystalsToSpawn = getAndClearStoredCrystals(level, mutableCrystalCannon);
                for (int i = 0; i < crystalsToSpawn; i++) {
                    float offset = 0;
                    if (i == 1) {
                        offset = (level.random.nextFloat() * 5f) + 3.5f;
                    }
                    else if (i == 2) {
                        offset = (level.random.nextFloat() * 5f) - 11.5f;
                    }
                    else if (i != 0) {
                        offset = level.random.nextFloat() * 10f - 5f;
                    }

                    Arrow newCrystal = EntityType.ARROW.create(level); // TODO: custom crystal entity
                    newCrystal.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    Vec3 playerEyePos = new Vec3(
                            player.getX(),
                            player.getEyeY() - 0.25f,
                            player.getZ());
                    newCrystal.moveTo(playerEyePos.x(),
                            playerEyePos.y(),
                            playerEyePos.z(),
                            player.getYRot(),
                            player.getXRot());

                    Vec3 upVector = player.getUpVector(1.0F);
                    Vec3 viewVector = player.getViewVector(1.0F);
                    Quaternion quaternion1 = new Quaternion(new Vector3f(upVector), offset, true);
                    Vector3f shootVector = new Vector3f(viewVector);
                    shootVector.transform(quaternion1);
                    newCrystal.shoot(
                            shootVector.x(),
                            shootVector.y() + (level.random.nextFloat() * 0.2f + 0.01f),
                            shootVector.z(),
                            1.9f,
                            1);
                    level.addFreshEntity(newCrystal);

                    // TODO: custom sound
                    level.playSound(null, player.blockPosition(), BzSounds.BEE_CANNON_FIRES.get(), SoundSource.PLAYERS, 1.0F, (level.getRandom().nextFloat() * 0.2F) + 0.6F);
                    mutableCrystalCannon.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND));

                    if(numberOfCrystals >= 3 && player instanceof ServerPlayer serverPlayer) {
                        BzCriterias.BEE_CANNON_FULL_TRIGGER.trigger(serverPlayer); // TODO: custom crystal cannon advancement
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack beeCannon = player.getItemInHand(interactionHand);
        if (!level.isClientSide()) {
            loadProjectiles(player, beeCannon);
        }

        if (getNumberOfCrystals(beeCannon) == 0) {
            return InteractionResultHolder.fail(beeCannon);
        }
        else {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(beeCannon);
        }
    }

    private void loadProjectiles(Player player, ItemStack beeCannon) {
        ItemStack projectItem1 = player.getProjectile(beeCannon);
        if (projectItem1.isEmpty()) {
            return;
        }

        if (tryAddCrystal(beeCannon)) {
            boolean infinite = player.getAbilities().instabuild ||
                    (projectItem1.getItem() instanceof HoneyCrystalShards &&
                            ((HoneyCrystalShards)projectItem1.getItem()).isInfinite(projectItem1, beeCannon, player));

            if (!infinite) {
                projectItem1.shrink(1);
                if (projectItem1.isEmpty()) {
                    player.getInventory().removeItem(projectItem1);
                }
            }
        }
    }

    public static int getAndClearStoredCrystals(Level level, ItemStack crystalCannonItem) {
        int numberOfCrystals = getNumberOfCrystals(crystalCannonItem);
        if(numberOfCrystals > 0) {
            CompoundTag tag = crystalCannonItem.getOrCreateTag();
            int crystals = tag.getInt(TAG_CRYSTALS);
            tag.putInt(TAG_CRYSTALS, Math.max(0, crystals - 3));
        }
        return numberOfCrystals;
    }

    public static boolean tryAddCrystal(ItemStack crystalCannonItem) {
        if(getNumberOfCrystals(crystalCannonItem) < 3) {
            CompoundTag tag = crystalCannonItem.getOrCreateTag();
            int crystals = tag.getInt(TAG_CRYSTALS);
            tag.putInt(TAG_CRYSTALS, crystals + 1);
            return true;
        }
        return false;
    }

    public static int getNumberOfCrystals(ItemStack crystalCannonItem) {
        if(crystalCannonItem.hasTag()) {
            CompoundTag tag = crystalCannonItem.getOrCreateTag();
            return tag.getInt(TAG_CRYSTALS);
        }
        return 0;
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(BzTags.CRYSTAL_CANNON_REPAIR_ITEMS);
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return (itemStack) -> itemStack.is(BzItems.HONEY_CRYSTAL_SHARDS.get());
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (itemStack) -> itemStack.is(BzItems.HONEY_CRYSTAL_SHARDS.get());
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }
}
