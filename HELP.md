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


### 存在问题

* limit n

    会出现错误，IO关闭错误

* sum(deciaml)出现过大的现象

    sum的值与直接在mysql执行的结果不一致  
    测试之后发现，在应用中计算后的结果与在mysql中sum结果一致，可见是clickhouse做了一些处理




