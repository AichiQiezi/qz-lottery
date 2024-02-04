package cn.acqz.lottery.application.process.draw.res;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: qz
 * @Date: 2024/1/31
 */
public class RuleQuantificationCrowdResult {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("cf1 execute");
            return 1;
        }).thenApply(a -> {
            throw new RuntimeException();
        });
        System.out.println("end");
        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("cf2 execute");
            return 2;
        });
        CompletableFuture<Object> cf3 = cf1.thenCombineAsync(cf2, (a, b) -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("cf3 execute");
            return a+b;
        });
        cf1.join();
    }
}
