package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.entities.BeeInteractivity;
import com.telepathicgrunt.the_bumblezone.entities.goals.BeehemothAIRide;
import com.telepathicgrunt.the_bumblezone.entities.goals.FlyingStillGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.RandomFlyGoal;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BeehemothEntity extends TameableEntity implements IFlyingAnimal {
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.defineId(BeehemothEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> QUEEN = EntityDataManager.defineId(BeehemothEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> FRIENDSHIP = EntityDataManager.defineId(BeehemothEntity.class, DataSerializers.INT);

    private boolean stopWandering = false;
    public float offset1, offset2, offset3, offset4, offset5, offset6;

    public BeehemothEntity(EntityType<? extends BeehemothEntity> type, World world) {
        super(type, world);
        this.moveControl = new MoveHelperController(this);
        this.offset1 = (this.random.nextFloat() - 0.5f);
        this.offset2 = (this.random.nextFloat() - 0.5f);
        this.offset3 = (this.random.nextFloat() - 0.5f);
        this.offset4 = (this.random.nextFloat() - 0.5f);
        this.offset5 = (this.random.nextFloat() - 0.5f);
        this.offset6 = (this.random.nextFloat() - 0.5f);
    }

    private static final TranslationTextComponent QUEEN_NAME = new TranslationTextComponent("entity.the_bumblezone.beehemoth_queen");

    @Override
    protected ITextComponent getTypeName() {
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

    public boolean isStopWandering() {
        return stopWandering;
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

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("saddled", isSaddled());
        tag.putBoolean("queen", isQueen());
        tag.putInt("friendship", getFriendship());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        setSaddled(tag.getBoolean("saddled"));
        setQueen(tag.contains("queen") && tag.getBoolean("queen"));
        setFriendship(tag.getInt("friendship"));
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
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
            else {
                addFriendship((int) -amount);
            }

            spawnMadParticles();
            setOrderedToSit(false);
            return super.hurt(source, amount);
        }
    }

    public static AttributeModifierMap.MutableAttribute getAttributeBuilder() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 42.0D)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BeehemothAIRide(this));
        this.goalSelector.addGoal(1, new FlyingStillGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.5D, Ingredient.of(BzItemTags.HONEY_BUCKETS), false));
        this.goalSelector.addGoal(3, new RandomFlyGoal(this));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 60));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(5, new SwimGoal(this));
    }

    @Override
    protected PathNavigator createNavigation(World pLevel) {
        return new DirectPathNavigator(this, pLevel);
    }

    // If our flyingSpeed is manually modified by something (like Beenergized effect),
    // calculate the % of change done and use that for speed change.
    // Otherwise, use the flying speed attribute.
    // Have to do this way as flyingSpeed doesn't use the attribute for many mobs so mods may change the field instead of attribute.
    public float getFinalFlyingSpeed() {
        float finalFlyingSpeed = this.flyingSpeed;
        if (finalFlyingSpeed == 0.02f) {
            finalFlyingSpeed = (float) this.getAttributeValue(Attributes.FLYING_SPEED) / 0.6f;
        }
        else {
            finalFlyingSpeed = finalFlyingSpeed / 0.02f;
        }
        return finalFlyingSpeed;
    }

    public static boolean checkMobSpawnRules(EntityType<? extends MobEntity> entityType, IWorld iWorld, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return true;
    }

    @Override
    public boolean checkSpawnRules(IWorld world, SpawnReason spawnReason) {
        return true;
    }

    @Override
    public boolean checkSpawnObstruction(IWorldReader worldReader) {
        AxisAlignedBB box = this.getBoundingBox();
        return !worldReader.containsAnyLiquid(box) && worldReader.getBlockStates(box).noneMatch(state -> state.getMaterial().blocksMotion()) && worldReader.isUnobstructed(this);
    }

    @Override
    public Entity getControllingPassenger() {
        for (Entity p : this.getPassengers()) {
            return p;
        }
        return null;
    }

    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        ResourceLocation itemRL = item.getRegistryName();
        if (this.level.isClientSide) {
            if (this.isTame() && this.isOwnedBy(player)) {
                return ActionResultType.SUCCESS;
            }
            else {
                return !(this.getHealth() < this.getMaxHealth()) && this.isTame() ? ActionResultType.PASS : ActionResultType.SUCCESS;
            }
        }
        else {
            // Healing and befriending Beehemoth
            if (this.isTame()) {
                if (this.isOwnedBy(player)) {
                    if (BzItemTags.BEE_FEEDING_ITEMS.contains(item) && !player.isShiftKeyDown()) {
                        if (item.is(BzItemTags.HONEY_BUCKETS)) {
                            this.heal(this.getMaxHealth() - this.getHealth());
                            BeeInteractivity.calmAndSpawnHearts(this.level, player, this, 0.8f, 5);
                            addFriendship(5);
                        }
                        else if (itemRL.getPath().contains("honey")) {
                            this.addEffect(new EffectInstance(Effects.HEAL, 1, 2, false, false, false));
                            BeeInteractivity.calmAndSpawnHearts(this.level, player, this, 0.3f, 3);
                            addFriendship(3);
                        }
                        else {
                            this.addEffect(new EffectInstance(Effects.HEAL, 1, 1, false, false, false));
                            BeeInteractivity.calmAndSpawnHearts(this.level, player, this, 0.1f, 3);
                            addFriendship(1);
                        }

                        if (!player.isCreative()) {
                            // remove current item
                            stack.shrink(1);
                            GeneralUtils.givePlayerItem(player, hand, new ItemStack(item), true);
                        }

                        player.swing(hand, true);
                        return ActionResultType.CONSUME;
                    }

                    if (item == Items.SADDLE && !isSaddled()) {
                        this.usePlayerItem(player, stack);
                        this.setSaddled(true);
                        return ActionResultType.CONSUME;
                    }

                    if(player.isShiftKeyDown()) {
                        if (isSaddled() && this.isInSittingPose() && stack.isEmpty()) {
                            setSaddled(false);
                            ItemStack saddle = new ItemStack(Items.SADDLE);
                            if (player.addItem(saddle)) {
                                ItemEntity entity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), saddle);
                                player.level.addFreshEntity(entity);
                            }
                        }
                        else {
                            this.setOrderedToSit(!this.isOrderedToSit());
                            this.navigation.stop();
                            this.setTarget(null);
                        }
                        return ActionResultType.SUCCESS;
                    }

                    if (!this.isVehicle() && !player.isSecondaryUseActive()) {
                        if (!this.level.isClientSide) {
                            this.setOrderedToSit(false);
                            player.startRiding(this);
                        }

                        return ActionResultType.sidedSuccess(this.level.isClientSide);
                    }
                }
            }
            // Taming Beehemoth
            else if (BzItemTags.BEE_FEEDING_ITEMS.contains(item)) {
                if(getFriendship() >= 0) {
                    int tameChance;
                    if (item.is(BzItemTags.HONEY_BUCKETS)) {
                        tameChance = 5;
                    }
                    else if (itemRL.getPath().contains("honey")) {
                        tameChance = 10;
                    }
                    else {
                        tameChance = 15;
                    }

                    if (this.random.nextInt(tameChance) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                        this.tame(player);
                        setFriendship(6);
                        this.setOrderedToSit(true);
                        this.level.broadcastEntityEvent(this, (byte) 7);
                    }
                    else {
                        this.level.broadcastEntityEvent(this, (byte) 6);
                    }
                }
                else {
                    addFriendship(1);
                    if (item.is(BzItemTags.HONEY_BUCKETS)) {
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
                    // remove current item
                    stack.shrink(1);
                    GeneralUtils.givePlayerItem(player, hand, new ItemStack(item), true);
                }
                this.setPersistenceRequired();
                player.swing(hand, true);

                if(getFriendship() < 0) {
                    spawnMadParticles();
                }

                return ActionResultType.CONSUME;
            }

            ActionResultType actionresulttype1 = super.mobInteract(player, hand);
            if (actionresulttype1.consumesAction()) {
                this.setPersistenceRequired();
            }

            if(getFriendship() < 0) {
                spawnMadParticles();
            }

            return actionresulttype1;
        }
    }

    private void spawnMadParticles() {
        if (!this.level.isClientSide()) {
            ((ServerWorld) this.level).sendParticles(
                    ParticleTypes.ANGRY_VILLAGER,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    Math.min(Math.max(1, getFriendship() / -3), 7),
                    this.level.getRandom().nextFloat() * 1.0f - 0.5f,
                    this.level.getRandom().nextFloat() * 0.4f + 0.4f,
                    this.level.getRandom().nextFloat() * 1.0f - 0.5f,
                    this.level.getRandom().nextFloat() * 0.8f + 0.4f);
        }
    }

    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float radius = -0.25F;
            float angle = (0.01745329251F * this.yBodyRot);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            passenger.setPos(this.getX() + extraX, this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), this.getZ() + extraZ);

            double currentSpeed = this.getDeltaMovement().length();
            if(currentSpeed > 0.000001D &&
                this.level.random.nextFloat() < 0.0085D &&
                passenger.getUUID().equals(getOwnerUUID()))
            {
                addFriendship(1);
            }
        }
    }

    public double getPassengersRidingOffset() {
        float f = Math.min(0.25F, this.animationSpeed);
        float f1 = this.animationPosition;
        return (double) this.getBbHeight() - 0.2D + (double) (0.12F * MathHelper.cos(f1 * 0.7F) * 0.7F * f);
    }

    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    @Override
    protected void playBlockFallSound() {
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return BzSounds.BEEHEMOTH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BzSounds.BEEHEMOTH_DEATH.get();
    }

    public void tick() {
        super.tick();
        stopWandering = isLeashed();

        // Become queen if friendship is maxed out.
        if(!isQueen() && getFriendship() >= 1000) {
            setQueen(true);
            if(this.getOwner() instanceof ServerPlayerEntity) {
                BzCriterias.QUEEN_BEEHEMOTH_TRIGGER.trigger((ServerPlayerEntity) this.getOwner());
            }
        }
        // Become untamed if bee is no longer a friend
        else if(getFriendship() < 0 && isTame()) {
            ejectPassengers();
            this.setTame(false);
            this.setOwnerUUID(null);
            spawnMadParticles();
        }
    }

    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return null;
    }

    @Override
    public void setLeashedTo(Entity pEntity, boolean pSendAttachNotification) {
        super.setLeashedTo(pEntity, pSendAttachNotification);
        stopWandering = true;
    }

    static class MoveHelperController extends MovementController {
        private final BeehemothEntity beehemothEntity;

        public MoveHelperController(BeehemothEntity beehemothEntity) {
            super(beehemothEntity);
            this.beehemothEntity = beehemothEntity;
        }

        public void tick() {
            if (this.operation == Action.STRAFE) {
                Vector3d vector3d = new Vector3d(this.wantedX - beehemothEntity.getX(), this.wantedY - beehemothEntity.getY(), this.wantedZ - beehemothEntity.getZ());
                double d0 = vector3d.length();
                beehemothEntity.setDeltaMovement(beehemothEntity.getDeltaMovement().add(0, vector3d.scale(this.speedModifier * 0.05D / d0).y(), 0));
                float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float f1 = (float) this.speedModifier * f;
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;

                this.mob.setSpeed(f1);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = MovementController.Action.WAIT;
            }
            else if (this.operation == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - beehemothEntity.getX(), this.wantedY - beehemothEntity.getY(), this.wantedZ - beehemothEntity.getZ());
                double d0 = vector3d.length();
                if (d0 < beehemothEntity.getBoundingBox().getSize()) {
                    this.operation = MovementController.Action.WAIT;
                    beehemothEntity.setDeltaMovement(beehemothEntity.getDeltaMovement().scale(0.5D));
                }
                else {
                    double localSpeed = this.speedModifier;
                    if (beehemothEntity.isVehicle()) {
                        localSpeed *= 1.5D;
                    }
                    beehemothEntity.setDeltaMovement(beehemothEntity.getDeltaMovement().add(vector3d.scale(localSpeed * 0.005D / d0)));
                    if (beehemothEntity.getTarget() == null) {
                        double d2 = this.wantedX - beehemothEntity.getX();
                        double d1 = this.wantedZ - beehemothEntity.getZ();
                        float newRot = (float)(-MathHelper.atan2(d2, d1) * (180F / (float) Math.PI));
                        beehemothEntity.yRot = this.rotlerp(beehemothEntity.yRot, newRot, 10.0F);
                        beehemothEntity.yBodyRot = beehemothEntity.yRot;
                    }
                    else {
                        double d2 = beehemothEntity.getTarget().getX() - beehemothEntity.getX();
                        double d1 = beehemothEntity.getTarget().getZ() - beehemothEntity.getZ();
                        float newRot = (float)(-MathHelper.atan2(d1, d2) * (180F / (float) Math.PI));
                        beehemothEntity.yRot = this.rotlerp(beehemothEntity.yRot, newRot, 10.0F);
                        beehemothEntity.yBodyRot = beehemothEntity.yRot;
                    }
                }

            }
        }
    }

    public boolean isTargetBlocked(Vector3d target) {
        Vector3d Vector3d = new Vector3d(this.getX(), this.getEyeY(), this.getZ());
        return this.level.clip(new RayTraceContext(Vector3d, target, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() != RayTraceResult.Type.MISS;
    }

    public static class DirectPathNavigator extends GroundPathNavigator {

        private final MobEntity mob;

        public DirectPathNavigator(MobEntity mob, World world) {
            super(mob, world);
            this.mob = mob;
        }

        public void tick() {
            ++this.tick;
        }

        public boolean moveTo(double x, double y, double z, double speedIn) {
            mob.getMoveControl().setWantedPosition(x, y, z, speedIn);
            return true;
        }

        public boolean moveTo(Entity entityIn, double speedIn) {
            mob.getMoveControl().setWantedPosition(entityIn.getX(), entityIn.getY(), entityIn.getZ(), speedIn);
            return true;
        }
    }
}
