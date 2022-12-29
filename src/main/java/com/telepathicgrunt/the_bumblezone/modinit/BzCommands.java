package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.commands.NonOpCommands;
import net.minecraftforge.event.RegisterCommandsEvent;

public class BzCommands {
    public static void registerCommand(RegisterCommandsEvent event) {
        NonOpCommands.createCommand(event);
        OpCommands.createCommand(event);
    }
}
