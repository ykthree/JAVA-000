package learn.java.thread;

import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 一个简单的代码参考：
 */
public class Homework03 {

    public static void main(String[] args) {
//        method();
//        method01();
//        method02();
//        method03();
//        method04();
//        method05();
//        method06();
//        method07();
//        method08();
//        method09();
        method10();
    }

    /**
     * 没加控制，主线程会先于子线程结束
     */
    private static void method() {
        long start = System.currentTimeMillis();
        Thread task = new Thread(() -> {
            int result = sum();
            System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
        });
        task.start();
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法1：主线程执行一个死循环，使用主线程的中断状态作为主线程跳出循环的标识。
     */
    private static void method01() {
        long start = System.currentTimeMillis();
        final Thread main = Thread.currentThread();
        Thread task = new Thread(() -> {
            int result = sum();
            System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
            main.interrupt();
        });
        task.start();
        while (true) {
            boolean interrupted = main.isInterrupted();
            if (interrupted) {
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法2：主线程执行一个死循环，使用局部变量作为标识控制主线程跳出循环。但需要调用{@link System#out}方法，否则循环不会退出。自己
     * 猜测调用{@link System#out}方法会刷新main线程局部变量的副本，程序才会退出，猜测不一定准确，需要继续研究。
     */
    private static void method02() {
        long start = System.currentTimeMillis();
        final boolean[] endArray = {false};
        Thread task = new Thread(() -> {
            int result = sum();
            System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
            endArray[0] = true;
        });
        task.start();
        while (true) {
            boolean end = endArray[0];
            // 不打印end变量，end的值不会变，会一直循环，TODO ...
            System.out.println(end);
            if (end) {
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法3：使用volatile关键字修饰的变量控制主线程退出
     */
    private static void method03() {
        long start = System.currentTimeMillis();
        Flag flag = new Flag();
        Thread task = new Thread(() -> {
            int result = sum();
            System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
            flag.end = true;
        });
        task.start();
        while (true) {
            if (flag.end) {
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法4：wait()/notifyAll()
     */
    private static void method04() {
        long start = System.currentTimeMillis();
        Thread task = new Thread(() -> {
            int result = sum();
            System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
            // 获取管程
            synchronized (Homework03.class) {
                Homework03.class.notifyAll();
//                    Homework03.class.notify();
            }
        });
        task.start();
        // 注意：java.lang.IllegalMonitorStateException
        // 获取管程
        synchronized (Homework03.class) {
            try {
                Homework03.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法5：调用子线程的join方法
     */
    private static void method05() {
        long start = System.currentTimeMillis();
        Thread task = new Thread(() -> {
            int result = sum();
            System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
        });
        task.start();
        try {
            task.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法6：使用Lock
     */
    private static void method06() {
        long start = System.currentTimeMillis();
        final Lock lock = new ReentrantLock();
        // 条件变量，子线程已完成工作
        final Condition childIsDone = lock.newCondition();

        Thread task = new Thread(() -> {
            try {
                lock.lock();
                int result = sum();
                System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
                childIsDone.signalAll();
//                childIsDone.signal();
            } finally {
                lock.unlock();
            }
        });
        task.start();

        lock.lock();
        try {
            childIsDone.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法7：使用{@link LockSupport#park()}方法和{@link LockSupport#unpark(Thread)}方法
     */
    private static void method07() {
        long start = System.currentTimeMillis();
        Thread main = Thread.currentThread();
        Thread task = new Thread(() -> {
            int result = sum();
            System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
            LockSupport.unpark(main);
        });
        task.start();

        LockSupport.park();
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法8：使用{@link CountDownLatch}
     */
    private static void method08() {
        long start = System.currentTimeMillis();
        CountDownLatch downLatch = new CountDownLatch(1);

        Thread task = new Thread(() -> {
            int result = sum();
            System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
            downLatch.countDown();
        });
        task.start();

        try {
            downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法9：使用{@link Semaphore}
     */
    private static void method09() {
        long start = System.currentTimeMillis();
        Semaphore semaphore = new Semaphore(2);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread task = new Thread(() -> {
            try {
                semaphore.acquire();
                int result = sum();
                System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                semaphore.release(2);
            }
        });
        task.start();
        try {
            // 保证子线程先执行
//            Thread.sleep(1000);
            semaphore.acquire(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法10：使用{@link CyclicBarrier}
     */
    private static void method10() {
        long start = System.currentTimeMillis();
        CyclicBarrier barrier = new CyclicBarrier(1);

        Thread task = new Thread(() -> {
            int result = sum();
            System.out.println(Thread.currentThread().getName() + "异步计算结果为：" + result);
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        task.start();

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}

class Flag {
    volatile boolean end = false;
}


