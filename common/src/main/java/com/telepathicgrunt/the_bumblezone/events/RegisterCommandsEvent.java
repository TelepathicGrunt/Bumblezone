package com.telepathicgrunt.the_bumblezone.events;

import com.mojang.brigadier.CommandDispatcher;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public record RegisterCommandsEvent(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection env, CommandBuildContext context) {

    public static final EventHandler<RegisterCommandsEvent> EVENT = new EventHandler<>();
}
