## SpringCloud EurekaServer
当前项目为SpringCloud的组件之一，Eureka，用户服务的注册和发现
坑：版本`bug`，`Srping Cloud Netflix`1.1以下，文档描述，一旦成功服务成功注册并发现之后，不会实时发送服务端信息，在Eureka服务上会一直呈现**Up**的状态，服务下线时，Eureka并不能及时发现，源码也并不提供相关状态发现代码。
新版本文档提到：
> By default, Eureka uses the client heartbeat to determine if a client is up. Unless specified otherwise the
Discovery Client will not propagate the current health check status of the application per the Spring Boot Actuator. Which means that after successful registration Eureka will always announce that the application is in 'UP' state. This behaviour can be altered by enabling Eureka health checks, which results in propagating application status to Eureka. As a consequence every other application won’t be sending traffic to application in state other then 'UP'.
##### `在client端application.yml配置-这部分会让client启动心跳检测，但是这部分的性能影响还没进行测试，留到后面观察`
```java
eureka:
　client:
　　healthcheck:
　　　enable: true
```