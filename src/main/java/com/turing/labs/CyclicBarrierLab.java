package com.turing.labs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class CyclicBarrierLab {
    private final Logger logger = LogManager.getLogger();
//TODO : AQS, Blocking Queue, 可重入锁原理
    public void doLab2(){
        Runnable hello = () ->{
            System.out.print("Hello\n");
            logger.info(Thread.currentThread().getName());
        };
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1, hello);
//        Semaphore
        Runnable say = () -> {
            try {
                Thread.sleep(1000);
                System.out.print("Say ");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        };
        new Thread(hello).start();
        new Thread(say).start();
    }
    public void doLab(){
        logger.info("Lab start.");
        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e)->{
            logger.error(t.getName() +" " + e.getLocalizedMessage());
        });
        List<Integer> target = Arrays.asList(1,2,3,5,7);


        Runnable makeFinalResult = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int result = target.stream().mapToInt(Integer::valueOf).sum();
            logger.info(result);
        };
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(5, makeFinalResult);
        ExecutorService service = Executors.newFixedThreadPool(5);

//        service.execute(makeFinalResult);
        class Calculator implements Runnable{
            private int index;
            private CyclicBarrier cyclicBarrier;
            Calculator(int i, CyclicBarrier cyclicBarrier){
                index = i;
                this.cyclicBarrier = cyclicBarrier;
            }
            @Override
            public void run() {
                target.set(index,target.get(index) + index);
                System.out.println(Thread.currentThread().getName() + " result = " + target.get(index));
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " index is " + index + " finished.");
            }
        };

        IntStream.range(0,5).forEach(i -> {
//            new Thread(new Calculator(i, cyclicBarrier)).start();
            service.execute(new Calculator(i, cyclicBarrier));
        });
        try {
            service.awaitTermination(5  ,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(!service.isTerminated()) service.shutdownNow();
        }
    }
}
