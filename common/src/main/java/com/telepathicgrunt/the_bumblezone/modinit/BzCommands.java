package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.commands.NonOpCommands;
import com.telepathicgrunt.the_bumblezone.commands.OpCommands;
import com.telepathicgrunt.the_bumblezone.events.RegisterCommandsEvent;

public class BzCommands {
    public static void registerCommand(RegisterCommandsEvent event) {
        NonOpCommands.createCommand(event);
        OpCommands.createCommand(event);
    }
}
