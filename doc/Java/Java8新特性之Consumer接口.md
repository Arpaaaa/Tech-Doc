# Java8新特性之Consumer接口

## 1. 基本概念

`Consumer<T>` 是Java 8中引入的一个函数式接口，位于 `java.util.function` 包中。它表示接受一个输入参数并执行操作但不返回任何结果的操作。

## 2. 接口定义

```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
    
    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
```

## 3. 核心特性

1. **单一参数**：接受一个类型为T的参数
2. **无返回值**：返回类型为void
3. **函数式接口**：可以使用Lambda表达式实现
4. **链式操作**：通过`andThen`方法支持操作链

## 4. 主要用途

1. **数据处理**：对数据进行处理但不返回结果
2. **日志记录**：记录操作日志
3. **对象修改**：修改对象状态
4. **Stream API**：在Stream的forEach操作中使用
5. **异步操作**：在异步编程中处理结果

## 5. 常见使用场景

### 5.1 基本使用
```java
// 使用Lambda表达式
Consumer<String> printConsumer = str -> System.out.println(str);
printConsumer.accept("Hello World");

// 使用方法引用
Consumer<String> printConsumer = System.out::println;
printConsumer.accept("Hello World");
```

### 5.2 链式操作
```java
Consumer<String> printUpperCase = str -> System.out.println(str.toUpperCase());
Consumer<String> printLowerCase = str -> System.out.println(str.toLowerCase());

// 组合两个Consumer
Consumer<String> combinedConsumer = printUpperCase.andThen(printLowerCase);
combinedConsumer.accept("Hello"); // 输出: HELLO hello
```

### 5.3 Stream API中的应用
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
names.stream()
     .forEach(System.out::println);
```

### 5.4 对象处理
```java
class Person {
    private String name;
    private int age;
    
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
}

Consumer<Person> updatePerson = person -> {
    person.setName("John");
    person.setAge(30);
};
```

## 6. 与其他函数式接口的区别

| 接口 | 参数 | 返回值 | 主要用途 |
|------|------|--------|----------|
| Consumer<T> | 1个 | void | 消费数据 |
| Supplier<T> | 0个 | T | 提供数据 |
| Function<T,R> | 1个 | R | 转换数据 |
| Predicate<T> | 1个 | boolean | 判断条件 |

## 7. 最佳实践

1. **保持简单**：Consumer应该只做一件事
2. **避免副作用**：尽量减少对外部状态的修改
3. **命名清晰**：使用有意义的变量名和方法名
4. **组合使用**：利用`andThen`方法组合多个操作
5. **异常处理**：在Consumer内部处理可能的异常

## 8. 注意事项

1. Consumer操作是终端操作，会消耗数据
2. 在Stream中使用时要注意并行处理的安全性
3. 避免在Consumer中修改外部状态
4. 注意Consumer的链式调用顺序
5. 考虑使用BiConsumer处理两个参数的情况

## 9. 实际应用示例

### 9.1 日志记录
```java
Consumer<String> logger = message -> {
    System.out.println("[" + LocalDateTime.now() + "] " + message);
};
logger.accept("User logged in");
```

### 9.2 数据验证
```java
Consumer<String> validator = input -> {
    if (input == null || input.isEmpty()) {
        throw new IllegalArgumentException("Input cannot be empty");
    }
};
```

### 9.3 对象初始化
```java
class User {
    private String name;
    private String email;
    
    public void initialize(Consumer<User> initializer) {
        initializer.accept(this);
    }
}

User user = new User();
user.initialize(u -> {
    u.name = "John";
    u.email = "john@example.com";
});
```

## 10. 总结

Consumer接口是Java 8函数式编程的重要组成部分，它提供了一种简洁的方式来处理数据而不返回结果。通过合理使用Consumer接口，可以使代码更加简洁、可读性更强，同时支持函数式编程的特性。

