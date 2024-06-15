package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.datacomponents.CrystalCannonData;
import com.telepathicgrunt.the_bumblezone.mixin.enchantments.EnchantmentAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.TriState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

import java.util.function.Predicate;

public class CrystalCannon extends ProjectileWeaponItem implements ItemExtension {

    public CrystalCannon(Properties properties) {
        super(properties.component(BzDataComponents.CRYSTAL_CANNON_DATA.get(), new CrystalCannonData()));
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack itemStack) {
        if (itemStack.get(BzDataComponents.CRYSTAL_CANNON_DATA.get()) == null) {
            itemStack.set(BzDataComponents.CRYSTAL_CANNON_DATA.get(), new CrystalCannonData());
        }
    }

    @Override
    protected void shootProjectile(LivingEntity livingEntity, Projectile projectile, int projectileIndex, float shootingPower, float difficulty, float someMagicNumber, @Nullable LivingEntity livingEntity2) {
        float offset = 0;
        if (projectileIndex == 1) {
            offset = (livingEntity.getRandom().nextFloat() * 5f) + 3.5f;
        }
        else if (projectileIndex == 2) {
            offset = (livingEntity.getRandom().nextFloat() * 5f) - 11.5f;
        }
        else if (projectileIndex != 0) {
            offset = livingEntity.getRandom().nextFloat() * 10f - 5f;
        }

        Vec3 entityEyePos = new Vec3(
                livingEntity.getX(),
                livingEntity.getEyeY() - 0.25f,
                livingEntity.getZ());
        projectile.moveTo(entityEyePos.x(),
                entityEyePos.y(),
                entityEyePos.z(),
                livingEntity.getYRot(),
                livingEntity.getXRot());

        Vec3 upVector = livingEntity.getUpVector(1.0F);
        Vec3 viewVector = livingEntity.getViewVector(1.0F);
        Vector3f shootVector = viewVector.toVector3f();
        if (projectileIndex != 0) {
            Quaternionfc quaternion1 = new Quaternionf(upVector.x(), upVector.y(), upVector.z(), offset);
            shootVector.rotate(quaternion1);
        }

        float weaponProjectileSpeed = 1.9F * shootingPower;
        projectile.shoot(
                shootVector.x(),
                shootVector.y() + (livingEntity.getRandom().nextFloat() * 0.2f + 0.01f),
                shootVector.z(),
                weaponProjectileSpeed,
                1);
    }

    @Override
    public void releaseUsing(ItemStack crystalCannon, Level level, LivingEntity livingEntity, int currentDuration) {
        if (!level.isClientSide()) {
            ItemStack mutableCrystalCannon = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);

            int numberOfCrystals = getNumberOfCrystals(mutableCrystalCannon);
            int quickCharge = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, mutableCrystalCannon);
            int remainingDuration = this.getUseDuration(mutableCrystalCannon, livingEntity) - currentDuration;
            if (remainingDuration >= 20 - (quickCharge * 3) && numberOfCrystals > 0) {
                int crystalsToSpawn = getAndClearStoredCrystals(level, mutableCrystalCannon);
                for (int crystalIndex = 0; crystalIndex < crystalsToSpawn; crystalIndex++) {
                    AbstractArrow newCrystal = BzItems.HONEY_CRYSTAL_SHARDS.get().createArrow(level, crystalCannon, livingEntity, mutableCrystalCannon);

                    double weaponDamage = newCrystal.getBaseDamage();
                    int power = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER, crystalCannon);
                    if (power > 0) {
                        weaponDamage += (power * 0.5D) + 0.5D;
                    }
                    newCrystal.setBaseDamage(weaponDamage);

                    int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH, crystalCannon);
                    if (punch > 0) {
                        newCrystal.setKnockback(newCrystal.getKnockback() + punch);
                    }

                    int pierce = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, crystalCannon);
                    if (pierce > 0) {
                        newCrystal.setPierceLevel((byte) pierce);
                    }

                    newCrystal.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    shootProjectile(livingEntity, newCrystal, crystalIndex, 1, 1.0F, 0, null);
                    level.addFreshEntity(newCrystal);

                    if (livingEntity instanceof Player player) {
                        player.awardStat(Stats.ITEM_USED.get(mutableCrystalCannon.getItem()));
                    }

                    level.playSound(null, livingEntity.blockPosition(), BzSounds.CRYSTAL_CANNON_FIRES.get(), SoundSource.PLAYERS, 1.0F, (livingEntity.getRandom().nextFloat() * 0.2F) + 0.6F);
                    mutableCrystalCannon.hurtAndBreak(1, livingEntity, EquipmentSlot.MAINHAND);

                    if(numberOfCrystals >= 3 && livingEntity instanceof ServerPlayer serverPlayer) {
                        BzCriterias.CRYSTAL_CANNON_FULL_TRIGGER.get().trigger(serverPlayer);
                    }
                }

                // Consume one extra durability
                mutableCrystalCannon.hurtAndBreak(1, livingEntity, EquipmentSlot.MAINHAND);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack crystalCannon = player.getItemInHand(interactionHand);
        if (!level.isClientSide()) {
            loadProjectiles(player, crystalCannon);
        }

        if (getNumberOfCrystals(crystalCannon) == 0) {
            return InteractionResultHolder.fail(crystalCannon);
        }
        else {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(crystalCannon);
        }
    }

    private void loadProjectiles(Player player, ItemStack crystalCannon) {
        ItemStack projectItem1 = player.getProjectile(crystalCannon);
        if (projectItem1.isEmpty()) {
            return;
        }

        if (tryAddCrystal(crystalCannon)) {
            boolean infinite = player.getAbilities().instabuild ||
                    (projectItem1.getItem() instanceof HoneyCrystalShards &&
                            ((HoneyCrystalShards)projectItem1.getItem()).bz$isInfinite(projectItem1, crystalCannon, player).orElseGet(() -> false));

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
        if (numberOfCrystals > 0) {
            crystalCannonItem.set(BzDataComponents.CRYSTAL_CANNON_DATA.get(), new CrystalCannonData(0));
        }
        return numberOfCrystals;
    }

    public static boolean tryAddCrystal(ItemStack crystalCannonItem) {
        if (getNumberOfCrystals(crystalCannonItem) < 3) {
            CrystalCannonData crystalCannonData = crystalCannonItem.get(BzDataComponents.CRYSTAL_CANNON_DATA.get());
            crystalCannonItem.set(BzDataComponents.CRYSTAL_CANNON_DATA.get(), new CrystalCannonData(crystalCannonData.crystalStored() + 1));
            return true;
        }
        return false;
    }

    public static int getNumberOfCrystals(ItemStack crystalCannonItem) {
        CrystalCannonData crystalCannonData = crystalCannonItem.get(BzDataComponents.CRYSTAL_CANNON_DATA.get());
        return crystalCannonData.crystalStored();
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
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public TriState bz$canEnchant(ItemStack itemstack, Enchantment enchantment) {
        return ((EnchantmentAccessor)enchantment).getBuiltInRegistryHolder().is(BzTags.ENCHANTABLES_CRYSTAL_CANNON_FORCED_ALLOWED) ? TriState.ALLOW : TriState.PASS;
    }
}
