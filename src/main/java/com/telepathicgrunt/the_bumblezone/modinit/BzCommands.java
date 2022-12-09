package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.commands.NonOpCommands;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

public class BzCommands {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, buildContext, commandSelection) -> NonOpCommands.createCommand(commandDispatcher, buildContext));
    }
}
