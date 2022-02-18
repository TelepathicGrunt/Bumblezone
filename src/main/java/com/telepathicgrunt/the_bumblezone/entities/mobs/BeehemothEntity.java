package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.LivingEntityFlyingSoundInstance;
import com.telepathicgrunt.the_bumblezone.entities.BeeInteractivity;
import com.telepathicgrunt.the_bumblezone.entities.goals.BeehemothAIRide;
import com.telepathicgrunt.the_bumblezone.entities.goals.BeehemothFlyingStillGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.BeehemothRandomFlyGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.BeehemothTemptGoal;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class BeehemothEntity extends TamableAnimal implements FlyingAnimal {

    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(BeehemothEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> QUEEN = SynchedEntityData.defineId(BeehemothEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FRIENDSHIP = SynchedEntityData.defineId(BeehemothEntity.class, EntityDataSerializers.INT);
    private static final TranslatableComponent QUEEN_NAME = new TranslatableComponent("entity.the_bumblezone.beehemoth_queen");
    private static final ResourceLocation MOB_CATCHER_RL = new ResourceLocation("mob_catcher", "mob_catcher");
    public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
    private boolean stopWandering = false;
    public float offset1, offset2, offset3, offset4, offset5, offset6;
    public boolean movingStraightUp = false;
    public boolean movingStraightDown = false;

    public BeehemothEntity(EntityType<? extends BeehemothEntity> type, Level world) {
        super(type, world);
        this.moveControl = new MoveHelperController(this);
        this.offset1 = (this.random.nextFloat() - 0.5f);
        this.offset2 = (this.random.nextFloat() - 0.5f);
        this.offset3 = (this.random.nextFloat() - 0.5f);
        this.offset4 = (this.random.nextFloat() - 0.5f);
        this.offset5 = (this.random.nextFloat() - 0.5f);
        this.offset6 = (this.random.nextFloat() - 0.5f);
    }

    @Override
    protected Component getTypeName() {
        if (isQueen()) {
            return QUEEN_NAME;
        }
        return super.getTypeName();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SADDLED, false);
        this.entityData.define(QUEEN, false);
        this.entityData.define(FRIENDSHIP, 0);
    }

    public static AttributeSupplier.Builder getAttributeBuilder() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 42.0D)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BeehemothAIRide(this));
        this.goalSelector.addGoal(1, new BeehemothTemptGoal(this, 1.5D, Ingredient.of(BzItemTags.HONEY_BUCKETS)));
        this.goalSelector.addGoal(2, new BeehemothFlyingStillGoal(this));
        this.goalSelector.addGoal(3, new BeehemothRandomFlyGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 60));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("saddled", isSaddled());
        tag.putBoolean("queen", isQueen());
        tag.putInt("friendship", getFriendship());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSaddled(tag.getBoolean("saddled"));
        setQueen(tag.contains("queen") && tag.getBoolean("queen"));
        setFriendship(tag.getInt("friendship"));
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new DirectPathNavigator(this, pLevel);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public boolean isQueen() {
        return this.entityData.get(QUEEN);
    }

    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(SADDLED, saddled);
    }

    public void setQueen(boolean queen) {
        this.entityData.set(QUEEN, queen);
    }

    public int getFriendship() {
        return this.entityData.get(FRIENDSHIP);
    }

    public void setFriendship(Integer newFriendship) {
        this.entityData.set(FRIENDSHIP, Math.min(Math.max(newFriendship, -100), 1000));
    }

    public void addFriendship(Integer deltaFriendship) {
        this.entityData.set(FRIENDSHIP, Math.min(Math.max(getFriendship() + deltaFriendship, -100), 1000));
    }

    public boolean isStopWandering() {
        return stopWandering;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        }
        else if(isOnPortalCooldown() && source == DamageSource.IN_WALL) {
            spawnMadParticles();
            playHurtSound(source);
            return false;
        }
        else {
            Entity entity = source.getEntity();

            if (entity != null && entity.getUUID().equals(getOwnerUUID())) {
                addFriendship((int) (-3 * amount));
            }
            if (Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.beehemothTriggersWrath && entity instanceof LivingEntity livingEntity) {
                addFriendship((int) (-amount));

                if (!(livingEntity instanceof Player player && player.isCreative()) &&
                        (livingEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                        Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone) &&
                        !livingEntity.isSpectator() &&
                        Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressiveBees)
                {
                    if(livingEntity.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE)) {
                        livingEntity.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE);
                    }
                    else {
                        //Now all bees nearby in Bumblezone will get VERY angry!!!
                        livingEntity.addEffect(new MobEffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts, 2, false, Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.showWrathOfTheHiveParticles, true));
                    }
                }
            }
            else {
                addFriendship((int) -amount);
            }

            spawnMadParticles();
            setOrderedToSit(false);
            return super.hurt(source, amount);
        }
    }

    // If our flyingSpeed is manually modified by something (like Beenergized effect),
    // calculate the % of change done and use that for speed change.
    // Otherwise, use the flying speed attribute.
    // Have to do this way as flyingSpeed doesn't use the attribute for many mobs so mods may change the field instead of attribute.
    public float getFinalFlyingSpeed() {
        float finalFlyingSpeed = this.flyingSpeed;
        if (finalFlyingSpeed == 0.02f) {
            finalFlyingSpeed = (float) getAttributeValue(Attributes.FLYING_SPEED) / 0.6f;
        }
        else {
            finalFlyingSpeed = finalFlyingSpeed / 0.02f;
        }
        return finalFlyingSpeed;
    }

    public static boolean checkMobSpawnRules(EntityType<? extends Mob> entityType, LevelAccessor iWorld, MobSpawnType spawnReason, BlockPos blockPos, Random random) {
        return true;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, MobSpawnType spawnReason) {
        return true;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldReader) {
        AABB box = getBoundingBox();
        return !worldReader.containsAnyLiquid(box) && worldReader.getBlockStates(box).noneMatch(state -> state.getMaterial().blocksMotion()) && worldReader.isUnobstructed(this);
    }

    @Override
    public Entity getControllingPassenger() {
        for (Entity p : getPassengers()) {
            return p;
        }
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        ResourceLocation itemRL = Registry.ITEM.getKey(item);

        if (this.isTame() && itemRL.equals(MOB_CATCHER_RL)) {
            if(this.isOwnedBy(player)) {
                return InteractionResult.PASS;
            }
            else {
                return InteractionResult.SUCCESS;
            }
        }

        if (this.level.isClientSide) {
            if (isTame() && isOwnedBy(player)) {
                return InteractionResult.SUCCESS;
            } 
            else {
                return !(getHealth() < getMaxHealth()) && isTame() ? InteractionResult.PASS : InteractionResult.SUCCESS;
            }
        }
        else {
            // Healing and befriending Beehemoth
            if (isTame()) {
                if (isOwnedBy(player)) {
                    if (BzItemTags.BEE_FEEDING_ITEMS.contains(item) && !player.isShiftKeyDown()) {
                        if(item == BzItems.BEE_BREAD) {
                            heal(2);
                            BeeInteractivity.calmAndSpawnHearts(this.level, player, this, 0.8f, 5);
                            addFriendship(5);
                            return InteractionResult.PASS;
                        }
                        else if (BzItemTags.HONEY_BUCKETS.contains(item)) {
                            heal(getMaxHealth() - getHealth());
                            BeeInteractivity.calmAndSpawnHearts(this.level, player, this, 0.8f, 5);
                            addFriendship(5);
                        }
                        else if (itemRL.getPath().contains("honey")) {
                            heal(2);
                            BeeInteractivity.calmAndSpawnHearts(this.level, player, this, 0.3f, 3);
                            addFriendship(3);
                        }
                        else {
                            heal(1);
                            BeeInteractivity.calmAndSpawnHearts(this.level, player, this, 0.1f, 3);
                            addFriendship(1);
                        }

                        if (!player.isCreative()) {
                            GeneralUtils.givePlayerItem(player, hand, new ItemStack(item), true, true);
                        }

                        player.swing(hand, true);
                        return InteractionResult.CONSUME;
                    }

                    if (item == Items.SADDLE && !isSaddled()) {
                        usePlayerItem(player, hand, stack);
                        setSaddled(true);
                        return InteractionResult.CONSUME;
                    }

                    if(player.isShiftKeyDown()) {
                        if (isSaddled() && isInSittingPose() && stack.isEmpty()) {
                            setSaddled(false);
                            ItemStack saddle = new ItemStack(Items.SADDLE);
                            if (player.addItem(saddle)) {
                                ItemEntity entity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), saddle);
                                player.level.addFreshEntity(entity);
                            }
                        }
                        else {
                            setOrderedToSit(!isOrderedToSit());
                            this.navigation.stop();
                            setTarget(null);
                        }
                        return InteractionResult.SUCCESS;
                    }

                    if (!isVehicle() && !player.isSecondaryUseActive()) {
                        if (!this.level.isClientSide) {
                            player.startRiding(this);
                            setOrderedToSit(false);
                        }

                        return InteractionResult.sidedSuccess(this.level.isClientSide);
                    }
                }
            }
            // Taming Beehemoth
            else if (BzItemTags.BEE_FEEDING_ITEMS.contains(item)) {
                if(getFriendship() >= 0) {
                    float tameChance;
                    if (BzItemTags.HONEY_BUCKETS.contains(item) || item == BzItems.BEE_BREAD) {
                        tameChance = 0.25f;
                    }
                    else if (itemRL.getPath().contains("honey")) {
                        tameChance = 0.1f;
                    }
                    else {
                        tameChance = 0.067f;
                    }

                    if (this.random.nextFloat() < tameChance) {
                        tame(player);
                        setFriendship(6);
                        setOrderedToSit(true);
                        this.level.broadcastEntityEvent(this, (byte) 7);
                    }
                    else {
                        this.level.broadcastEntityEvent(this, (byte) 6);
                    }
                }
                else {
                    addFriendship(1);
                    if(item == BzItems.BEE_BREAD) {
                        addFriendship(5);
                        return InteractionResult.PASS;
                    }
                    else if (BzItemTags.HONEY_BUCKETS.contains(item)) {
                        addFriendship(3);
                    }
                    else if (itemRL.getPath().contains("honey")) {
                        addFriendship(2);
                    }
                    else {
                        addFriendship(1);
                    }
                }

                if (!player.isCreative()) {
                    GeneralUtils.givePlayerItem(player, hand, new ItemStack(item), true, true);
                }
                setPersistenceRequired();
                player.swing(hand, true);

                if(getFriendship() < 0) {
                    spawnMadParticles();
                }

                return InteractionResult.CONSUME;
            }

            InteractionResult actionresulttype1 = super.mobInteract(player, hand);
            if (actionresulttype1.consumesAction()) {
                setPersistenceRequired();
            }

            if(getFriendship() < 0) {
                spawnMadParticles();
            }

            return actionresulttype1;
        }
    }

    private void spawnMadParticles() {
        if (!this.level.isClientSide()) {
            ((ServerLevel) this.level).sendParticles(
                    ParticleTypes.ANGRY_VILLAGER,
                    getX(),
                    getY(),
                    getZ(),
                    Math.min(Math.max(1, getFriendship() / -3), 7),
                    this.level.getRandom().nextFloat() - 0.5f,
                    this.level.getRandom().nextFloat() * 0.4f + 0.4f,
                    this.level.getRandom().nextFloat() - 0.5f,
                    this.level.getRandom().nextFloat() * 0.8f + 0.4f);
        }
    }

    @Override
    public void positionRider(Entity passenger) {
        if (hasPassenger(passenger)) {
            float radius = -0.25F;
            float angle = (0.01745329251F * this.yBodyRot);
            double extraX = radius * Mth.sin((float) (Math.PI + angle));
            double extraZ = radius * Mth.cos(angle);
            passenger.setPos(getX() + extraX, getY() + getPassengersRidingOffset() + passenger.getMyRidingOffset(), getZ() + extraZ);

            double currentSpeed = getDeltaMovement().length();
            if(currentSpeed > 0.000001D &&
                this.level.random.nextFloat() < 0.0085D &&
                passenger.getUUID().equals(getOwnerUUID()))
            {
                addFriendship(1);
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        float f = Math.min(0.25F, this.animationSpeed);
        float f1 = this.animationPosition;
        return (double) getBbHeight() - 0.2D + (double) (0.12F * Mth.cos(f1 * 0.7F) * 0.7F * f);
    }

    @Override
    protected void removePassenger(Entity entity) {
        super.removePassenger(entity);
        if(entity == getOwner()) {
            setOrderedToSit(true);
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageModifier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void playBlockFallSound() {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BzSounds.BEEHEMOTH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BzSounds.BEEHEMOTH_DEATH;
    }

    @Override
    public void recreateFromPacket(ClientboundAddMobPacket clientboundAddMobPacket) {
        super.recreateFromPacket(clientboundAddMobPacket);
        LivingEntityFlyingSoundInstance.playSound(this, BzSounds.BEEHEMOTH_LOOP);
    }

    @Override
    public void tick() {
        super.tick();
        stopWandering = isLeashed();

        // Become queen if friendship is maxed out.
        if(!isQueen() && getFriendship() >= 1000) {
            setQueen(true);
            if(getOwner() instanceof ServerPlayer) {
                BzCriterias.QUEEN_BEEHEMOTH_TRIGGER.trigger((ServerPlayer) getOwner());
            }
        }
        // Become untamed if bee is no longer a friend
        else if(getFriendship() < 0 && isTame()) {
            ejectPassengers();
            if(level.random.nextFloat() < 0.01f) spawnMadParticles();
        }

        if(isOnGround()) {
            this.setDeltaMovement(
                this.getDeltaMovement().x(),
                this.getDeltaMovement().y() - 0.003D,
                this.getDeltaMovement().z()
            );
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return null;
    }

    @Override
    public void setLeashedTo(Entity entity, boolean sendAttachNotification) {
        super.setLeashedTo(entity, sendAttachNotification);
        stopWandering = true;
    }

    @Override
    public boolean isFlying() {
        return this.tickCount % TICKS_PER_FLAP == 0;
    }

    static class MoveHelperController extends MoveControl {
        private final BeehemothEntity beehemothEntity;

        public MoveHelperController(BeehemothEntity beehemothEntity) {
            super(beehemothEntity);
            this.beehemothEntity = beehemothEntity;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.STRAFE) {
                Vec3 vector3d = new Vec3(this.wantedX - beehemothEntity.getX(), this.wantedY - beehemothEntity.getY(), this.wantedZ - beehemothEntity.getZ());
                double d0 = vector3d.length();
                beehemothEntity.setDeltaMovement(beehemothEntity.getDeltaMovement().add(0, vector3d.scale(this.speedModifier * 0.05D / d0).y(), 0));
                float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float f1 = (float) this.speedModifier * f;
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;

                this.mob.setSpeed(f1);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = MoveControl.Operation.WAIT;
            }
            else if (this.operation == MoveControl.Operation.MOVE_TO) {
                Vec3 vec3 = new Vec3(this.wantedX - beehemothEntity.getX(), this.wantedY - beehemothEntity.getY(), this.wantedZ - beehemothEntity.getZ());
                double d0 = vec3.length();
                if (d0 < beehemothEntity.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    beehemothEntity.setDeltaMovement(beehemothEntity.getDeltaMovement().scale(0.5D));
                }
                else {
                    double localSpeed = this.speedModifier;
                    if (beehemothEntity.isVehicle()) {
                        localSpeed *= 1.5D;
                    }
                    Vec3 newVelocity = beehemothEntity.getDeltaMovement().add(vec3.scale(localSpeed * 0.005D / d0));
                    double newYSpeed = beehemothEntity.isOnGround() && newVelocity.y() + 0.0027D > 0 ? (newVelocity.y() + 0.009D) : newVelocity.y();
                    beehemothEntity.setDeltaMovement(newVelocity.x(), newYSpeed, newVelocity.z());

                    if (beehemothEntity.getTarget() == null) {
                        double d2 = this.wantedX - beehemothEntity.getX();
                        double d1 = this.wantedZ - beehemothEntity.getZ();
                        float newRot = (float)(-Mth.atan2(d2, d1) * (180F / (float) Math.PI));
                        beehemothEntity.setYRot(rotlerp(beehemothEntity.getYRot(), newRot, 10.0F));
                    }
                    else {
                        double d2 = beehemothEntity.getTarget().getX() - beehemothEntity.getX();
                        double d1 = beehemothEntity.getTarget().getZ() - beehemothEntity.getZ();
                        float newRot = (float)(-Mth.atan2(d1, d2) * (180F / (float) Math.PI));
                        beehemothEntity.setYRot(rotlerp(beehemothEntity.getYRot(), newRot, 10.0F));
                    }
                }
            }
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 vec3 = new Vec3(getX(), getEyeY(), getZ());
        return this.level.clip(new ClipContext(vec3, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    public static class DirectPathNavigator extends GroundPathNavigation {

        private final Mob mob;

        public DirectPathNavigator(Mob mob, Level world) {
            super(mob, world);
            this.mob = mob;
        }

        @Override
        public void tick() {
            ++this.tick;
        }

        @Override
        public boolean moveTo(double x, double y, double z, double speedIn) {
            mob.getMoveControl().setWantedPosition(x, y, z, speedIn);
            return true;
        }

        @Override
        public boolean moveTo(Entity entityIn, double speedIn) {
            mob.getMoveControl().setWantedPosition(entityIn.getX(), entityIn.getY(), entityIn.getZ(), speedIn);
            return true;
        }
    }
}
