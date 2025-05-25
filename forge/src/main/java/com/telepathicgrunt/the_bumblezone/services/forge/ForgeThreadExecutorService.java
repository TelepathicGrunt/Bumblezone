package com.telepathicgrunt.the_bumblezone.services.forge;

import com.telepathicgrunt.the_bumblezone.services.ThreadExecutorService;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import org.jetbrains.annotations.Contract;

public class ForgeThreadExecutorService implements ThreadExecutorService {

    public Thread createServerThread(Runnable runnable, String name) {
        return new Thread(SidedThreadGroups.SERVER, runnable, name);
    }
}
