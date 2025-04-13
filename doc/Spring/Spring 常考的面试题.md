# Spring 常考面试题

## 1. Spring 基础

### 1.1 什么是Spring框架？Spring框架有哪些主要特性？
Spring是一个开源的轻量级Java开发框架，旨在简化企业级应用开发。其主要特性包括：
- **IoC容器**：管理对象的创建、配置和生命周期
- **AOP编程**：面向切面编程，允许将横切关注点如日志、事务等从业务逻辑中分离
- **MVC框架**：用于构建Web应用
- **事务管理**：提供统一的事务管理接口
- **JDBC抽象层**：简化JDBC操作
- **集成支持**：易于与其他框架集成（Hibernate, MyBatis等）
- **测试支持**：支持单元测试和集成测试

### 1.2 什么是IOC(控制反转)？
IOC是一种设计原则，将传统上由应用程序代码执行的控制流程交给外部容器或框架。在Spring中，IoC容器负责创建对象、配置它们并管理它们的生命周期，而不是由应用程序代码直接创建和管理。

### 1.3 什么是依赖注入(DI)？
依赖注入是IoC的一种实现方式，它允许容器在运行时将依赖项注入到对象中，而不是在编译时由对象自己创建或查找依赖项。这促进了松耦合和更易于测试的代码。

### 1.4 Spring Bean的作用域有哪些？
Spring支持以下几种Bean作用域：
- **singleton**：默认作用域，每个容器只有一个Bean实例
- **prototype**：每次请求都会创建一个新的Bean实例
- **request**：每个HTTP请求都有一个Bean实例（在Web应用中有效）
- **session**：每个HTTP会话都有一个Bean实例（在Web应用中有效）
- **global-session**：在全局HTTP会话中的单例（在portlet应用中有效）

### 1.5 Spring提供了哪些类型的依赖注入？
Spring主要支持两种类型的依赖注入：
- **构造函数注入**：通过构造函数提供依赖
- **Setter注入**：通过setter方法提供依赖
- **注解注入**：通过@Autowired注解提供依赖


## 2. Spring MVC

### 2.1 什么是Spring MVC？
Spring MVC是Spring框架的一个模块，提供了基于模型-视图-控制器(MVC)设计模式的Web框架。它有助于创建灵活且松耦合的Web应用程序。

### 2.2 Spring MVC的工作流程是什么？
1. 客户端发送请求到DispatcherServlet
2. DispatcherServlet根据处理器映射（HandlerMapping）选择合适的控制器
3. 控制器处理请求并返回ModelAndView对象
4. DispatcherServlet根据视图解析器（ViewResolver）查找实际的视图
5. 视图渲染结果返回给客户端

### 2.3 什么是DispatcherServlet？
DispatcherServlet是Spring MVC的核心组件，作为前端控制器接收所有请求并协调它们的处理过程。它负责将请求路由到正确的控制器，然后将处理结果发送回客户端。

### 2.4 @Controller注解的作用是什么？
@Controller注解用于标识一个类作为Spring MVC的控制器，负责处理用户请求并返回响应。它是@Component的特化版本，专用于Web层组件。

## 3. Spring Boot

### 3.1 什么是Spring Boot？它有哪些优点？
Spring Boot是Spring框架的扩展，简化了Spring应用的初始化和开发过程。主要优点包括：
- 自动配置：消除了大量手动配置的需要
- 起步依赖：简化依赖管理
- 内嵌服务器：无需部署WAR文件
- Actuator：提供生产就绪的监控和管理功能
- 无代码生成和XML配置：减少样板代码

### 3.2 @SpringBootApplication注解的作用是什么？
@SpringBootApplication是一个便捷注解，等同于同时使用以下三个注解：
- **@Configuration**：表明该类是一个配置类
- **@EnableAutoConfiguration**：启用Spring Boot的自动配置机制
- **@ComponentScan**：启用组件扫描

### 3.3 Spring Boot如何实现自动配置？
Spring Boot通过以下机制实现自动配置：
1. @EnableAutoConfiguration注解触发自动配置
2. spring.factories文件中列出自动配置类
3. 条件注解（如@ConditionalOnClass）决定配置是否应用
4. 属性绑定允许通过application.properties/yaml自定义配置

## 4. Spring AOP

### 4.1 什么是AOP？
AOP（面向切面编程）是一种编程范式，允许通过将横切关注点（如日志记录、事务管理、安全等）与业务逻辑分离来提高模块化。它允许在不修改核心业务代码的情况下添加功能。

### 4.2 Spring AOP中的关键概念是什么？
- **切面(Aspect)**：横切关注点的模块化
- **连接点(Join Point)**：程序执行期间的特定点
- **通知(Advice)**：在特定连接点执行的动作
- **切入点(Pointcut)**：匹配连接点的表达式
- **引入(Introduction)**：向现有类添加新方法或属性
- **织入(Weaving)**：将切面与应用程序连接创建通知对象的过程

## 5. Spring事务管理

### 5.1 Spring支持哪些事务管理类型？
Spring支持两种类型的事务管理：
- **编程式事务管理**：在代码中显式管理事务
- **声明式事务管理**：通过配置分离事务管理和业务代码，通常使用@Transactional注解

### 5.2 什么是事务？为什么需要事务？
事务是一组操作的集合，这些操作要么全部成功，要么全部失败。事务具有ACID特性：
- **原子性(Atomicity)**：事务是不可分割的最小操作单元，要么全部成功，要么全部失败
- **一致性(Consistency)**：事务执行前后，数据库从一个一致性状态转换到另一个一致性状态
- **隔离性(Isolation)**：并发执行的事务彼此隔离，不会互相干扰
- **持久性(Durability)**：一旦事务提交，其所做的修改将永久保存在数据库中

### 5.3 @Transactional注解的作用是什么？
@Transactional注解用于声明式事务管理，可以标注在类或方法上，表示该类/方法中的代码需要在事务环境中执行。它由Spring的AOP功能实现，在方法调用前后自动开启、提交或回滚事务。

### 5.4 事务传播行为有哪些？
Spring定义了7种事务传播行为，控制事务如何在方法调用间传播：
- **REQUIRED（默认）**：如果当前存在事务，则加入该事务；如果不存在，则创建新事务
- **SUPPORTS**：如果当前存在事务，则加入该事务；如果不存在，则以非事务方式执行
- **MANDATORY**：如果当前存在事务，则加入该事务；如果不存在，则抛出异常
- **REQUIRES_NEW**：创建新事务，如果当前存在事务，则挂起当前事务
- **NOT_SUPPORTED**：以非事务方式执行，如果当前存在事务，则挂起当前事务
- **NEVER**：以非事务方式执行，如果当前存在事务，则抛出异常
- **NESTED**：如果当前存在事务，则创建一个嵌套事务；如果不存在，则创建新事务

### 5.5 事务的隔离级别有哪些？
Spring支持以下事务隔离级别：
- **DEFAULT**：使用数据库默认的隔离级别
- **READ_UNCOMMITTED**：读未提交，允许读取未提交的数据（可能出现脏读、不可重复读、幻读）
- **READ_COMMITTED**：读已提交，只允许读取已提交的数据（可能出现不可重复读、幻读）
- **REPEATABLE_READ**：可重复读，确保在一个事务中多次读取的数据一致（可能出现幻读）
- **SERIALIZABLE**：串行化，最高隔离级别，完全串行执行（性能最低）

### 5.6 什么是脏读、不可重复读和幻读？
- **脏读**：一个事务读取了另一个事务未提交的数据
- **不可重复读**：一个事务内多次读取同一数据集，但由于其他事务对该数据的修改，导致两次读取的结果不同
- **幻读**：一个事务内多次查询某个范围的记录数量，由于其他事务插入或删除了该范围内的记录，导致两次查询的结果不同

### 5.7 Spring事务管理的实现原理是什么？
Spring事务管理基于AOP（面向切面编程）实现：
1. 当使用@Transactional注解标记方法或类时，Spring创建该类的动态代理
2. 代理拦截对事务方法的调用，在方法执行前后执行事务操作
3. 代理使用事务管理器（如PlatformTransactionManager）来控制事务
4. 事务管理器负责开启、提交或回滚事务，实际上是管理数据库连接的autoCommit属性

### 5.8 在Spring中如何处理事务的嵌套问题？
处理嵌套事务主要依赖于事务的传播行为：
- 使用REQUIRED传播行为时，内外方法使用相同的事务，内部方法抛出异常会导致外部方法也回滚
- 使用REQUIRES_NEW时，内部方法创建新事务，与外部事务隔离，互不影响
- 使用NESTED时，内部方法创建嵌套事务（savepoint），内部方法异常不会影响外部事务，但外部事务异常会导致内部事务也回滚

### 5.9 @Transactional注解的常用属性有哪些？
@Transactional注解包含以下常用属性：
- **propagation**：事务传播行为
- **isolation**：事务隔离级别
- **timeout**：事务超时时间
- **readOnly**：是否为只读事务
- **rollbackFor/rollbackForClassName**：触发回滚的异常类
- **noRollbackFor/noRollbackForClassName**：不触发回滚的异常类

### 5.10 什么是事务管理器？Spring提供了哪些常用的事务管理器？
事务管理器是Spring事务架构的核心，负责管理事务的开启、提交和回滚。Spring提供了多种事务管理器实现：
- **DataSourceTransactionManager**：适用于使用JDBC或MyBatis的应用
- **JpaTransactionManager**：适用于使用JPA的应用
- **HibernateTransactionManager**：适用于使用Hibernate的应用
- **JtaTransactionManager**：适用于分布式事务管理

### 5.11 @Transactional注解在什么情况下会失效？
@Transactional注解在以下情况下会失效：
- 注解应用于非public方法
- 同一个类中的方法调用（自调用），由于Spring AOP的代理机制，不会触发事务
- 未被Spring容器管理的类
- 数据库不支持事务
- 事务管理器配置错误
- 异常被捕获且未重新抛出（针对运行时异常的默认回滚行为）

### 5.12 Spring事务默认回滚的异常类型是什么？如何自定义回滚异常？
默认情况下，Spring事务仅在遇到运行时异常（RuntimeException及其子类）和Error时才会回滚，而遇到受检查异常（Checked Exception）不会回滚。

可以通过@Transactional注解的rollbackFor属性自定义回滚的异常类型：
```java
@Transactional(rollbackFor = Exception.class) // 所有异常都回滚
public void someMethod() {
    // 方法实现
}
```

也可以通过noRollbackFor属性指定不触发回滚的异常：
```java
@Transactional(noRollbackFor = IllegalArgumentException.class)
public void someMethod() {
    // 方法实现
}
```

### 5.13 如何在Spring Boot中配置事务管理？
Spring Boot自动配置了事务管理，只需在应用类或配置类上添加@EnableTransactionManagement注解，然后在需要事务的方法或类上使用@Transactional注解。

如果需要自定义事务管理器，可以配置如下：
```java
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```
