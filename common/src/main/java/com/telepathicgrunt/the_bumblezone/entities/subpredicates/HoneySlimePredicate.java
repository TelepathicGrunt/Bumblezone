package com.telepathicgrunt.the_bumblezone.entities.subpredicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzSubPredicates;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record HoneySlimePredicate(boolean isBaby) implements EntitySubPredicate {
    public static final MapCodec<HoneySlimePredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Codec.BOOL.fieldOf("is_baby").forGetter(HoneySlimePredicate::isBaby))
                    .apply(instance, HoneySlimePredicate::new)
    );

    public static HoneySlimePredicate isBabied(boolean isBaby) {
        return new HoneySlimePredicate(isBaby);
    }

    public boolean matches(Entity entity, ServerLevel serverLevel, @Nullable Vec3 vec3) {
        return entity instanceof HoneySlimeEntity honeySlime && this.isBaby == honeySlime.isBaby();
    }

    public MapCodec<HoneySlimePredicate> codec() {
        return BzSubPredicates.HONEY_SLIME.get();
    }
}
