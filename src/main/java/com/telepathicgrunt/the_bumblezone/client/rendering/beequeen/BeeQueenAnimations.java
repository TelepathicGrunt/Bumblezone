package com.telepathicgrunt.the_bumblezone.client.rendering.beequeen;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class BeeQueenAnimations {
    public static final AnimationDefinition BEE_QUEEN_IDLE = AnimationDefinition.Builder.withLength(4f).looping()
        .addAnimation("segment3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                new Keyframe(0.5f, KeyframeAnimations.scaleVec(1f, 1f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.5f, KeyframeAnimations.scaleVec(1.035f, 1.035f, 1.035f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4f, KeyframeAnimations.scaleVec(1.005f, 1.005f, 1.005f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("segment2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2f, KeyframeAnimations.scaleVec(1.04f, 1.04f, 1.04f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4f, KeyframeAnimations.scaleVec(1f, 1f, 1f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("segment1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2f, KeyframeAnimations.degreeVec(-3f, 0f, -3f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("rightwing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.20833333333333334f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.375f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.5833333333333334f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.9583333333333334f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.125f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.3333333333333333f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.7083333333333333f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.875f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.0833333333333335f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.4583333333333335f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.625f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.8333333333333335f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.1666666666666665f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.3333333333333335f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.5f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.6666666666666665f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.8333333333333335f, KeyframeAnimations.degreeVec(17.81263344483159f, 46.85725507617963f, 7.076121633234834f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("legs1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-1f, 0f, -3f), AnimationChannel.Interpolations.LINEAR), 
                new Keyframe(0.3333333333333333f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.3333333333333333f, KeyframeAnimations.degreeVec(4f, 0f, 10f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.3333333333333335f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.3333333333333335f, KeyframeAnimations.degreeVec(-4f, 0f, -10f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4.333333333333333f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2f, KeyframeAnimations.posVec(-0.25f, 0f, 0.1f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2f, KeyframeAnimations.degreeVec(-2.3870184786801474f, 0.24035499111323588f, -3.505627393786199f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("legs3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1f, KeyframeAnimations.degreeVec(-4f, 0f, -10f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3f, KeyframeAnimations.degreeVec(4f, 0f, 10f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("antenna", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2f, KeyframeAnimations.degreeVec(6.178418333894115f, -1.0107281709192648f, 16.480247626627715f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("legs2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1f, KeyframeAnimations.degreeVec(4f, 0f, 10f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3f, KeyframeAnimations.degreeVec(-4f, 0f, -10f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("upperbody", new AnimationChannel(AnimationChannel.Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.5f, KeyframeAnimations.posVec(-0.2f, 0f, 0.2f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.0416666666666665f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.9583333333333335f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("upperbody", new AnimationChannel(AnimationChannel.Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.5f, KeyframeAnimations.scaleVec(1.025f, 1.025f, 1.025f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.0416666666666665f, KeyframeAnimations.scaleVec(1f, 1f, 1f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("leftwing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.20833333333333334f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.375f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.5833333333333334f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.9583333333333334f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.125f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.3333333333333333f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.7083333333333333f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(1.875f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.0833333333333335f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.4583333333333335f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.625f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(2.8333333333333335f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.1666666666666665f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.3333333333333335f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.5f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.6666666666666665f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(3.8333333333333335f, KeyframeAnimations.degreeVec(-21.06976242994324f, -41.16140018522674f, 22.322391657779463f), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))
        ).build();
}
