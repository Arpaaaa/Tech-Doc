import java.util.function.Consumer;

public class ConsumerInterfaceService {

    public void doAccept(String name) {
        // 使用Consumer接口来验证输入
        Consumer<String> validator = input -> {
            if (StringUtils.isBlank(input)) {
                throw new IllegalArgumentException("Input cannot be blank");
            }
        };
        // 使用Accept方法来调用
        validator.accept(name);
    }

    public void doAndThen(String name, String email) {
        // 使用Consumer接口来验证输入
        Consumer<String> validator = input -> {
            if (StringUtils.isBlank(input)) {
                throw new IllegalArgumentException("Input cannot be blank");
            }
        };
        // 使用andThen方法来连接两个Consumer接口
        Consumer<String> emailValidator = validator.andThen(input -> {
            if (!input.contains("@")) {
                throw new IllegalArgumentException("Email must contain @");
            }
        });
        // 使用accept方法来调用
        emailValidator.accept(name);
        emailValidator.accept(email);
    }
}
