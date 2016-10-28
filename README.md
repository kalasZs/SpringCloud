## SpringCloud EurekaServer
当前项目为SpringCloud的组件之一，Eureka，服务调用的注册和发现。
遇到的问题：
- 版本`bug`，`Srping Cloud Netflix`1.1以下
- 默认情况下，Eureka通过客户端发来的心跳包来判断客户端是否在线。
如果你不显式指定，客户端在心跳包中不会包含当前应用的健康数据(由Spring Boot Actuator提供)。
这意味着只要客户端启动时完成了服务注册，那么该客户端在主动注销之前在Eureka中的状态会永远是UP状态。
我们可以通过配置修改这一默认行为，即在客户端发送心跳包时会带上自己的健康信息。这样做的后果是只有当该服务的状态是UP时才能被访问，其它的任何状态都会导致该服务不能被调用。

###### 文档内容：

> By default, Eureka uses the client heartbeat to determine if a client is up.
Unless specified otherwise the Discovery Client will not propagate the current health check status of the application
 per the Spring Boot Actuator.
 Which means that after successful registration Eureka will always announce that the application is in 'UP' state.
 This behaviour can be altered by enabling Eureka health checks, which results in propagating application status to Eureka.
 As a consequence every other application won’t be sending traffic to application in state other then 'UP'.

###### 配置文件
```java
eureka:
　client:
　　healthcheck:
　　　enable: true
```