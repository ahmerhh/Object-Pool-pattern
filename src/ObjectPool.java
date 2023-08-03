import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface CreateFunction<T> {
    T create();
}

interface ResetFunction<T> {
    T reset(T obj);
}

public class ObjectPool<T> {
    private List<T> activeObjects = new ArrayList<>();
    private List<T> availableObjects = new ArrayList<>();
    private CreateFunction<T> createFunction;
    private ResetFunction<T> resetFunction;
    private int maxSize;

    public ObjectPool(CreateFunction<T> createFunction, ResetFunction<T> resetFunction, int maxSize) {
        this.createFunction = createFunction;
        this.resetFunction = resetFunction;
        this.maxSize = maxSize;
    }

    public T get() {
        if (availableObjects.isEmpty() && activeObjects.size() < maxSize) {
            T newObj = createFunction.create();
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
            resetFunction.reset(obj);
            availableObjects.add(obj);
        }
    }

    public void clear() {
        availableObjects.clear();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter maximum pool size: ");
        int maxSize = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        ObjectPool<Integer> pool = new ObjectPool<>(
            () -> 0, // Create function
            obj -> obj, // Reset function
            maxSize
        );

        while (true) {
            System.out.println("1. Get object from pool");
            System.out.println("2. Free object to pool");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    Integer obj = pool.get();
                    if (obj != null) {
                        System.out.println("Got object: " + obj);
                    } else {
                        System.out.println("Pool is full, cannot get object.");
                    }
                    break;
                case 2:
                    System.out.print("Enter object value to free: ");
                    int value = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    pool.free(value);
                    System.out.println("Object freed.");
                    break;
                case 3:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
