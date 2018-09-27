package com.yle.concurrency.singleton;/**
 * Created by zhangling on 2018/9/21.
 */

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 单例类Test
 *
 * @author zhangling
 * @create 2018/09/21
 **/
@Slf4j
public class SingletonExampleTest2 {


    private SingletonExampleTest2() {

    }

    //单例对象
    private static  SingletonExampleTest2 instance = null;

    //静态工厂方法
    public static SingletonExampleTest2 getInstance(){

        if (instance != null){
            return instance;
        }

        synchronized (SingletonExample.class){
            if (instance == null){
                log.info("SingletonExample install {}");
                instance = new SingletonExampleTest2();
            }
        }
        return instance;
    }

    //请求总数
    public  static  int clientTotal = 5000;

    //同时并发执行线程数
    public static  int threadTotal = 200;

    public static Set<Object> sets = new HashSet<>();

//    public static Set<Object> sets = ConcurrentHashMap.<Object> newKeySet();

    public static void main(String[] args) throws Exception {

        sets.clear();

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

                    int hashCode = getInstance().hashCode();

                    sets.add(hashCode);

                    semaphore.release();

                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();



            });
        }

        countDownLatch.await();
        executorService.shutdown();

//        Thread.sleep(2000);

        log.info("集合大小 {} ",  sets.size() );

        Iterator it = sets.iterator();
        while(it.hasNext()){
            log.info("遍历集合元素 {}", it.next());
        }

    }
}
