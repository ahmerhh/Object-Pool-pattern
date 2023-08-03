import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ObjectPool<T> {
    private List<T> activeObjects;
    private List<T> availableObjects;
    private Supplier<T> createFunction;
    private BiFunction<T, Object[], T> resetFunction;

    public ObjectPool(Supplier<T> createFunction, BiFunction<T, Object[], T> resetFunction) {
        this.createFunction = createFunction;
        this.resetFunction = resetFunction;
        activeObjects = new ArrayList<>();
        availableObjects = new ArrayList<>();
    }

    public T get() {
        if (availableObjects.isEmpty()) {
            T newObj = createFunction.get();
            activeObjects.add(newObj);
            return newObj;
        } else {
            T obj = availableObjects.remove(availableObjects.size() - 1);
            activeObjects.add(obj);
            return obj;
        }
    }

    public void free(T obj) {
        if (activeObjects.remove(obj)) {
            resetFunction.apply(obj, new Object[0]); // You can pass additional arguments if needed
            availableObjects.add(obj);
        }
    }

    public void clear() {
        availableObjects.clear();
    }

    public static void main(String[] args) {
        ObjectPool<Integer> pool = new ObjectPool<>(
            () -> 0, // Create function
            (obj, resetArgs) -> obj // Reset function
        );

        Integer obj = pool.get();
        pool.free(obj);
    }
}
