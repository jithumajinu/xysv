package io.crm.app.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class ThreadUtils {
    private ThreadUtils() {
    }

    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException var2) {
            // logger.trace(var2.getMessage(), var2);
            Thread.currentThread().interrupt();
        }
    }

    public static <T> T timed(Supplier<T> supplier, String name) {
        Instant start = Instant.now();
        T result = supplier.get();
        //logger.debug("Called {} ({} ms)", name, DateUtils.diffMillis(start));
        return result;
    }

    public static void async(Runnable runnable) {
        (new Thread(runnable)).start();
    }

    public static ExecutorService async(List<Runnable> tasks, int size) {
        ExecutorService executor = Executors.newFixedThreadPool(size);
        Objects.requireNonNull(executor);
        tasks.forEach(executor::submit);
        return executor;
    }

    public static void async(List<Runnable> tasks, int size, Duration duration) throws TimeoutException {
        ExecutorService executor = Executors.newFixedThreadPool(size);
        Objects.requireNonNull(executor);
        tasks.forEach(executor::submit);
        try {
            executor.shutdown();
            if (!executor.awaitTermination(duration.toMillis(), TimeUnit.MILLISECONDS)) {
                throw new TimeoutException(String.format("Failed to complete tasks within %s", duration));
            }
            // logger.debug("Completed {} tasks)", tasks.size());
        } catch (InterruptedException var5) {
            // logger.trace(var5.getMessage(), var5);
            Thread.currentThread().interrupt();
        }
    }
}
