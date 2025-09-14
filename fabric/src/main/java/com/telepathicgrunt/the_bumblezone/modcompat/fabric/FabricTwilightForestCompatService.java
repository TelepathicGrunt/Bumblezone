package com.telepathicgrunt.the_bumblezone.modcompat.fabric;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompatService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueOutput;

import java.util.Optional;

public class FabricTwilightForestCompatService implements TwilightForestCompatService {
    @Override
    public Optional<CompoundTag> getPersistentData(Entity entity) {
        // From Porting Lib's implementation
        TagValueOutput tagvalueoutput = TagValueOutput.createWithContext(new ProblemReporter.ScopedCollector(Bumblezone.LOGGER), entity.level().registryAccess());
        entity.saveWithoutId(tagvalueoutput);
        return tagvalueoutput.buildResult().getCompound("ForgeData");
    }
}
