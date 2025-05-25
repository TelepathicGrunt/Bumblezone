package com.telepathicgrunt.the_bumblezone.services.fabric;

import com.telepathicgrunt.the_bumblezone.services.ThreadExecutorService;

public class FabricThreadExecutorService implements ThreadExecutorService {
    public Thread createServerThread(Runnable runnable, String name) {
        return new Thread(runnable, name);
    }
}
