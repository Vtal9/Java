package problem1;

import java.util.ArrayList;
import java.util.LinkedList;

public class FixedThreadPool implements ThreadPool {

    private final ArrayList<ThreadPoolWorker> threadPoolWorkers;
    private final LinkedList<Runnable> tasks;

    public FixedThreadPool(int numberOfThreads) {
        this.threadPoolWorkers = new ArrayList<ThreadPoolWorker>(numberOfThreads);
        tasks = new LinkedList<>();
    }

    @Override
    public void start() {
        threadPoolWorkers.forEach(ThreadPoolWorker::start);
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (tasks) {
            tasks.add(runnable);
            notify();
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
            }
        }
    }
}
