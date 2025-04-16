# ListenableFuture：让你的Java异步操作更"听话"

## 1. 前言：为什么需要ListenableFuture？

想象一下，你让朋友帮你去买杯咖啡（这是一个耗时操作）。

**普通Future的模式**：你不停地问朋友："咖啡买好了吗？" "买好了吗？" 直到朋友告诉你买好了，你才能拿到咖啡。这期间你啥也干不了，只能干等（这就是`Future.get()`的阻塞方式）。

**ListenableFuture的模式**：你告诉朋友："咖啡买好后，直接送到我工位上就行。" 然后你就继续忙自己的事。朋友买好咖啡后，会主动把咖啡给你送过来（这就是回调）。

`ListenableFuture`是Google Guava库提供的一个接口，它是对Java标准`Future`的增强。它最大的亮点就是**允许你注册回调函数**，当异步任务完成（成功或失败）时，它会自动"通知"你，而不是让你傻傻地等待。

## 2. 核心原理：回调机制

`ListenableFuture`的核心在于"Listen"（监听）。你可以给它添加监听器（回调函数），告诉它："嘿，等你这活儿干完了（无论成功还是失败），记得叫我一声，顺便告诉我结果。"

这个回调机制通过`Futures.addCallback()`方法实现，你需要提供两个回调：
- `onSuccess(V result)`: 当任务成功完成时被调用，参数是任务的执行结果。
- `onFailure(Throwable t)`: 当任务执行过程中抛出异常时被调用，参数是抛出的异常。

## 3. 基本用法：三步走

使用`ListenableFuture`通常需要三步：

**第一步：创建"更听话"的线程池**

你需要一个能返回`ListenableFuture`的线程池。Guava提供了`ListeningExecutorService`，可以通过`MoreExecutors.listeningDecorator()`包装普通的`ExecutorService`来创建。

```java
import com.google.common.util.concurrent.*;
import java.util.concurrent.*;

// 1. 创建一个普通的线程池
ExecutorService pool = Executors.newFixedThreadPool(10);

// 2. 包装成ListeningExecutorService
ListeningExecutorService listeningPool = MoreExecutors.listeningDecorator(pool);
```

**第二步：提交任务，获取ListenableFuture**

使用`listeningPool.submit()`提交你的异步任务（`Callable`或`Runnable`），它会返回一个`ListenableFuture`对象。

```java
ListenableFuture<String> futureTask = listeningPool.submit(() -> {
    System.out.println("开始执行异步任务...");
    // 模拟耗时操作
    TimeUnit.SECONDS.sleep(2); 
    // 假设任务成功，返回结果
    return "咖啡做好了！"; 
    // 如果发生异常，比如：
    // throw new RuntimeException("咖啡机坏了！");
});
```

**第三步：注册回调函数**

这是最关键的一步，使用`Futures.addCallback()`给`futureTask`添加回调。

```java
Futures.addCallback(
    futureTask, // 你要监听的Future
    new FutureCallback<String>() { // 回调接口
        @Override
        public void onSuccess(String result) {
            // 任务成功时执行这里的代码
            System.out.println("太棒了，收到结果: " + result);
        }

        @Override
        public void onFailure(Throwable t) {
            // 任务失败时执行这里的代码
            System.err.println("出错了: " + t.getMessage());
        }
    },
    MoreExecutors.directExecutor() // 指定在哪个线程执行回调，后面会讲
);

System.out.println("我已经提交任务并设置好回调了，现在可以做其他事情...");

// 不要忘记在程序退出前关闭线程池
// listeningPool.shutdown(); 
```

运行这段代码，你会看到程序先打印"我已经提交任务..."，然后等待2秒后，根据任务是成功还是模拟失败，打印`onSuccess`或`onFailure`里的内容。这证明了回调的非阻塞特性。

## 4. 进阶用法：链式操作与组合

`ListenableFuture`的强大之处还在于它的组合能力。

### 4.1 结果转换 (Transform)

假设你拿到咖啡后，想加点糖。`Futures.transform()`允许你对一个Future的结果进行异步转换，生成一个新的Future。

```java
ListenableFuture<String> addSugarFuture = Futures.transform(
    futureTask, // 原始的咖啡Future
    (String coffee) -> { // 转换函数：输入是咖啡，输出是加糖咖啡
        System.out.println("给咖啡加糖...");
        return coffee + " 加了糖";
    },
    listeningPool // 在哪个线程池执行转换操作
);

// 你可以继续给addSugarFuture添加回调
Futures.addCallback(addSugarFuture, new FutureCallback<String>() {
    // ... onSuccess / onFailure ...
}, MoreExecutors.directExecutor());
```

### 4.2 组合多个Future (Combine)

假设你同时点了咖啡和甜点，想等两者都准备好后再一起享用。`Futures.allAsList()`可以组合多个`ListenableFuture`，等它们全部完成后返回一个包含所有结果的列表。

```java
ListenableFuture<String> coffeeFuture = listeningPool.submit(() -> {
    TimeUnit.SECONDS.sleep(1);
    return "拿铁咖啡";
});

ListenableFuture<String> dessertFuture = listeningPool.submit(() -> {
    TimeUnit.SECONDS.sleep(2);
    return "提拉米苏";
});

// 组合两个Future
ListenableFuture<List<String>> coffeeAndDessertFuture = Futures.allAsList(coffeeFuture, dessertFuture);

// 添加回调
Futures.addCallback(coffeeAndDessertFuture, new FutureCallback<List<String>>() {
    @Override
    public void onSuccess(List<String> results) {
        System.out.println("咖啡和甜点都好了: " + results); // 输出: [拿铁咖啡, 提拉米苏] (顺序可能不同)
    }

    @Override
    public void onFailure(Throwable t) {
        System.err.println("准备过程中出错了: " + t.getMessage()); // 如果其中任何一个失败，这里就会被调用
    }
}, MoreExecutors.directExecutor());
```
**注意**: `allAsList` 是"一荣俱荣，一损俱损"模式，只要有一个任务失败，最终组合的Future就是失败状态。如果你想获取所有成功的任务结果（忽略失败的），可以使用`Futures.successfulAsList()`。

## 5. 错误处理：更优雅的方式

除了在`onFailure`里处理异常，`Futures.catching()`提供了一种更链式、更优雅的异常处理方式。它可以捕获特定类型的异常，并返回一个备用结果或者转换成另一种异常。

```java
ListenableFuture<String> robustFuture = Futures.catching(
    futureTask, // 原始任务
    RuntimeException.class, // 捕获RuntimeException及其子类
    (RuntimeException e) -> { // 异常处理函数
        System.err.println("捕获到运行时异常: " + e.getMessage() + "，返回默认值。");
        return "速溶咖啡"; // 返回一个备用结果
    },
    listeningPool // 在哪个线程池执行异常处理
);

// 给处理过的Future添加回调
Futures.addCallback(robustFuture, new FutureCallback<String>() {
    @Override
    public void onSuccess(String result) {
        System.out.println("最终拿到的: " + result); // 如果原始任务失败，这里会收到"速溶咖啡"
    }
    @Override
    public void onFailure(Throwable t) {
        // 如果发生了非RuntimeException，这里仍然会被调用
        System.err.println("发生了无法处理的错误: " + t.getMessage());
    }
}, MoreExecutors.directExecutor());
```

## 6. 回调执行器：`directExecutor` vs `listeningPool`

`Futures.addCallback()`的最后一个参数是`Executor`，它决定了你的回调函数（`onSuccess`/`onFailure`）在哪个线程里执行。
- `MoreExecutors.directExecutor()`: 在**完成异步任务的那个线程**里直接执行回调。优点是快，没有线程切换开销；缺点是如果回调逻辑很耗时，会阻塞那个完成任务的线程。
- `listeningPool` (或其他线程池): 在你指定的线程池里找一个线程来执行回调。优点是不会阻塞完成任务的线程，适合耗时的回调逻辑；缺点是有线程切换的开销。

**选择原则**：回调逻辑简单快速，用`directExecutor`；回调逻辑复杂耗时，用独立的线程池。

## 7. 与Java 8 `CompletableFuture`的比较

Java 8引入了`CompletableFuture`，它提供了与`ListenableFuture`类似甚至更强大的功能（如更灵活的组合、异常处理等），并且是Java标准库的一部分，无需引入Guava依赖。

**选择建议**：
- **新项目（Java 8+）**：优先考虑使用`CompletableFuture`。
- **老项目或严重依赖Guava的项目**：继续使用`ListenableFuture`也是完全可以的，它依然稳定且强大。

## 8. 总结

`ListenableFuture`通过引入回调机制，极大地改善了Java异步编程的体验，让你可以用非阻塞的方式处理异步结果和异常，并方便地进行链式调用和组合。虽然Java 8+有了`CompletableFuture`，但理解`ListenableFuture`对于维护现有代码库和理解异步编程模式仍然非常有价值。

记住它的核心：**提交任务，注册回调，优雅地处理结果与异常。**

