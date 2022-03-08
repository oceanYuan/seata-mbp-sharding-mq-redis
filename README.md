# SEATA整合sharding-jdbc思路

​ 这里主要是参考了ShardingSphere官网的整合思路

​
参考连接：https://shardingsphere.apache.org/document/legacy/4.x/document/cn/features/transaction/principle/base-transaction-seata/

​ 具体内容如下：

​ 整合`Seata AT`事务时，需要把TM，RM，TC的模型融入到ShardingSphere
分布式事务的SPI的生态中。在数据库资源上，Seata通过对接DataSource接口，让JDBC操作可以同TC进行RPC通信。同样，ShardingSphere也是面向DataSource接口对用户配置的物理DataSource进行了聚合，因此把物理DataSource二次包装为Seata
的DataSource后，就可以把Seata AT事务融入到ShardingSphere的分片中。

version 1: 配置druid连接池,配置双主双从模式,配置读写分离,配置路由分片规则,新增redis,redisson.
TODO:kafka 3.0(惊闻kafka3.0摒弃zk,good news) 若kafka不兼容jdk8 则转向rockermq 配置双从,解决削峰填谷,解决线程等待问题.
