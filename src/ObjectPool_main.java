import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;



public class ObjectPool_main<T> {
    private List<T> activeObjects = new ArrayList<>();
    private List<T> availableObjects = new ArrayList<>();
    private ObjectPoolOpts<T> opts;
    private Timer collectTimer;

    public ObjectPool_main(ObjectPoolOpts<T> opts) {
        this.opts = opts;

        for (int i = 0; i < opts.initialSize; ++i) {
            availableObjects.add(opts.create.create());
        }

        if (opts.collectFreq != -1) {
            collectTimer = new Timer();
            collectTimer.schedule(new CollectTask(), opts.collectFreq, opts.collectFreq);
        }
    }

    public T get(Object... args) {
        T obj;
        if (!availableObjects.isEmpty()) {
            obj = opts.reset.reset(availableObjects.remove(availableObjects.size() - 1), args);
        } else {
            obj = opts.create.create();
        }

        activeObjects.add(obj);
        return obj;
    }

    public void free(T obj) {
        int index = activeObjects.indexOf(obj);
        if (index != -1) {
            activeObjects.remove(index);
            availableObjects.add(obj);
        }
    }

    public void dispose() {
        if (collectTimer != null) {
            collectTimer.cancel();
        }
        activeObjects = null;
        availableObjects = null;
    }

    private class CollectTask extends TimerTask {
        @Override
        public void run() {
            availableObjects.subList(opts.maxSize, availableObjects.size()).clear();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter initial pool size: ");
        int initialSize = scanner.nextInt();

        System.out.print("Enter maximum pool size: ");
        int maxSize = scanner.nextInt();

        System.out.print("Enter collect frequency (-1 for no collection): ");
        int collectFreq = scanner.nextInt();

        ObjectPoolOpts<Integer> opts = new ObjectPoolOpts<>(
            initialSize,
            maxSize,
            new CreateFunction<>(),
            new ResetFunction<>(),
            collectFreq
        );

        ObjectPoolMain<Integer> pool = new ObjectPoolMain<>(opts);

        // Testing the object pool
        Integer obj1 = pool.get();
        System.out.println("Got object: " + obj1);
        pool.free(obj1);

        // Dispose the pool
        pool.dispose();

        scanner.close();
    }
}

class ObjectPoolOpts<T> {
    int initialSize;
    int maxSize;
    CreateFunction<T> create;
    ResetFunction<T> reset;
    int collectFreq;

    ObjectPoolOpts(int initialSize, int maxSize, CreateFunction<T> create, ResetFunction<T> reset, int collectFreq) {
        this.initialSize = initialSize;
        this.maxSize = maxSize;
        this.create = create;
        this.reset = reset;
        this.collectFreq = collectFreq;
    }
}
