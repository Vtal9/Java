package edu.phystech.threadpools;

public interface ThreadPool {
    void start();
    void execute(Runnable runnable);
}
