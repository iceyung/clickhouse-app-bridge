# 快速开始

### 介绍

clickhouse-jdbc-bridge的另一种实现，主要是用熟悉的架构完成，便于后续的改造。
继续更新中

* 支持的数据源

Mysql（项目已包含该驱动）

...持续更新中


## 支持的数据类型

* app-web-jdbc模块：

| JDBC data type | ClickHouse data type |
|----------------|----------------------|
| INTEGER        | Int32 |
| VARCHAR        | String |
| DECIMAL    | Decimal128(10) |

...持续添加中


# 帮助

### 接口参考

* 接口127.0.0.1:9019/ping

  GET
  REQUEST: ``` ```
  RESPONSE: ```Ok.```

* 接口127.0.0.1:9019/identifier_quote

  POST

* 接口127.0.0.1:9019/columns_info

  POST

* 接口127.0.0.1:9019

  POST


### clickhouse-jdbc-bridge存在问题

* limit n

    会出现错误，IO关闭错误

* sum(deciaml)出现过大的现象

    sum的值与直接在mysql执行的结果不一致
    测试之后发现，在应用中计算后的结果与在mysql中sum结果一致，可见是clickhouse做了一些处理

### 参考

[1] https://github.com/cicadasmile/data-manage-parent

[2] https://github.com/clickhouse/clickhouse-jdbc-bridge
