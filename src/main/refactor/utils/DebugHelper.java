package dev.uncandango.alltheleaks.utils;

import dev.uncandango.alltheleaks.AllTheLeaks;

import java.time.Duration;
import java.time.Instant;

public interface DebugHelper {
    static void pauseOnIde(){
        boolean flag;
        Instant instant = Instant.now();
//        AllTheLeaks.LOGGER.warn("Did you remember to set a breakpoint here?");
        boolean bl = flag = Duration.between(instant, Instant.now()).toMillis() > 500L;
        if (!flag) {
            AllTheLeaks.LOGGER.warn("You forgot to set a breakpoint!");
        }
    }
}
