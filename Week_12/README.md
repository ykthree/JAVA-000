# 作业说明

- Week12
  - 作业 1（必做）：配置 redis 的主从复制，sentinel 高可用，cluster 集群。
    1. config 配置文件，
    2. 启动和操作、验证集群下数据读写的命令步骤。

## 总览

本文介绍了 Redis 高可用配置，包括：
- Redis 主从复制。
- Redis Sentinel。
- Redis Cluster。

本文使用 Redis 版本：6.0.9，配置环境基于 Docker Desktop for Windows(WSL2)。

### Redis 主从复制

1. 编写 docker-compose 启动文件（redis-replica.yml）

    ```yml
    version: "3"
    services:
    redis-master:
        image: redis:6.0.9
        ports: 
        - 6379:6379
        networks:
        - net-redis-replica

    redis-slave:
        image: redis:6.0.9
        command: redis-server --replicaof redis-master 6379
        networks:
        - net-redis-replica
    networks:
    net-redis-replica:
    ```

2. 启动 redis 主从配置（一主三从）

    ```
    docker-compose -f redis-replica.yml up -d --scale redis-slave=3
    ```

3. 验证
   
    登录主库容器，添加设置缓存 name=ykthree。
    ```
    docker exec -it redis_redis-master_1 /bin/bash

    redis-cli

    set name ykthree

    OK
    ```
    登录任意从库，查看缓存
    ```
    docker exec -it redis_redis-slave_3 /bin/bash

    redic-cli

    get anme 

    "ykthree"
    ```
    也可以查看主库状态
    ```
    redis-cli

    info replication

    # Replication
    role:master
    connected_slaves:3
    slave0:ip=172.19.0.3,port=6379,state=online,offset=241,lag=1
    slave1:ip=172.19.0.2,port=6379,state=online,offset=241,lag=1
    slave2:ip=172.19.0.4,port=6379,state=online,offset=241,lag=1
    master_replid:1c0ae13bf2825a9ccb9581f1075ff64030af4bc1
    master_replid2:0000000000000000000000000000000000000000
    master_repl_offset:241
    second_repl_offset:-1
    repl_backlog_active:1
    repl_backlog_size:1048576
    repl_backlog_first_byte_offset:1
    repl_backlog_histlen:241
    ```
    尝试关掉主库，观察从库日志，并且整个主从复制在外部处于不可用状态，因为在使用 Docker 启动时对外只暴露了主库地址。
    ```
    Connecting to MASTER redis-master:6379
    Unable to connect to MASTER: Invalid argument
    ```
    主库恢复后，从库重新连接到主库，整个主从复制对外恢复使用，从库日志如下：
    ```
    redis-slave_1   | 1:S 05 Jan 2021 12:39:22.595 * Connecting to MASTER redis-master:6379
    redis-slave_1   | 1:S 05 Jan 2021 12:39:22.595 * MASTER <-> REPLICA sync started
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.608 * Non blocking connect for SYNC fired the event.
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.608 * Master replied to PING, replication can continue...
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.608 * Trying a partial resynchronization (request e1f773e4e55f37ba92f67104ef3923eaf71d5433:463).
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.610 * Full resync from master: 271eae07f48340517925144666692769e15b3555:0
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.610 * Discarding previously cached master state.
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.754 * MASTER <-> REPLICA sync: receiving 194 bytes from master to disk
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.754 * MASTER <-> REPLICA sync: Flushing old data
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.754 * MASTER <-> REPLICA sync: Loading DB in memory
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.814 * Loading RDB produced by version 6.0.9
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.814 * RDB age 0 seconds
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.814 * RDB memory usage when created 1.83 Mb
    redis-slave_1   | 1:S 05 Jan 2021 12:39:23.814 * MASTER <-> REPLICA sync: Finished with success
    ```
4. 停止 Reids 服务
    ```
    docker-compose -f redis-replica.yml down
    ```

### Redis Sentinel

1. 编写 docker-compose 启动文件

    ```yml
    version: "3"
    services:
    redis-master:
        image: redis:6.0.9
        ports: 
        - 6379:6379
        networks:
        net-redis-sentinel:
            ipv4_address: 192.168.3.24

    redis-slave:
        image: redis:6.0.9
        command: redis-server --replicaof redis-master 6379
        networks:
        - net-redis-sentinel
    
    redis-sentinel_1:
        image: redis:6.0.9
        command: redis-sentinel /redis/sentinel.conf
        ports:
        - "26379:26379"
        volumes:
        - ./conf/sentinel01:/redis/
        networks:
        - net-redis-sentinel

    redis-sentinel_2:
        image: redis:6.0.9
        command: redis-sentinel /redis/sentinel.conf
        ports:
        - "26380:26379"
        volumes:
        - ./conf/sentinel02:/redis/
        networks:
        - net-redis-sentinel

    redis-sentinel_3:
        image: redis:6.0.9
        command: redis-sentinel /redis/sentinel.conf
        ports:
        - "26381:26379"
        volumes:
        - ./conf/sentinel03:/redis/
        networks:
        - net-redis-sentinel

    networks:
    net-redis-sentinel:
        driver: bridge
        ipam:
        config:
            - subnet: "192.168.3.0/24"
    ```
2. 启动

    ```
    docker-compose -f redis-sentinel.yml up -d --scale redis-slave=2
    ```
3. 验证

    查看哨兵状态
    ```
    redis-cli -p 26379

    info sentinel

    # Sentinel
    sentinel_masters:1
    sentinel_tilt:0
    sentinel_running_scripts:0
    sentinel_scripts_queue_length:0
    sentinel_simulate_failure_flags:0
    master0:name=mymaster,status=ok,address=192.168.3.24:6379,slaves=3,sentinels=3
    ```

    关掉主库，查看哨兵状态，发现主库节点已被切换

    ```
    info sentinel

    # Sentinel
    sentinel_masters:1
    sentinel_tilt:0
    sentinel_running_scripts:0
    sentinel_scripts_queue_length:0
    sentinel_simulate_failure_flags:0
    master0:name=mymaster,status=ok,address=192.168.3.4:6379,slaves=3,sentinels=3
    ```

    重启主库，查看原来主库状态，发现主库变成从库。
    ```
    info replication

    # Replication
    role:slave
    master_host:192.168.3.4
    master_port:6379
    master_link_status:up
    master_last_io_seconds_ago:1
    master_sync_in_progress:0
    slave_repl_offset:122290
    slave_priority:100
    slave_read_only:1
    connected_slaves:0
    master_replid:8548715d4bf7001cc4691a8210aca497d04cc4a5
    master_replid2:0000000000000000000000000000000000000000
    master_repl_offset:122290
    second_repl_offset:-1
    repl_backlog_active:1
    repl_backlog_size:1048576
    repl_backlog_first_byte_offset:94960
    repl_backlog_histlen:27331
    ```
4. 停止 Redis
    
    ```
    docker-compose -f redis-sentinel.yml down
    ```

### Redis Cluster

待完成

## 总结

### 参考链接

[1] [gitbook.docker-compose启动redis](https://wychuan.gitee.io/redis/install/docker-compose.html)

[2] [docker.Networking in Compose](https://docs.docker.com/compose/networking/)

[3] [简书.docker-compose搭建redis-sentinel](https://www.jianshu.com/p/eb74ea16b9e9)

[[4] docker.Use volumes](https://docs.docker.com/storage/volumes/)

### 问题记录

- [ ] 在 Docker Desktop for Windows(WSL2) 环境上使用 docker-compose 启动 Redis sentinel 出现 Device or resource busy 问题。











# 学习笔记