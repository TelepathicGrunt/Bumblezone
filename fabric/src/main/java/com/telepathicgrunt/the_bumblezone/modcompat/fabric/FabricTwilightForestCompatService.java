package com.telepathicgrunt.the_bumblezone.modcompat.fabric;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompatService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueOutput;

public class FabricTwilightForestCompatService implements TwilightForestCompatService {
    private static final String ENDER_BOW_ATTACHED_TAG = "twilightforest:is_ender_arrow";

    @Override
    public boolean getEnderArrowData(Entity entity) {
        // From Porting Lib's implementation
        TagValueOutput tagvalueoutput = TagValueOutput.createWithContext(new ProblemReporter.ScopedCollector(Bumblezone.LOGGER), entity.level().registryAccess());
        entity.saveWithoutId(tagvalueoutput);
        return tagvalueoutput.buildResult().getCompound("fabric:attachments").orElse(new CompoundTag()).getBoolean(ENDER_BOW_ATTACHED_TAG).orElse(false) ;
    }
}
