package ch.chang.mybatis.source.test.cach;

import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.impl.PerpetualCache;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BlockingCacheTest {
    public static void main(String[] args) {
        PerpetualCache perpetualCache = new PerpetualCache("test");
        BlockingCache blockingCache = new BlockingCache(perpetualCache);

        Thread getThread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Object a = blockingCache.getObject("a");
            System.out.println("get->"+a);
        });
        getThread.start();

        IntStream.rangeClosed(0, 100).forEach(
                i -> new Thread(() -> {
                    int newValue = ThreadLocalRandom.current().nextInt(2,5);
                    try {
                        TimeUnit.SECONDS.sleep(newValue);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " :" + newValue);
                    blockingCache.putObject("a", newValue);
                }).start()
        );
    }
}
