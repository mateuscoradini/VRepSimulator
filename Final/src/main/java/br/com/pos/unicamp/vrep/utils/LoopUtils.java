package br.com.pos.unicamp.vrep.utils;

public class LoopUtils {

    public static void loop(final Runnable runnable,
                            final int millis) {
        while (true) {
            runnable.run();
            sleep(millis);
        }
    }

    public static void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
