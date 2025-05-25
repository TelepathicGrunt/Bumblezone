package com.telepathicgrunt.the_bumblezone.services.forge;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.client.BeehemothControls;
import com.telepathicgrunt.the_bumblezone.services.KeyMappingService;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.IKeyConflictContext;

public class ForgeKeyMappingService implements KeyMappingService {

    @Override
    public KeyMapping createBeehemothKey(String display, BeehemothControls.KeyConflict conflict, InputConstants.Key key, String category) {
        return new KeyMapping(display, new ForgeConflict(conflict), key, category);
    }

    private record ForgeConflict(BeehemothControls.KeyConflict conflict) implements IKeyConflictContext {

        @Override
            public boolean isActive() {
                return conflict.isActive();
            }

            @Override
            public boolean conflicts(IKeyConflictContext iKeyConflictContext) {
                if (iKeyConflictContext instanceof ForgeConflict forgeConflict) {
                    return conflict.conflicts(forgeConflict.conflict);
                }

                return conflict.conflicts(new BeehemothControls.KeyConflict() {
                    @Override
                    public boolean isActive() {
                        return iKeyConflictContext.isActive();
                    }

                    @Override
                    public boolean conflicts(BeehemothControls.KeyConflict other) {
                        return iKeyConflictContext.conflicts(new ForgeConflict(other));
                    }
                });
            }
        }
}
