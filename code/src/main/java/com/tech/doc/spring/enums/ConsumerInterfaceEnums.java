import java.util.function.Consumer;


public enum ConsumerInterfaceEnums {
    
    // 通过枚举来定义不同的消费者以及实际需要执行的动作
    APP_1("app1", "测试app1号", input -> System.out.println("app1号收到消息：" + input)),

    ;


    private String code;

    private String message;

    private Consumer<String> consumer;


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void accept(String input) {
        consumer.accept(input);
    }
}
