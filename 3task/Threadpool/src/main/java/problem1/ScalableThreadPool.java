package problem1;

import java.util.ArrayList;
import java.util.LinkedList;

public class ScalableThreadPool implements ThreadPool {
    private final int minNumberOfThreads;
    private final int maxNumberOfThreads;

    private final ArrayList<ThreadPoolWorker> threadPoolWorkers;
    private final LinkedList<Runnable> tasks;

    ScalableThreadPool(int min, int max) {
        threadPoolWorkers = new ArrayList<ThreadPoolWorker>(min);
        tasks = new LinkedList<>();
        minNumberOfThreads = min;
        maxNumberOfThreads = max;
    }

    @Override
    public void start() {
        threadPoolWorkers.forEach(ThreadPoolWorker::run);
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (threadPoolWorkers) {
            tasks.add(runnable);
            if (threadPoolWorkers.size() < maxNumberOfThreads) {
                ThreadPoolWorker newWorker = new ThreadPoolWorker();
                threadPoolWorkers.add(newWorker);
                newWorker.start();
            } else {
                notify();
            }
        }
    }


    public class ThreadPoolWorker extends Thread {
        public void run() {
            Runnable currentTask;
            while (true) {
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    tasks.remove().run();
                }
                synchronized (threadPoolWorkers) {
                    if (threadPoolWorkers.size() > minNumberOfThreads) {
                        threadPoolWorkers.remove(this);
                    }

                }
            }
        }
    }
}