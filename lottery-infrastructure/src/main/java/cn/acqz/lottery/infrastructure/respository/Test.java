package cn.acqz.lottery.infrastructure.respository;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: qz
 * @Date: 2024/2/2
 */
public class Test {
    private Integer times;

    static ExecutorService executorService = Executors.newCachedThreadPool();
    private static final Object LOCK = new Object();

    public Integer getTimes() {
        return times;
    }
    public static void main(String[] args) throws InterruptedException {
        Instance instance = new Instance();
        HashSet set = new HashSet();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 100; i++) {
            executorService.execute(()->{
                Instance test = instance.getInstance();
                synchronized (LOCK){
                    set.add(test);
                    arrayList.add(test);
                }
            });
        }
        TimeUnit.SECONDS.sleep(1);
        System.out.println();
    }
}
