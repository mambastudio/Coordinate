/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.parser.obj.thread;

import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * 
 * Simple thread pool implementation. Java ExecutorService is just a thread pool
 * implementation hence no special additional methods are required. This class 
 * therefore is for verbosity purpose. 
 * 
 */
public class ThreadPool {
    private final ThreadPoolExecutor pool;
    private final int poolSize;
    
    public ThreadPool()
    {        
        poolSize = Runtime.getRuntime().availableProcessors();
        pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize);
    }
    
    public void execute(Runnable runnable)
    {
        pool.submit(runnable);
    }
        
    public void shutdown()
    {        
        pool.shutdown();
        while (true) {
            try {
                System.out.println("Waiting for the service to terminate...");
                if (pool.awaitTermination(100, TimeUnit.SECONDS)) {
                    break;
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
