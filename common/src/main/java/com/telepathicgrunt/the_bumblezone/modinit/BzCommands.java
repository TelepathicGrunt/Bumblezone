package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.commands.DebugDevOpCommands;
import com.telepathicgrunt.the_bumblezone.commands.NonOpCommands;
import com.telepathicgrunt.the_bumblezone.commands.OpCommands;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterCommandsEvent;

public class BzCommands {
    public static void registerCommand(BzRegisterCommandsEvent event) {
        NonOpCommands.createCommand(event);
        OpCommands.createCommand(event);
        DebugDevOpCommands.createCommand(event);
    }
}
