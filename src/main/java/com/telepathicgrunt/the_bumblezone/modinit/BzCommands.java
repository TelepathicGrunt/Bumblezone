package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.advancements.BlockStateSpecificTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.CounterTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.EntitySpecificTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.GenericTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.ItemSpecificTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.KilledCounterTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.RecipeDiscoveredTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.TargetAdvancementDoneTrigger;
import com.telepathicgrunt.the_bumblezone.commands.NoneOpCommands;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegisterCommandsEvent;

public class BzCommands {
    public static void registerCommand(RegisterCommandsEvent event) {
        CommandBuildContext buildContext = new CommandBuildContext(RegistryAccess.BUILTIN.get());
        NoneOpCommands.createCommand(event.getDispatcher(), buildContext);
    }
}
