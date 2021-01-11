package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpawnRestriction.class)
public interface SpawnRestrictionAccessor {

    @Invoker("register")
    static <T extends MobEntity> void bz_invokeRegister(EntityType<T> type, SpawnRestriction.Location location, Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate) {
        throw new UnsupportedOperationException();
    }
}
