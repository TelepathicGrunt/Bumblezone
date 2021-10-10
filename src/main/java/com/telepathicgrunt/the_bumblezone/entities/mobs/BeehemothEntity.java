package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.entities.goals.BeehemothAIRide;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.EnumSet;

public class BeehemothEntity extends TameableEntity implements IFlyingAnimal {
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.defineId(BeehemothEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> QUEEN = EntityDataManager.defineId(BeehemothEntity.class, DataSerializers.BOOLEAN);

    private boolean stopWandering = false;
    private boolean hasItemTarget = false;

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

    private static TranslationTextComponent QUEEN_NAME = new TranslationTextComponent("entity.carrierbees.beehemoth_queen");

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

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("saddled", isSaddled());
        tag.putBoolean("queen", isQueen());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        setSaddled(tag.getBoolean("saddled"));
        setQueen(tag.contains("queen") && tag.getBoolean("queen"));
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source == DamageSource.OUT_OF_WORLD || source.getEntity() instanceof PlayerEntity) {
            return super.hurt(source, amount);
        }

        return false;
    }

    public static AttributeModifierMap.MutableAttribute getAttributeBuilder() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 42.0D).add(Attributes.FLYING_SPEED, 0.6).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.FOLLOW_RANGE, 128.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BeehemothAIRide(this, 3.2D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 3.2d, Ingredient.of(Items.SUGAR), false));
        this.goalSelector.addGoal(4, new RandomFlyGoal(this));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 10));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(9, new SwimGoal(this));
    }

    @Override
    protected PathNavigator createNavigation(World pLevel) {
        return new DirectPathNavigator(this, pLevel);
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
        if (this.level.isClientSide) {
            if (this.isTame() && this.isOwnedBy(player)) {
                return ActionResultType.SUCCESS;
            } else {
                return !(this.getHealth() < this.getMaxHealth()) && this.isTame() ? ActionResultType.PASS : ActionResultType.SUCCESS;
            }
        } else {
            if (this.isTame()) {
                if (this.isOwnedBy(player)) {
//                    if (item == ModItems.ROYAL_JELLY.get() && !isQueen()) {
//                        this.usePlayerItem(player, stack);
//                        setQueen(true);
//                        return ActionResultType.CONSUME;
//                    }

                    if (item == Items.SUGAR && this.getHealth() < this.getMaxHealth()) {
                        this.usePlayerItem(player, stack);
                        this.heal(10);
                        return ActionResultType.CONSUME;
                    }

                    if (item == Items.SADDLE && !isSaddled()) {
                        this.usePlayerItem(player, stack);
                        this.setSaddled(true);
                        return ActionResultType.CONSUME;
                    }

                    if (stack.isEmpty() && isSaddled() && player.isShiftKeyDown()) {
                        setSaddled(false);
                        ItemStack saddle = new ItemStack(Items.SADDLE);
                        if (player.addItem(saddle)) {
                            ItemEntity entity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), saddle);
                            player.level.addFreshEntity(entity);
                        }
                    }

                    if (stack.isEmpty() && !this.isVehicle() && !player.isSecondaryUseActive()) {
                        if (!this.level.isClientSide) {
                            player.startRiding(this);
                        }

                        return ActionResultType.sidedSuccess(this.level.isClientSide);
                    }
                }
            } else if (item == Items.SUGAR) {
                this.usePlayerItem(player, stack);
                if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.setOrderedToSit(true);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }

                this.setPersistenceRequired();
                return ActionResultType.CONSUME;
            }

            ActionResultType actionresulttype1 = super.mobInteract(player, hand);
            if (actionresulttype1.consumesAction()) {
                this.setPersistenceRequired();
            }

            return actionresulttype1;
        }
    }

    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float radius = -0.25F;
            float angle = (0.01745329251F * this.yBodyRot);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            passenger.setPos(this.getX() + extraX, this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), this.getZ() + extraZ);
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
    }

    private BlockPos getGroundPosition(BlockPos radialPos) {
        while (radialPos.getY() > 1 && level.isEmptyBlock(radialPos)) {
            radialPos = radialPos.below();
        }
        if (radialPos.getY() <= 1) {
            return new BlockPos(radialPos.getX(), level.getSeaLevel(), radialPos.getZ());
        }
        return radialPos;
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
        private final BeehemothEntity parentEntity;

        public MoveHelperController(BeehemothEntity sunbird) {
            super(sunbird);
            this.parentEntity = sunbird;
        }

        public void tick() {
            if (this.operation == Action.STRAFE) {
                Vector3d vector3d = new Vector3d(this.wantedX - parentEntity.getX(), this.wantedY - parentEntity.getY(), this.wantedZ - parentEntity.getZ());
                double d0 = vector3d.length();
                parentEntity.setDeltaMovement(parentEntity.getDeltaMovement().add(0, vector3d.scale(this.speedModifier * 0.05D / d0).y(), 0));
                float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float f1 = (float) this.speedModifier * f;
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;

                this.mob.setSpeed(f1);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = MovementController.Action.WAIT;
            } else if (this.operation == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - parentEntity.getX(), this.wantedY - parentEntity.getY(), this.wantedZ - parentEntity.getZ());
                double d0 = vector3d.length();
                if (d0 < parentEntity.getBoundingBox().getSize()) {
                    this.operation = MovementController.Action.WAIT;
                    parentEntity.setDeltaMovement(parentEntity.getDeltaMovement().scale(0.5D));
                } else {
                    double localSpeed = this.speedModifier;
                    if (parentEntity.isVehicle()) {
                        localSpeed *= 1.5D;
                    }
                    parentEntity.setDeltaMovement(parentEntity.getDeltaMovement().add(vector3d.scale(localSpeed * 0.005D / d0)));
                    if (parentEntity.getTarget() == null) {
                        Vector3d vector3d1 = parentEntity.getDeltaMovement();
                        parentEntity.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                        parentEntity.yBodyRot = parentEntity.yRot;
                    } else {
                        double d2 = parentEntity.getTarget().getX() - parentEntity.getX();
                        double d1 = parentEntity.getTarget().getZ() - parentEntity.getZ();
                        parentEntity.yRot = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
                        parentEntity.yBodyRot = parentEntity.yRot;
                    }
                }

            }
        }
    }

    public boolean isTargetBlocked(Vector3d target) {
        Vector3d Vector3d = new Vector3d(this.getX(), this.getEyeY(), this.getZ());
        return this.level.clip(new RayTraceContext(Vector3d, target, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() != RayTraceResult.Type.MISS;
    }

    public class DirectPathNavigator extends GroundPathNavigator {

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


    static class RandomFlyGoal extends Goal {
        private final BeehemothEntity parentEntity;
        private BlockPos target = null;

        public RandomFlyGoal(BeehemothEntity mosquito) {
            this.parentEntity = mosquito;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            MovementController movementcontroller = this.parentEntity.getMoveControl();
            if (parentEntity.stopWandering || parentEntity.hasItemTarget) {
                return false;
            }
            if (!movementcontroller.hasWanted() || target == null) {
                target = getBlockInViewBeehemoth();
                if (target != null) {
                    this.parentEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 1.0D);
                }
                return true;
            }
            return false;
        }

        public boolean canContinueToUse() {
            return target != null && !parentEntity.stopWandering && !parentEntity.hasItemTarget && parentEntity.distanceToSqr(Vector3d.atCenterOf(target)) > 2.4D && parentEntity.getMoveControl().hasWanted() && !parentEntity.horizontalCollision;
        }

        public void stop() {
            target = null;
        }

        public void tick() {
            if (target == null) {
                target = getBlockInViewBeehemoth();
            }
            if (target != null) {
                this.parentEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 1.0D);
                if (parentEntity.distanceToSqr(Vector3d.atCenterOf(target)) < 2.5F) {
                    target = null;
                }
            }
        }

        public BlockPos getBlockInViewBeehemoth() {
            float radius = 1 + parentEntity.getRandom().nextInt(5);
            float neg = parentEntity.getRandom().nextBoolean() ? 1 : -1;
            float renderYawOffset = parentEntity.yBodyRot;
            float angle = (0.01745329251F * renderYawOffset) + 3.15F + (parentEntity.getRandom().nextFloat() * neg);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            BlockPos radialPos = new BlockPos(parentEntity.getX() + extraX, parentEntity.getY() + 2, parentEntity.getZ() + extraZ);
            BlockPos ground = parentEntity.getGroundPosition(radialPos);
            BlockPos newPos = ground.above(1 + parentEntity.getRandom().nextInt(6));
            if (!parentEntity.isTargetBlocked(Vector3d.atCenterOf(newPos)) && parentEntity.distanceToSqr(Vector3d.atCenterOf(newPos)) > 6) {
                return newPos;
            }
            return null;
        }
    }
}
