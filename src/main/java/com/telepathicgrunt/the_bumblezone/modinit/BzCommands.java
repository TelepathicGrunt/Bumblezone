package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.commands.NonOpCommands;
import com.telepathicgrunt.the_bumblezone.commands.OpCommands;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

public class BzCommands {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, buildContext, commandSelection) -> NonOpCommands.createCommand(commandDispatcher, buildContext));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, buildContext, commandSelection) -> OpCommands.createCommand(commandDispatcher, buildContext));
    }
}
