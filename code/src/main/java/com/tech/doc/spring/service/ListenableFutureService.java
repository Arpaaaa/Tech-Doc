import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

public class ListenableFutureService {
    
    public void create() {

        // 创建一个普通的线程池
        ExecutorService pool = Executors.newFixedThreadPool(10);

        // 使用ListeningExecutorService包装线程池
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(pool);

        // 提交任务并获取ListenableFuture
        ListenableFuture<String> future = listeningExecutorService.submit(() -> {
            System.out.println("任务执行中...");
            return "咖啡做好了..";
        });

        // 注册回调函数
        Futures.addCallback(future, new FutureCallback<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("咖啡买好了，送到我工位上就行..");
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("咖啡买失败了，换个口味..");
            }
        },
        // 指定回调函数在哪个线程执行
        MoreExecutors.directExecutor()
        );
    }

}
