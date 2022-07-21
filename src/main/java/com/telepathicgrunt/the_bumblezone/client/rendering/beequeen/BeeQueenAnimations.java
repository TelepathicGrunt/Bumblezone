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
                new Keyframe(4f, KeyframeAnimations.scaleVec(1f, 1f, 1f), AnimationChannel.Interpolations.CATMULLROM)))
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

    
    public static final AnimationDefinition BEE_QUEEN_ATTACK = AnimationDefinition.Builder.withLength(0.8333333333333334f)
        .addAnimation("segment3", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.20833333333333334f, KeyframeAnimations.posVec(3.055368266967535f, 0f, -0.7243788740761357f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.4166666666666667f, KeyframeAnimations.posVec(0.8186902518286353f, 0f, -0.670958867446775f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("segment3", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.20833333333333334f, KeyframeAnimations.degreeVec(0f, -6f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.4166666666666667f, KeyframeAnimations.degreeVec(0f, -10.666666666666897f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("segment2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.20833333333333334f, KeyframeAnimations.posVec(-0.14922721068328365f, 0f, 1.4689217949202358f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.4166666666666667f, KeyframeAnimations.posVec(-0.4895518130045147f, 0f, 1.2930733729460357f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("segment2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.20833333333333334f, KeyframeAnimations.degreeVec(0f, 11f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.4166666666666667f, KeyframeAnimations.degreeVec(0f, 4.555555555555712f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("segment1", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.posVec(-0.9238795325112866f, 0f, 0.38268343236508984f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.posVec(-1.44f, -0.61f, 0.6f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("segment1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0.9564495114527745f, -0.019280287246147054f, 2.309591564531729f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.degreeVec(-2.4928285804570622f, -0.13583453360048225f, -6.027030673911758f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("leftwing", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.041666666666666664f, KeyframeAnimations.posVec(0.7647706614294003f, -0.6985851587056553f, -0.3165362717870014f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.08333333333333333f, KeyframeAnimations.posVec(1.7271194309573363f, -1.594372522179121f, -0.7147743546235729f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.16666666666666666f, KeyframeAnimations.posVec(2.6546760143716153f, -3.175390578458652f, -1.1005531612065904f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.2916666666666667f, KeyframeAnimations.posVec(0.4730040144754572f, -2.7787948994916887f, -0.32512670568744717f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.375f, KeyframeAnimations.posVec(-1.3892582462044714f, -2.3403880568973006f, 0.3434795040381243f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.posVec(-4.019339435939979f, -1.3708610685629283f, 1.2271463161110712f), AnimationChannel.Interpolations.LINEAR), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.posVec(-3.722284022683385f, -1.0454864721390198f, 1.0893703307561915f), AnimationChannel.Interpolations.LINEAR), 
                new Keyframe(0.625f, KeyframeAnimations.posVec(-3.1900392316896387f, -0.7614433594286242f, 0.958829114934481f), AnimationChannel.Interpolations.LINEAR), 
                new Keyframe(0.6666666666666666f, KeyframeAnimations.posVec(-2.274438287408149f, -0.5615089047741667f, 0.6442361726528203f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.7083333333333334f, KeyframeAnimations.posVec(-1.4733190901421662f, -0.4465143729315153f, 0.3752178166834242f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.75f, KeyframeAnimations.posVec(-0.7953540469603378f, -0.30642752161098064f, 0.16341959133031922f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.7916666666666666f, KeyframeAnimations.posVec(-0.2676280468952996f, -0.1312661833218764f, 0.03171398350025898f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("leftwing", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.20833333333333334f, KeyframeAnimations.degreeVec(5.781191338581266f, -39.6096734545913f, -3.8976985428334956f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.degreeVec(-17.869635045315974f, 12.585277688735843f, -37.50903324644392f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("rightwing", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.041666666666666664f, KeyframeAnimations.posVec(0.8595413228588006f, -0.6171703174113103f, -0.353072543574003f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.08333333333333333f, KeyframeAnimations.posVec(1.5747462873048907f, -1.406248348119414f, -0.6465162364157152f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.16666666666666666f, KeyframeAnimations.posVec(2.2697668046956694f, -2.7926908855096833f, -0.9412238000599289f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.2916666666666667f, KeyframeAnimations.posVec(0.15067201930060964f, -2.421726532655585f, -0.06683560758326293f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.375f, KeyframeAnimations.posVec(-1.733887369306707f, -2.00558208534595f, 0.7202192560571863f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.posVec(-3.631164700663008f, -1.105534892695045f, 1.5075796614637604f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.6666666666666666f, KeyframeAnimations.posVec(-2.2758225326157837f, -0.7356020796418835f, 0.943611286050986f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.7083333333333334f, KeyframeAnimations.posVec(-1.4433190901421662f, -0.5765143729315155f, 0.5952178166834242f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.75f, KeyframeAnimations.posVec(-0.7753540469603379f, -0.3964275216109806f, 0.31341959133031927f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("rightwing", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.20833333333333334f, KeyframeAnimations.degreeVec(-2.2057121500497487f, 49.966639230227884f, 1.4226486216493868f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.degreeVec(-5.130227950713106f, -25.58809011129837f, -31.93008234142777f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("upperbody", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5f, KeyframeAnimations.posVec(-0.24089294841747427f, -2.7878296484278104f, 0.0998821242746067f), AnimationChannel.Interpolations.LINEAR), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("upperbody", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.16666666666666666f, KeyframeAnimations.degreeVec(7.024843936947491f, -1.4846798631965612f, 18.588152696313955f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-13.32695657882905f, -4.247804356014912f, -34.42235216960369f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("legs2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.125f, KeyframeAnimations.posVec(0.19803440872779943f, 0.7174468844053064f, -0.08271783402588695f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.posVec(0.08208971776225547f, -0.6497144664745573f, -0.03500772506238793f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.3333333333333333f, KeyframeAnimations.posVec(-0.1706095468498383f, -2.123260317658634f, 0.0708038209909401f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.4166666666666667f, KeyframeAnimations.posVec(-0.2332535928705572f, -3.869073669252534f, 0.10379142918619545f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.posVec(-0.25965426797419117f, -4.448277678549844f, 0.11229027541723215f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.posVec(-0.20829875199322817f, -3.7985740167524193f, 0.08629640300414496f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.7083333333333334f, KeyframeAnimations.posVec(-0.10766991291052577f, -1.9904458353846735f, 0.04489075964107862f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.75f, KeyframeAnimations.posVec(-0.0757253896608955f, -1.360107097834044f, 0.02822918004541616f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("legs2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.20833333333333334f, KeyframeAnimations.degreeVec(-4.7578467021703545f, -0.48102351897205153f, -11.532486915715708f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(9.305814923686048f, -1.899776228741739f, 22.998242198928182f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("legs1", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.125f, KeyframeAnimations.posVec(-0.5865784610468693f, -0.185751678034433f, 0.24048348518860255f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.posVec(-0.24295040922285688f, -1.1564382945813279f, 0.09695803080273226f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.3333333333333333f, KeyframeAnimations.posVec(0.07112827209582541f, -2.254786364807934f, -0.030837455039005673f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.4166666666666667f, KeyframeAnimations.posVec(1.2343143092718911f, -3.3703278610243204f, -0.5120949142450931f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.posVec(1.3919613652294722f, -3.610125978360611f, -0.5739794570268469f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.posVec(1.1595246029181094f, -3.159865158917995f, -0.4803695168136396f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("legs1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.2916666666666667f, KeyframeAnimations.degreeVec(-4.7578467021703545f, -0.48102351897205153f, -11.532486915715708f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.degreeVec(9.305814923686048f, -1.899776228741739f, 22.998242198928182f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.posVec(3.0221225808569505f, 2.839491846772632f, -1.2520077697307288f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.posVec(-3.431739729896031f, -4.370853288927655f, 1.4206236342219085f), AnimationChannel.Interpolations.LINEAR), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(16.058565421965614f, -6.346804224216685f, 42.43773520947067f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.degreeVec(6.612815598882662f, -0.9480628768701536f, 16.302916495720183f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("legs3", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.125f, KeyframeAnimations.degreeVec(-4.7578467021703545f, -0.48102351897205153f, -11.532486915715708f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.4166666666666667f, KeyframeAnimations.degreeVec(9.305814923686048f, -1.899776228741739f, 22.998242198928182f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("antenna", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.2916666666666667f, KeyframeAnimations.degreeVec(7.024843936947491f, -1.4846798631965612f, 18.588152696313955f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.degreeVec(-11.06394135506971f, -2.570809266351207f, -29.214351314049217f), AnimationChannel.Interpolations.LINEAR), 
                new Keyframe(0.8333333333333334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))
        ).build();
    
    
    public static final AnimationDefinition BEE_QUEEN_ITEM_THROW = AnimationDefinition.Builder.withLength(1f)
         .addAnimation("segment3", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.posVec(1.8543248747028618f, 0f, -0.22689041200151863f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("segment3", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, -6f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("segment2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.posVec(-0.14922721068328365f, 0f, 1.4689217949202358f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("segment2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 11f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("segment1", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.posVec(-1.44f, -0.61f, 0.6f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.6666666666666666f, KeyframeAnimations.posVec(-0.9238795325112866f, 0f, 0.38268343236508984f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("segment1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-2.4928285804570622f, -0.13583453360048225f, -6.027030673911758f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.6666666666666666f, KeyframeAnimations.degreeVec(0.9564495114527745f, -0.019280287246147054f, 2.309591564531729f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("leftwing", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.posVec(0.9234410943253348f, 0.0206697191958038f, -0.3831831259588486f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("leftwing", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(5.781191338581266f, -39.6096734545913f, -3.8976985428334956f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.degreeVec(0.36894065410751864f, 19.996101187871773f, -0.09512350613022136f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("rightwing", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.posVec(0.9234410943253348f, 0.0206697191958038f, -0.3831831259588486f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("rightwing", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-2.2057121500497487f, 49.966639230227884f, 1.4226486216493868f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.degreeVec(-0.6271755305810984f, -27.49252374076741f, 0.3756963688010728f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("legs2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-4.7578467021703545f, -0.48102351897205153f, -11.532486915715708f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.degreeVec(9.305814923686048f, -1.899776228741739f, 22.998242198928182f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("legs1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.3333333333333333f, KeyframeAnimations.degreeVec(-4.7578467021703545f, -0.48102351897205153f, -11.532486915715708f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.6666666666666666f, KeyframeAnimations.degreeVec(9.305814923686048f, -1.899776228741739f, 22.998242198928182f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.125f, KeyframeAnimations.posVec(0.26150486936578865f, -0.3793312469229895f, -0.11233613946161793f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.2916666666666667f, KeyframeAnimations.posVec(0.6428593960775435f, -0.07561551954298129f, -0.2664846901400926f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.625f, KeyframeAnimations.posVec(0.33642084835262376f, 0.8224937177962011f, -0.1414623827071089f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.125f, KeyframeAnimations.degreeVec(-0.7340241470858018f, -0.0792198339141396f, -1.7743245223478334f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.625f, KeyframeAnimations.degreeVec(4.755060006873464f, -0.4855951020726934f, 11.65826639305078f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("legs3", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.16666666666666666f, KeyframeAnimations.degreeVec(-4.7578467021703545f, -0.48102351897205153f, -11.532486915715708f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(9.305814923686048f, -1.899776228741739f, 22.998242198928182f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("antenna", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.16666666666666666f, KeyframeAnimations.degreeVec(-4.186761514944829f, -0.44190731496013536f, -10.722687857123383f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5416666666666666f, KeyframeAnimations.degreeVec(7.024843936947491f, -1.4846798631965612f, 18.588152696313955f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))
         ).build();
    
    
    public static final AnimationDefinition BEE_QUEEN_ITEM_REJECT = AnimationDefinition.Builder.withLength(1f)
         .addAnimation("segment3", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.posVec(1.8543248747028618f, 0f, -0.22689041200151863f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("segment3", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, -6f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("segment2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.posVec(-0.14922721068328365f, 0f, 1.4689217949202358f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("segment2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 11f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("segment1", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.2916666666666667f, KeyframeAnimations.posVec(-0.9238795325112866f, 0f, 0.38268343236508984f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.6666666666666666f, KeyframeAnimations.posVec(-1.44f, -0.61f, 0.6f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("segment1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.2916666666666667f, KeyframeAnimations.degreeVec(0.9564495114527745f, -0.019280287246147054f, 2.309591564531729f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.6666666666666666f, KeyframeAnimations.degreeVec(-2.4928285804570622f, -0.13583453360048225f, -6.027030673911758f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("leftwing", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.posVec(0.9234410943253348f, 0.0206697191958038f, -0.3831831259588486f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("leftwing", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(5.781191338581266f, -39.6096734545913f, -3.8976985428334956f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.degreeVec(0.36894065410751864f, 19.996101187871773f, -0.09512350613022136f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("rightwing", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.posVec(0.9234410943253348f, 0.0206697191958038f, -0.3831831259588486f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(-0.0038795325112868095f, 0.02f, 0.002683432365089944f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("rightwing", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-2.2057121500497487f, 49.966639230227884f, 1.4226486216493868f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.degreeVec(-0.6271755305810984f, -27.49252374076741f, 0.3756963688010728f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("legs2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-4.7578467021703545f, -0.48102351897205153f, -11.532486915715708f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.degreeVec(9.305814923686048f, -1.899776228741739f, 22.998242198928182f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("legs1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.3333333333333333f, KeyframeAnimations.degreeVec(-4.7578467021703545f, -0.48102351897205153f, -11.532486915715708f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.6666666666666666f, KeyframeAnimations.degreeVec(9.305814923686048f, -1.899776228741739f, 22.998242198928182f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.125f, KeyframeAnimations.posVec(0.26150486936578865f, -0.3793312469229895f, -0.11233613946161793f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.2916666666666667f, KeyframeAnimations.posVec(0.6428593960775435f, -0.07561551954298129f, -0.2664846901400926f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.625f, KeyframeAnimations.posVec(0.33642084835262376f, 0.8224937177962011f, -0.1414623827071089f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.125f, KeyframeAnimations.degreeVec(-0.7340241470858018f, -0.0792198339141396f, -1.7743245223478334f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.625f, KeyframeAnimations.degreeVec(4.755060006873464f, -0.4855951020726934f, 11.65826639305078f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("legs3", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.16666666666666666f, KeyframeAnimations.degreeVec(-4.7578467021703545f, -0.48102351897205153f, -11.532486915715708f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(9.305814923686048f, -1.899776228741739f, 22.998242198928182f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
         .addAnimation("antenna", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.20833333333333334f, KeyframeAnimations.degreeVec(7.024843936947491f, -1.4846798631965612f, 18.588152696313955f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(0.5833333333333334f, KeyframeAnimations.degreeVec(-4.186761514944829f, -0.44190731496013536f, -10.722687857123383f), AnimationChannel.Interpolations.CATMULLROM), 
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))
         ).build();
}
