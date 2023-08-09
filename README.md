# Java Object Pool Example

This repository contains two Java code examples that demonstrate object pooling using different approaches. The first example (`ObjectPool_main`) showcases a basic implementation of an object pool using an interface-based approach, while the second example (`ObjectPool`) extends this concept to include more advanced features such as object reset and automatic object collection.

## Installation

```sh
$ npm install --save @ahmerhh/object-pool-pattren 
```


## Object Pool (Main) - Basic Object Pool

The first example (`ObjectPool_main`) demonstrates a straightforward implementation of an object pool using interfaces and basic object management functions. The example includes an `ObjectPool` class that allows objects to be acquired from and released back to the pool.

### Features

- Acquire objects from the pool
- Release objects back to the pool

## Object Pool - Advanced Object Pool

The second example (`ObjectPool`) builds upon the concepts demonstrated in `ObjectPool_main` to create an extended object pool with advanced features. It introduces an `ObjectPool_main` class that includes automatic object collection, customizable configuration options, and object reset using the `ResetFunction` interface.

### Features

- Acquire objects from the pool with automatic collection
- Configure initial and maximum pool sizes
- Reset objects using the `ResetFunction` interface

## Usage

Both examples come with their own `main` methods for demonstration purposes. To run each example, you can follow these steps:

1. Make sure you have Java installed on your machine.
2. Clone this repository.
3. Navigate to the appropriate directory for the example you want to run (`ObjectPool_main` or `ObjectPool`).
4. Compile the Java files using the command: `javac *.java`.
5. Run the compiled Java program using the command: `java [MainClassName]` (replace `[MainClassName]` with the actual main class name).

## Customization

You can customize the `CreateFunction` and `ResetFunction` implementations in the examples to modify object creation and reset behavior according to your requirements.

## Contributions

Contributions to this repository are welcome! If you find any issues, have suggestions, or want to add improvements, please feel free to create pull requests or issues.

## License

This project is licensed under the [MIT License](LICENSE).
