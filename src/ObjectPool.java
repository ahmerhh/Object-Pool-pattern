import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ObjectPool<T> {
    private final List<T> activeObjects = new ArrayList<>();
    private final List<T> availableObjects = new ArrayList<>();
    private final Supplier<T> createFunction;
    private final BiFunction<T, Object[], T> resetFunction;
    private final int maxSize;

    public ObjectPool(Supplier<T> createFunction, BiFunction<T, Object[], T> resetFunction, int maxSize) {
        this.createFunction = createFunction;
        this.resetFunction = resetFunction;
        this.maxSize = maxSize;
    }

    public T get() {
        if (availableObjects.isEmpty() && activeObjects.size() < maxSize) {
            T newObj = createFunction.get();
            activeObjects.add(newObj);
            return newObj;
        } else if (!availableObjects.isEmpty()) {
            T obj = availableObjects.remove(availableObjects.size() - 1);
            activeObjects.add(obj);
            return obj;
        } else {
            return null; // Pool is full
        }
    }

    public void free(T obj) {
        if (activeObjects.remove(obj)) {
            resetFunction.apply(obj, new Object[0]);
            availableObjects.add(obj);
        }
    }

    public void clear() {
        availableObjects.clear();
    }

    public static void main(String[] args) {
        // Test the object pool
        ObjectPool<Integer> pool = new ObjectPool<>(
            () -> 0, // Create function
            (obj, resetArgs) -> obj, // Reset function
            5 // Maximum pool size
        );

        // Get and free objects
        Integer obj1 = pool.get();
        if (obj1 != null) {
            System.out.println("Got object: " + obj1);
            pool.free(obj1);
        }

        // Test pool full scenario
        for (int i = 0; i < 10; i++) {
            Integer obj = pool.get();
            if (obj != null) {
                System.out.println("Got object: " + obj);
                pool.free(obj);
            } else {
                System.out.println("Pool is full, cannot get object.");
            }
        }
    }
}
