package problem3;


public interface ExecutionManager {
    Context execute(Runnable callback, Runnable... tasks);
}
