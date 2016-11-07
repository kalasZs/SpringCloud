## Eureka
- ### 配置 
  `eureka.environment: xx`
  
  [参考文档](https://github.com/Netflix/eureka/wiki/Configuring-Eureka)
- ### DataCenter的配置
  `eureka.datacenter: cloud`
 
  [参考文档](https://github.com/Netflix/eureka/wiki/Configuring-Eureka)
 >配置-Deureka.datacenter=cloud，这样eureka将会知道是在AWS云上
  
- ### 解决注册速度慢问题
  `eureka.instance.leaseRenewalIntervalInSeconds`
  
  [参考文档](http://cloud.spring.io/spring-cloud-static/Camden.SR1/#_why_is_it_so_slow_to_register_a_service)
  
  原文：
>Why is it so Slow to Register a Service?
Being an instance also involves a periodic heartbeat to the registry (via the client’s serviceUrl) with default duration 30 seconds. A service is not available for discovery by clients until the instance, the server and the client all have the same metadata in their local cache (so it could take 3 heartbeats). You can change the period using eureka.instance.leaseRenewalIntervalInSeconds and this will speed up the process of getting clients connected to other services. In production it’s probably better to stick with the default because there are some computations internally in the server that make assumptions about the lease renewal period.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;翻译：
 >作为实例还涉及到与注册中心的周期性心跳，默认持续时间为30秒（通过serviceUrl）。在实例、服务器、客户端都在本地缓存中具有相同的元数据之前，服务不可用于客户端发现（所以可能需要3次心跳）。你可以使用eureka.instance.leaseRenewalIntervalInSeconds 配置，这将加快客户端连接到其他服务的过程。在生产中，最好坚持使用默认值，因为在服务器内部有一些计算，他们对续约做出假设。

- ### 如何解决Eureka Server不踢出已关停的节点的问题
 
`server`端:
</br>eureka.server.enable-self-preservation			（设为false，关闭自我保护主要）
eureka.server.eviction-interval-timer-in-ms     清理间隔（单位毫秒，默认是60*1000）

`client`端：
</br>eureka.client.healthcheck.enabled = true
</br>开启健康检查（需要spring-boot-starter-actuator依赖）
</br>eureka.instance.lease-renewal-interval-in-seconds =10
</br>租期更新时间间隔（默认30秒）
</br>eureka.instance.lease-expiration-duration-in-seconds =30 
</br>租期到期时间（默认90秒）

<strong>
注意：更改Eureka更新频率将打破服务器的自我保护功能</br>
https://github.com/spring-cloud/spring-cloud-netflix/issues/373
</strong>
#### Eureka开启自我保护的提示
<p style='color:red'>EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE. </p>

- ### Eureka配置instanceId显示IP
```java
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: true
    instance-id: ${spring.cloud.client.ipAddress}:${server.port}
```

- ### Eureka配置最佳实践总结
https://github.com/spring-cloud/spring-cloud-netflix/issues/203

- ### 个人发现

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



## Ribbon & Feign

<p>基于`HTTP`和`TCP`客户端的负载均衡器,可以在通过客户端中配置的`ribbonServerList`服务端列表去轮询访问以达到均衡负载的作用。</p>
<p>`Feign`是一个声明式的`Web Service`客户端，它使得编写`Web
Serivce`客户端变得更加简单。</br>我们只需要使用`Feign`来创建一个接口并用注解来配置它既可完成。它具备可插拔的注解支持，包括`Feign`
注解和`JAX-RS`注解。`Feign`也支持可插拔的编码器和解码器。</br>`Spring Cloud`为`Feign`增加了对`Spring
MVC`注解的支持，还整合了`Ribbon`和`Eureka`来提供均衡负载的HTTP客户端实现
> 当前Demo中。ribbon保持轮训的方式访问每个client,而feign并非按照轮训的方式,具体原因待查证