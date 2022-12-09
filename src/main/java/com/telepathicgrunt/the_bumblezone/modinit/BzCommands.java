package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.commands.NonOpCommands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class BzCommands {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, buildContext, commandSelection) -> NonOpCommands.createCommand(commandDispatcher, buildContext));
    }
}
