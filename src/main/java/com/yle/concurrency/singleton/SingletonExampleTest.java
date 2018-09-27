package com.yle.concurrency.singleton;/**
 * Created by zhangling on 2018/9/21.
 */

import lombok.extern.slf4j.Slf4j;
import sun.rmi.runtime.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 单例类Test
 *
 * @author zhangling
 * @create 2018/09/21
 **/
@Slf4j
public class SingletonExampleTest {

    //请求总数
    public  static  int clientTotal = 5000;

    //同时并发执行线程数
    public static  int threadTotal = 200;

    public static Set<Object> sets = new HashSet<>();

    public static void main(String[] args) throws Exception {

        sets.clear();

        log.info("集合大小 {}", sets.size() );

        Iterator itFirst = sets.iterator();
        while(itFirst.hasNext()){
            log.info("{}", itFirst.next());
        }

        ExecutorService executorService = Executors.newCachedThreadPool();

        final Semaphore semaphore = new Semaphore(threadTotal);

        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);

        for (int i = 0; i < clientTotal; i++){
            executorService.execute(() -> {
                try {

                    semaphore.acquire();

                    int hashCode = SingletonExample.getInstance().hashCode();

                    sets.add(hashCode);

                    log.info("线程 {}, {}", Thread.currentThread().getName(), hashCode);

                    semaphore.release();

                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();



            });
        }

        countDownLatch.await();
        executorService.shutdown();

        log.info("集合大小 {},{}", Thread.currentThread().getName(), sets.size() );

        Iterator it = sets.iterator();
        while(it.hasNext()){
            log.info("遍历集合元素 {}", it.next());
        }

    }
}
