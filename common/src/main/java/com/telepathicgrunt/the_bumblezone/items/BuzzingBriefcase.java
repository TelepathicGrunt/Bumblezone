package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.screens.BuzzingBriefcaseMenuProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class BuzzingBriefcase extends Item {
    public static final String TAG_BEES = "BeesStored";
    public static final int MAX_NUMBER_OF_BEES = 14;

    public BuzzingBriefcase(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            player.openMenu(new BuzzingBriefcaseMenuProvider(stack));

            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        List<Entity> releasedBees = dumpBees(player, player.isCrouching() ? -1 : 0, false);
        return releasedBees.isEmpty();
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            List<Entity> releasedBees = dumpBees(player, player.isCrouching() ? -1 : 0, false);
            for (Entity entity : releasedBees) {
                if (entity instanceof NeutralMob neutralMob && !BeeAggression.isBeelikeEntity(victim)) {
                    neutralMob.setTarget(victim);
                    neutralMob.setRemainingPersistentAngerTime(400); // 20 seconds
                    neutralMob.setPersistentAngerTarget(victim.getUUID());
                }
            }
        }
        return true;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack beeCannon, Player player, LivingEntity entity, InteractionHand playerHand) {
        if (!(entity instanceof Bee bee) || bee.getType().is(BzTags.BUZZING_BRIEFCASE_DISALLOWED_BEE)) {
            return InteractionResult.PASS;
        }

        if (player.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        ItemStack briefcaseItem = player.getItemInHand(playerHand);
        boolean addedBee = tryAddBee(briefcaseItem, entity);
        if (addedBee) {
            player.awardStat(Stats.ITEM_USED.get(briefcaseItem.getItem()));
            player.swing(playerHand, true);
            return InteractionResult.SUCCESS;
        }
        else {
            return InteractionResult.PASS;
        }
    }

    public static List<Entity> dumpBees(Player player, int beeIndex, boolean releaseAtPlayer) {
        Level level = player.level();
        ItemStack briefcaseItem = player.getItemInHand(InteractionHand.MAIN_HAND);

        int numberOfBees = getNumberOfBees(briefcaseItem);
        if (numberOfBees > 0) {
            List<Entity> bees = new ArrayList<>();
            if (beeIndex == -1) {
                bees = getBeesStored(level, briefcaseItem, true);
            }
            else {
                bees.add(getSpecificBeesStored(level, briefcaseItem, beeIndex, true));
            }

            if (!bees.isEmpty()) {
                player.awardStat(Stats.ITEM_USED.get(briefcaseItem.getItem()));

                float maxDistance = 15;
                Vec3 playerEyePos = new Vec3(player.getX(), player.getEyeY() - 0.25f, player.getZ());
                Vec3 maxDistanceDirection = player.getLookAngle().multiply(maxDistance, maxDistance, maxDistance);
                Vec3 finalPos = playerEyePos.add(maxDistanceDirection.normalize());
                if (!releaseAtPlayer) {
                    finalPos = playerEyePos.add(maxDistanceDirection);

                    EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                            level,
                            player,
                            playerEyePos,
                            finalPos,
                            player.getBoundingBox().expandTowards(maxDistanceDirection),
                            entity -> !entity.isInvisibleTo(player) && !BeeAggression.isBeelikeEntity(entity),
                            0.25f);

                    if (entityHitResult != null && entityHitResult.getType() != HitResult.Type.MISS) {
                        finalPos = entityHitResult.getLocation();
                    }
                    else {
                        HitResult hitResult = level.clip(new ClipContext(
                                playerEyePos,
                                finalPos,
                                ClipContext.Block.COLLIDER,
                                ClipContext.Fluid.NONE,
                                player));

                        if (hitResult.getType() != HitResult.Type.MISS) {
                            finalPos = hitResult.getLocation();
                        }
                    }
                }

                Vec3 finalPos1 = finalPos;
                bees.forEach(bee -> {
                    bee.moveTo(finalPos1.x(),
                            finalPos1.y(),
                            finalPos1.z(),
                            player.getYRot(),
                            player.getXRot());
                    level.addFreshEntity(bee);
                });

                level.playSound(null, player.blockPosition(), BzSounds.BUZZING_BRIEFCASE_RELEASES.get(), SoundSource.PLAYERS, 1.0F, (player.getRandom().nextFloat() * 0.2F) + 0.6F);
//                if (player instanceof ServerPlayer serverPlayer) {
//                    BzCriterias.BEE_CANNON_FULL_TRIGGER.trigger(serverPlayer);
//                }
                return new ArrayList<>(bees);
            }
        }
        return new ArrayList<>();
    }

    public static List<Entity> getBeesStored(Level level, ItemStack briefcaseItem, boolean removeFromList) {
        if (getNumberOfBees(briefcaseItem) > 0) {
            CompoundTag briefcaseTag = briefcaseItem.getOrCreateTag();
            ListTag beeList = briefcaseTag.getList(TAG_BEES, ListTag.TAG_COMPOUND);
            List<Entity> beesStored = new ObjectArrayList<>();
            if (removeFromList) {
                for (int i = beeList.size() - 1; i >= 0; i--) {
                    CompoundTag beeTag = beeList.getCompound(0);
                    beeList.remove(0);
                    beesStored.add(EntityType.loadEntityRecursive(beeTag, level, entityx -> entityx));
                    if (beesStored.size() == MAX_NUMBER_OF_BEES) {
                        break;
                    }
                }
            }
            else {
                for (int i = 0 ; i < beeList.size(); i++) {
                    CompoundTag beeTag = beeList.getCompound(i);
                    beesStored.add(EntityType.loadEntityRecursive(beeTag, level, entityx -> entityx));
                    if (beesStored.size() == MAX_NUMBER_OF_BEES) {
                        break;
                    }
                }
            }
            return beesStored;
        }
        return new ObjectArrayList<>();
    }

    public static Entity getSpecificBeesStored(Level level, ItemStack briefcaseItem, int beeIndex, boolean removeFromList) {
        if (getNumberOfBees(briefcaseItem) > 0) {
            CompoundTag briefcaseTag = briefcaseItem.getOrCreateTag();
            ListTag beeList = briefcaseTag.getList(TAG_BEES, ListTag.TAG_COMPOUND);
            if (beeIndex < beeList.size()) {
                CompoundTag beeTag = beeList.getCompound(beeIndex);
                if (removeFromList) {
                    beeList.remove(beeIndex);
                }
                return EntityType.loadEntityRecursive(beeTag, level, entityx -> entityx);
            }
            return null;
        }
        return null;
    }

    public static void overrwriteBees(ItemStack briefcaseItem, List<Entity> bees) {
        CompoundTag briefcaseTag = briefcaseItem.getOrCreateTag();
        briefcaseTag.remove(TAG_BEES);
        bees.forEach(bee -> tryAddBee(briefcaseItem, bee));
    }

    public static boolean tryAddBee(ItemStack briefcaseItem, Entity bee) {
        if (getNumberOfBees(briefcaseItem) < MAX_NUMBER_OF_BEES) {
            CompoundTag briefcaseTag = briefcaseItem.getOrCreateTag();
            ListTag beeList = briefcaseTag.getList(TAG_BEES, ListTag.TAG_COMPOUND);
            CompoundTag beeTag = new CompoundTag();

            bee.save(beeTag);
            bee.stopRiding();
            bee.ejectPassengers();

            beeTag.remove("UUID");
            beeList.add(beeTag);

            bee.discard();
            return true;
        }
        return false;
    }

    public static int getNumberOfBees(ItemStack briefcaseItem) {
        CompoundTag briefcaseTag = briefcaseItem.getOrCreateTag();
        if (briefcaseTag.contains(TAG_BEES)) {
            ListTag beeList = briefcaseTag.getList(TAG_BEES, ListTag.TAG_COMPOUND);
            return beeList.size();
        }
        else {
            ListTag listTag = new ListTag();
            briefcaseTag.put(TAG_BEES, listTag);
        }
        return 0;
    }
}