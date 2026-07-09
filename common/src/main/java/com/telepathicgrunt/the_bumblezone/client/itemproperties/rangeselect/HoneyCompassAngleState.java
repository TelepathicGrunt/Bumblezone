package com.telepathicgrunt.the_bumblezone.client.itemproperties.rangeselect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.HoneyCompassTargetData;
import com.telepathicgrunt.the_bumblezone.mixin.NeedleDirectionHelperAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.NeedleDirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class HoneyCompassAngleState extends NeedleDirectionHelper {
    public static final MapCodec<HoneyCompassAngleState> MAP_CODEC = RecordCodecBuilder.mapCodec(
        i -> i.group(
            Codec.BOOL.optionalFieldOf("wobble", true).forGetter(honeyCompassAngleState -> ((NeedleDirectionHelperAccessor)honeyCompassAngleState).bumblezone$callWobble())
        )
        .apply(i, HoneyCompassAngleState::new)
    );
    private final NeedleDirectionHelper.Wobbler wobbler;
    private final NeedleDirectionHelper.Wobbler noTargetWobbler;
    private final RandomSource random = RandomSource.create();

    public HoneyCompassAngleState(boolean wobble) {
        super(wobble);
        this.wobbler = this.newWobbler(0.8F);
        this.noTargetWobbler = this.newWobbler(0.8F);
    }

    @Override
    protected float calculate(ItemStack itemStack, ClientLevel level, int seed, ItemOwner owner) {
        GlobalPos compassTargetPos = this.getStructurePosition(level, itemStack);
        long gameTime = level.getGameTime();
        return !isValidCompassTargetPos(owner, compassTargetPos)
                ? this.getRandomlySpinningRotation(seed, gameTime)
                : this.getRotationTowardsCompassTarget(owner, gameTime, compassTargetPos.pos());
    }

    private float getRandomlySpinningRotation(int seed, long gameTime) {
        if (this.noTargetWobbler.shouldUpdate(gameTime)) {
            this.noTargetWobbler.update(gameTime, this.random.nextFloat());
        }

        float targetRotation = this.noTargetWobbler.rotation() + hash(seed) / 2.1474836E9F;
        return Mth.positiveModulo(targetRotation, 1.0F);
    }

    private float getRotationTowardsCompassTarget(ItemOwner owner, long gameTime, BlockPos compassTargetPos) {
        float angleToTarget = (float)getAngleFromEntityToPos(owner, compassTargetPos);
        float ownerYRotation = getWrappedVisualRotationY(owner);
        float targetRotation;
        if (owner.asLivingEntity() instanceof Player player && player.isLocalPlayer() && player.level().tickRateManager().runsNormally()) {
            if (this.wobbler.shouldUpdate(gameTime)) {
                this.wobbler.update(gameTime, 0.5F - (ownerYRotation - 0.25F));
            }

            targetRotation = angleToTarget + this.wobbler.rotation();
        } else {
            targetRotation = 0.5F - (ownerYRotation - 0.25F - angleToTarget);
        }

        return Mth.positiveModulo(targetRotation, 1.0F);
    }

    private static boolean isValidCompassTargetPos(ItemOwner owner, @Nullable GlobalPos positionToPointTo) {
        return positionToPointTo != null
                && positionToPointTo.dimension() == owner.level().dimension()
                && !(positionToPointTo.pos().distToCenterSqr(owner.position()) < 1.0E-5F);
    }

    private static double getAngleFromEntityToPos(ItemOwner owner, BlockPos position) {
        Vec3 target = Vec3.atCenterOf(position);
        Vec3 ownerPosition = owner.position();
        return Math.atan2(target.z() - ownerPosition.z(), target.x() - ownerPosition.x()) / (float) (Math.PI * 2);
    }

    private static float getWrappedVisualRotationY(ItemOwner owner) {
        return Mth.positiveModulo(owner.getVisualRotationYInDegrees() / 360.0F, 1.0F);
    }

    private static int hash(int input) {
        return input * 1327217883;
    }

    private GlobalPos getStructurePosition(Level level, ItemStack itemStack) {
        HoneyCompassTargetData honeyCompassTargetData = itemStack.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get());
        boolean structurePos = honeyCompassTargetData.targetPos().isPresent();
        boolean dimension = honeyCompassTargetData.targetDimension().isPresent();
        if (structurePos && dimension) {
            Optional<ResourceKey<Level>> optional = honeyCompassTargetData.targetDimension();
            if (level.dimension() == optional.get()) {
                return GlobalPos.of(optional.get(), honeyCompassTargetData.targetPos().orElse(BlockPos.ZERO));
            }
        }

        return null;
    }

}
