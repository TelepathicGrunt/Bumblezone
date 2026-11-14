package com.telepathicgrunt.the_bumblezone.modcompat.fabric;

import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompatService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;

public class FabricTwilightForestCompatService implements TwilightForestCompatService {
    private static final String ENDER_BOW_ATTACHED_TAG = "twilightforest:ender";

    public boolean getIsEnderArrow(Entity entity) {
        // From Porting Lib's implementation
        CompoundTag compoundTag = entity.saveWithoutId(new CompoundTag());
        ListTag tagList = compoundTag.getList("Tags", Tag.TAG_STRING);
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.getString(i).equals("twilightforest:ender")) {
                return true;
            }
        }
        return false;
    }
}
