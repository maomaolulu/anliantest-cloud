## 简单介绍
~~~
本系统基于Spring Boot、Spring Cloud & Alibaba、Vue、Element进行前后端架构设计
技术选型
1、系统环境

Java EE 8
Servlet 3.0
Apache Maven 3
2、主框架

Spring Boot 2.3.x
Spring Cloud Hoxton.SR9
Spring Framework 5.2.x
Spring Security 5.2.x
3、持久层

Apache MyBatis 3.5.x
Hibernate Validation 6.0.x
Alibaba Druid 1.2.x
4、视图层

Vue 2.6.x
Axios 0.21.0
Element 2.14.x
~~~
## 系统模块
~~~
com.anliantest     
├── anliantest-ui              // 前端框架 [80]
├── anliantest-gateway         // 网关模块 [8080]
├── anliantest-auth            // 认证中心 [9200]
├── anliantest-api             // 接口模块
│       └── anliantest-api-system                          // 系统接口
│       └── anliantest-api-data                            // 基础数据服务接口
│       └── anliantest-api-file                            // 文件服务接口
│       └── anliantest-api-message                         // 消息服务接口
│       └── anliantest-api-project                         // 项目服务接口
│       └── anliantest-api-third                           // 第三方对接服务接口
│       └── anliantest-api-rocketmq                        // rocketmq服务接口
│       └── anliantest-api-websocket                       // websocket服务接口
├── anliantest-common          // 通用模块
│       └── anliantest-common-core                         // 核心模块
│       └── anliantest-common-datascope                    // 权限范围
│       └── anliantest-common-datasource                   // 多数据源
│       └── anliantest-common-log                          // 日志记录
│       └── anliantest-common-redis                        // 缓存服务
│       └── anliantest-common-seata                        // 分布式事务
│       └── anliantest-common-security                     // 安全模块
│       └── anliantest-common-swagger                      // 系统接口
├── anliantest-modules         // 业务模块
│       └── anliantest-system                              // 系统模块 [9201]
│       └── anliantest-gen                                 // 代码生成 [9202]
│       └── anliantest-job                                 // 定时任务 [9203]
│       └── anliantest-file                                // 文件服务 [9300]
│       └── anliantest-data                                // 数据基础服务（物质库、设备库） [9301]
│       └── anliantest-third                               // 第三方对接服务[9302]
│       └── anliantest-message                             // 消息服务[9303]
│       └── anliantest-project                             // 项目服务[9304](客户、合同、项目)
├── anliantest-visual          // 图形化管理模块
│       └── anliantest-visual-monitor                      // 监控中心 [9100]
├── anliantest-rocketmq          //rocketmq模块             //rocket模块[9208]
├── anliantest-websocket                                   //即时通讯websocket模块[9209]
│                        
├──pom.xml                // 公共依赖
~~~
