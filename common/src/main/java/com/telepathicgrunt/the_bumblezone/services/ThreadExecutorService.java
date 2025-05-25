package com.telepathicgrunt.the_bumblezone.services;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import org.apache.commons.lang3.NotImplementedException;

public interface ThreadExecutorService {

    ThreadExecutorService INSTANCE = GeneralUtils.loadService(ThreadExecutorService.class);

    default Thread createServerThread(Runnable runnable, String name) {
        throw new NotImplementedException("ThreadExecutorService#createServerThread");
    }
}
