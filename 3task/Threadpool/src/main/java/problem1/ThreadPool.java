package problem1;

public interface ThreadPool {
    void start();
    void execute(Runnable runnable);
}
