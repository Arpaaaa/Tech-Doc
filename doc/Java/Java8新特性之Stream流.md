# Java8 新特性之Steam流？

## 一、Stream流
Java Stream流是Java 8引入的一个重要特性，它提供了一种函数式编程的方式来处理集合数据。Stream API让开发者能够以声明式的方式处理数据，而不是使用循环这样的命令式编程方式。以下是对Stream流的主要特性和用法的总结：

### 1. Stream流的基本概念
- **Stream定义**：Stream是Java 8 API的新成员，它允许以声明性方式处理数据集合
- **数据流**：Stream不是集合元素，而是从支持数据处理操作的源生成的元素
- **惰性计算**：Stream操作是延迟执行的，它只有在终端操作被执行时才会开始计算
- **一次性使用**：Stream只能被消费一次，一旦执行了终端操作，Stream就不能再被使用

### 2. Stream流的创建方式
- **从集合创建**：通过`Collection.stream()`或`Collection.parallelStream()`
- **从数组创建**：通过`Arrays.stream(Object[])`
- **使用Stream.of**：通过`Stream.of(T...)`创建
- **使用Stream.iterate**：通过`Stream.iterate()`创建无限流
- **使用Stream.generate**：通过`Stream.generate()`创建无限流
- **文件生成Stream**：通过`Files.lines()`或`Files.walk()`

### 3. Stream操作分类
- **中间操作**：返回Stream的操作，可以链式调用，不会立即执行
- **终端操作**：返回非Stream结果的操作，会触发流的计算

### 4. 常用的中间操作
- **filter(Predicate)**：过滤流中的元素
- **map(Function)**：对流中的每个元素应用函数
- **flatMap(Function)**：对流中的每个元素应用函数，并将结果展平为一个流
- **distinct()**：去除流中的重复元素
- **sorted()**：对流中的元素进行排序
- **peek(Consumer)**：对流中的每个元素执行操作，主要用于调试
- **limit(long)**：截取流中的前n个元素
- **skip(long)**：跳过流中的前n个元素

### 5. 常用的终端操作
- **forEach(Consumer)**：对流中的每个元素执行操作
- **collect(Collector)**：收集流中的元素到集合中
- **reduce(BinaryOperator)**：使用指定的函数对流中的元素进行归约操作
- **count()**：返回流中的元素个数
- **anyMatch(Predicate)**：判断流中是否有元素满足给定的条件
- **allMatch(Predicate)**：判断流中的所有元素是否都满足给定的条件
- **noneMatch(Predicate)**：判断流中的所有元素是否都不满足给定的条件
- **findFirst()**：返回流中的第一个元素
- **findAny()**：返回流中的任意一个元素
- **min(Comparator)**：返回流中的最小值
- **max(Comparator)**：返回流中的最大值
- **toArray()**：将流中的元素转换为数组

### 6. Collectors收集器
- **toList()**：收集元素到List
- **toSet()**：收集元素到Set
- **toMap()**：收集元素到Map
- **joining()**：连接字符串元素
- **counting()**：计算元素个数
- **summarizingInt/Long/Double()**：汇总数值统计信息
- **groupingBy()**：根据指定的分类函数对元素进行分组
- **partitioningBy()**：根据Predicate对元素进行分区

### 7. 并行流
- **使用parallel()**：将串行流转换为并行流
- **使用parallelStream()**：直接从集合创建并行流
- **使用并行流的注意事项**：
  - 并行流使用ForkJoinPool执行操作
  - 并行流不一定比串行流快，取决于数据大小和操作复杂度
  - 避免使用有状态、非关联性的操作
  - 避免修改共享状态

### 8. Stream流的优点
- **声明式编程**：代码更加简洁、易读
- **函数式编程**：支持函数式编程范式
- **内部迭代**：由Stream API内部管理迭代过程
- **惰性求值**：提高性能
- **并行支持**：更容易编写并行处理代码
- **无副作用**：推荐使用无副作用的函数进行操作

### 9. 代码示例
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Dave", "Eve");

// 过滤并转换
List<String> filteredNames = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// 排序
List<String> sortedNames = names.stream()
    .sorted()
    .collect(Collectors.toList());

// 汇总统计
IntSummaryStatistics stats = names.stream()
    .mapToInt(String::length)
    .summaryStatistics();

// 分组
Map<Integer, List<String>> groupedNames = names.stream()
    .collect(Collectors.groupingBy(String::length));

// 并行处理
long count = names.parallelStream()
    .filter(name -> name.startsWith("A"))
    .count();
```

### 10. 注意事项和最佳实践
- **使用Stream.forEach()还是集合的forEach()**：前者适用于流操作链的结束，后者简单迭代集合元素
- **避免过度使用Stream**：简单操作使用传统循环可能更清晰
- **正确选择中间操作的顺序**：例如，先filter再map通常比先map再filter效率高
- **考虑使用并行流时的线程安全性**：并行操作可能导致线程安全问题
- **Stream不能重复使用**：一旦执行了终端操作，流就会关闭
- **避免在流操作中修改外部状态**：尽量使用纯函数式操作，避免副作用
