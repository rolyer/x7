package x7.repository.cache;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class LockTest {

    public static Executor executor = Executors.newFixedThreadPool(500);

    public static AtomicInteger ii= new AtomicInteger();

    public static void main(String[] args){

        for (int i=0; i<2000; i++){

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Object lock = new Object();

                    try {
                        synchronized (lock) {

//                            lock.wait(3000);
                        }
                        Thread.sleep(3000);
                        System.out.println("___________ " + ii.incrementAndGet());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

    }

}
