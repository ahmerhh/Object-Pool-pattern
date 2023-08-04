import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

interface CreateFunction<T> {
    T create();
}

interface ResetFunction<T> {
    T reset(T obj, Object... args);
}

public class ObjectPool_main<T> {
    private List<T> activeObjects = new ArrayList<>();
    private List<T> availableObjects = new ArrayList<>();
    private ObjectPoolOpts<T> opts;
    private Timer collectTimer;

    public ObjectPool_main(ObjectPoolOpts<T> opts) {
        this.opts = opts;

        for (int i = 0; i < opts.initialSize; ++i) {
        	availableObjects.add(opts.create());
        }

        if (opts.collectFreq != -1) {
            collectTimer = new Timer();
            collectTimer.schedule(new CollectTask(), opts.collectFreq, opts.collectFreq);
        }
    }

    public T get(Object... args) {
        T obj;
        if (!availableObjects.isEmpty()) {
            obj = opts.reset(availableObjects.remove(availableObjects.size() - 1), args);
        } else {
            obj = opts.create(args);
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
        ObjectPoolOpts<Integer> opts = new ObjectPoolOpts<>(
            0, // initialSize
            100, // maxSize
            () -> 0, // create function
            (obj, resetArgs) -> obj, // reset function
            -1 // collectFreq
        );

        ObjectPool_main <Integer> pool = new ObjectPool_main <>(opts);

        // Testing the object pool
        Integer obj1 = pool.get();
        System.out.println("Got object: " + obj1);
        pool.free(obj1);

        // Dispose the pool
        pool.dispose();
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
