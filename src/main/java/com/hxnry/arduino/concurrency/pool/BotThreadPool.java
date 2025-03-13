package com.hxnry.arduino.concurrency.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Purple
 * Date: 6/28/2015
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class BotThreadPool {

    private final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("[Bot Thread #%d]")
            .setDaemon(true)
            .build();

    private ExecutorService service = Executors.newFixedThreadPool(2, threadFactory);

    public ExecutorService getExecutiveServer() {
        return service;
    }
}

