package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.mixin.enchantments.EnchantmentAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.TriState;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class BeeCannon extends Item implements ItemExtension {
    public static final String TAG_BEES = "BeesStored";
    public static final int MAX_NUMBER_OF_BEES = 3;

    public BeeCannon(Properties properties) {
        super(properties.component(BzDataComponents.BEE_CANNON_DATA.get(), CustomData.EMPTY));
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack itemStack) {
        if (itemStack.get(BzDataComponents.BEE_CANNON_DATA.get()) == null) {
            itemStack.set(BzDataComponents.BEE_CANNON_DATA.get(), CustomData.EMPTY);
        }
    }

    @Override
    public void releaseUsing(ItemStack beeCannon, Level level, LivingEntity livingEntity, int currentDuration) {
        if (!level.isClientSide() && livingEntity instanceof Player player) {
            ItemStack mutableBeeCannon = player.getItemInHand(InteractionHand.MAIN_HAND);

            int numberOfBees = getNumberOfBees(mutableBeeCannon);
            int quickCharge = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, beeCannon);
            int remainingDuration = this.getUseDuration(mutableBeeCannon) - currentDuration;
            if (remainingDuration >= 20 - (quickCharge * 3) && numberOfBees > 0) {
                List<Entity> bees = tryReleaseBees(level, mutableBeeCannon);
                if (bees.isEmpty()) {
                    return;
                }

                player.awardStat(Stats.ITEM_USED.get(beeCannon.getItem()));

                float maxDistance = 15;
                Vec3 playerEyePos = new Vec3(player.getX(), player.getEyeY() - 0.25f, player.getZ());
                Vec3 maxDistanceDirection = player.getLookAngle().multiply(maxDistance, maxDistance, maxDistance);
                Vec3 finalPos = playerEyePos.add(maxDistanceDirection);
                HitResult hitResult = level.clip(new ClipContext(
                        playerEyePos,
                        finalPos,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        player));

                if (hitResult.getType() != HitResult.Type.MISS) {
                    finalPos = hitResult.getLocation();
                }

                EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                        level,
                        player,
                        playerEyePos,
                        finalPos,
                        player.getBoundingBox().expandTowards(maxDistanceDirection),
                        entity -> !entity.isInvisibleTo(player),
                        1);

                bees.forEach(bee -> {
                    bee.moveTo(playerEyePos.x(),
                            playerEyePos.y() - 0.5D,
                            playerEyePos.z(),
                            player.getYRot(),
                            player.getXRot());
                    bee.setDeltaMovement(player.getLookAngle().multiply(2.5d, 2.5d, 2.5d));
                    level.addFreshEntity(bee);

                    if (bee instanceof NeutralMob neutralMob &&
                        entityHitResult != null &&
                        entityHitResult.getType() != HitResult.Type.MISS &&
                        entityHitResult.getEntity() instanceof LivingEntity targetEntity
                        && !(targetEntity instanceof Bee))
                    {
                        neutralMob.setRemainingPersistentAngerTime(60);
                        neutralMob.setPersistentAngerTarget(targetEntity.getUUID());
                        if (bee instanceof Bee trueBee) {
                            trueBee.setTarget(targetEntity);
                        }
                    }

                    mutableBeeCannon.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                });

                level.playSound(null, player.blockPosition(), BzSounds.BEE_CANNON_FIRES.get(), SoundSource.PLAYERS, 1.0F, (player.getRandom().nextFloat() * 0.2F) + 0.6F);
                if (numberOfBees >= MAX_NUMBER_OF_BEES && player instanceof ServerPlayer serverPlayer) {
                    BzCriterias.BEE_CANNON_FULL_TRIGGER.get().trigger(serverPlayer);
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack beeCannon = player.getItemInHand(interactionHand);
        if (getNumberOfBees(beeCannon) == 0) {
            return InteractionResultHolder.fail(beeCannon);
        }
        else {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(beeCannon);
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack beeCannon, Player playerEntity, LivingEntity entity, InteractionHand playerHand) {
        if (playerEntity.level().isClientSide() ||
            !(entity instanceof Bee bee) ||
            bee.isAngry() ||
            bee.getType().is(BzTags.CANNON_BEES_DISALLOWED_BEE) ||
            playerEntity.getCooldowns().isOnCooldown(beeCannon.getItem()))
        {
            return InteractionResult.PASS;
        }

        ItemStack mutableBeeCannon = playerEntity.getItemInHand(playerHand);
        boolean addedBee = tryAddBee(mutableBeeCannon, entity);
        if (addedBee) {
            playerEntity.swing(playerHand, true);
            return InteractionResult.SUCCESS;
        }
        else {
            return InteractionResult.PASS;
        }
    }

    public static List<Entity> tryReleaseBees(Level level, ItemStack beeCannonItem) {
        if (getNumberOfBees(beeCannonItem) > 0 && beeCannonItem.has(BzDataComponents.BEE_CANNON_DATA.get())) {
            CompoundTag cannonTag = beeCannonItem.get(BzDataComponents.BEE_CANNON_DATA.get()).copyTag();
            ListTag beeList = cannonTag.getList(TAG_BEES, ListTag.TAG_COMPOUND);
            List<Entity> releasedBees = new ObjectArrayList<>();
            for (int i = beeList.size() - 1; i >= 0; i--) {
                CompoundTag beeTag = beeList.getCompound(0);
                beeList.remove(0);
                releasedBees.add(EntityType.loadEntityRecursive(beeTag, level, entityx -> entityx));
            }
            beeCannonItem.set(BzDataComponents.BEE_CANNON_DATA.get(), CustomData.of(cannonTag));
            return releasedBees;
        }
        return new ObjectArrayList<>();
    }

    public static boolean tryAddBee(ItemStack beeCannonItem, Entity bee) {
        if (getNumberOfBees(beeCannonItem) < MAX_NUMBER_OF_BEES && beeCannonItem.has(BzDataComponents.BEE_CANNON_DATA.get())) {
            String beeTypeRL = bee.getEncodeId();
            if (beeTypeRL == null) {
                return false;
            }

            CompoundTag cannonTag = beeCannonItem.get(BzDataComponents.BEE_CANNON_DATA.get()).copyTag();
            ListTag beeList = cannonTag.getList(TAG_BEES, ListTag.TAG_COMPOUND);
            CompoundTag beeTag = new CompoundTag();

            bee.stopRiding();
            bee.ejectPassengers();
            beeTag.putString("id", beeTypeRL);
            bee.saveWithoutId(beeTag);

            UUID uUID = bee.getUUID();
            bee.load(beeTag);
            bee.setUUID(uUID);
            beeTag.remove("UUID");
            beeList.add(beeTag);
            cannonTag.put(TAG_BEES, beeList);

            bee.discard();
            beeCannonItem.set(BzDataComponents.BEE_CANNON_DATA.get(), CustomData.of(cannonTag));
            return true;
        }
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    public static int getNumberOfBees(ItemStack beeCannonItem) {
        if (beeCannonItem.has(BzDataComponents.BEE_CANNON_DATA.get())) {
            CompoundTag cannonTag = beeCannonItem.get(BzDataComponents.BEE_CANNON_DATA.get()).copyTag();
            if (cannonTag.contains(TAG_BEES)) {
                ListTag beeList = cannonTag.getList(TAG_BEES, ListTag.TAG_COMPOUND);
                return beeList.size();
            }
            else {
                ListTag listTag = new ListTag();
                cannonTag.put(TAG_BEES, listTag);
            }
        }
        return 0;
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(BzTags.BEE_CANNON_REPAIR_ITEMS);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public TriState bz$canEnchant(ItemStack itemstack, Enchantment enchantment) {
        return ((EnchantmentAccessor)enchantment).getBuiltInRegistryHolder().is(BzTags.ENCHANTABLES_BEE_CANNON_FORCED_ALLOWED) ? TriState.ALLOW : TriState.PASS;
    }
}
