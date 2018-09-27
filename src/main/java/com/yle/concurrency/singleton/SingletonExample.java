package com.yle.concurrency.singleton;/**
 * Created by zhangling on 2018/9/20.
 */

import lombok.extern.slf4j.Slf4j;

/**
 * 单例
 *
 * @author zhangling
 * @create 2018/09/20
 **/
@Slf4j
public class SingletonExample {

    private SingletonExample() {

    }

    //单例对象
    private static  SingletonExample instance = null;

    //静态工厂方法
    public static SingletonExample getInstance(){

        if (instance != null){
            return instance;
        }

        synchronized (SingletonExample.class){
            if (instance == null){
                log.info("SingletonExample install {}");
                instance = new SingletonExample();
            }
        }
        return instance;
    }

    public static void main(String[] args) {

        log.info(SingletonExample.getInstance().toString());

        log.info(SingletonExample.getInstance().toString());

    }

}
