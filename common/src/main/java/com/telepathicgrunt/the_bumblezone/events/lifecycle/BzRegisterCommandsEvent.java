package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.mojang.brigadier.CommandDispatcher;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public record BzRegisterCommandsEvent(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection env, CommandBuildContext context) {

    public static final EventHandler<BzRegisterCommandsEvent> EVENT = new EventHandler<>();
}
