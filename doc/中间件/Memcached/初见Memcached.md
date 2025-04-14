# Memcached详解

## 1. 基本概念

Memcached是一个高性能的分布式内存对象缓存系统，最初由Danga Interactive开发，用于动态Web应用程序以减轻数据库负载。它通过在内存中缓存数据和对象来减少对外部数据源（如数据库或API）的读取次数，从而提高动态Web应用程序的速度和扩展性。

## 2. 核心特性

1. **高性能**：纯内存操作，单个实例可以处理数十万QPS（每秒查询数）
2. **简单的键值存储**：基于简单的键值对存储，支持存储字符串和序列化对象
3. **分布式架构**：支持多服务器横向扩展，可以构建大型缓存集群
4. **内置过期机制**：支持为每个数据项设置TTL（生存时间）
5. **无持久化**：纯内存存储，重启后数据会丢失
6. **LRU算法**：当内存不足时，使用最近最少使用算法自动删除不活跃的数据

## 3. 工作原理

### 3.1 内存管理
#### 3.1.1 使用slab allocation机制管理内存

Slab allocation是一种预分配内存的内存分配方式，主要为了减少内存碎片。其核心概念主要有
1. Page
   
   Page可以理解为一块固定大小的内存空间，是内存分配的基本单元，可以用来存储多个Chunk

2. Slab Class

    Slab Class是多个Chunk的集合，每个slab用来管理特定大小范围的数据，不同的slab具有不同的chunk大小

3. Chunk

    Chunk是实际用来存储数据的空间，其大小根据增长因子来增长

```ascii
+----------------+ 
|     Page      |
|  +--------+   |
|  | Chunk  |   |
|  +--------+   |
|  | Chunk  |   |
|  +--------+   |
|  | Chunk  |   |
|  +--------+   |
+----------------+
```

#### 3.1.2 
- 将内存分成多个slab，每个slab包含多个chunk
- 不同大小的chunk用于存储不同大小的数据
- 避免内存碎片问题

### 3.2 数据分布
- 使用一致性哈希算法将数据分布在多个节点
- 支持虚拟节点，提高数据分布均匀性
- 客户端负责数据分片和路由

### 3.3 过期机制
- 惰性清理：读取时检查数据是否过期
- 定期批量清理：后台线程定期清理过期数据
- 显式删除：支持手动删除数据

## 4. 主要命令

### 4.1 基本操作
```bash
# 存储命令
set key flags exptime bytes [noreply]
add key flags exptime bytes [noreply]
replace key flags exptime bytes [noreply]
append key flags exptime bytes [noreply]
prepend key flags exptime bytes [noreply]

# 读取命令
get key
gets key

# 删除命令
delete key [noreply]

# 计数命令
incr key value [noreply]
decr key value [noreply]

# 统计命令
stats
stats items
stats slabs
```

### 4.2 参数说明
- `flags`：客户端用来存储数据的标志
- `exptime`：过期时间（秒）
- `bytes`：数据字节数
- `noreply`：可选参数，表示不需要服务器响应

## 5. 使用建议

### 5.1 适用场景
1. **减轻数据库负载**：缓存频繁访问的数据
2. **会话存储**：存储用户会话信息
3. **API结果缓存**：缓存第三方API调用结果
4. **页面片段缓存**：缓存HTML片段
5. **计数器**：实现高性能计数器
6. **热点数据缓存**：缓存热门商品、文章等

### 5.2 最佳实践
1. **合理设置过期时间**：根据数据更新频率设置合适的TTL
2. **避免大对象**：单个对象建议不超过1MB
3. **使用批量操作**：减少网络往返次数
4. **监控内存使用**：定期检查内存使用情况
5. **实现故障转移**：配置多个Memcached实例
6. **使用连接池**：复用连接，提高性能

### 5.3 注意事项
1. 不要存储关键业务数据
2. 注意缓存穿透问题
3. 考虑缓存雪崩问题
4. 合理设置内存大小
5. 注意并发访问问题

## 6. 实际应用案例

### 6.1 用户会话存储
```java
// 存储会话
memcachedClient.set("session:" + sessionId, 3600, userSession);

// 获取会话
UserSession session = (UserSession) memcachedClient.get("session:" + sessionId);
```

### 6.2 商品信息缓存
```java
// 缓存商品信息
public Product getProduct(String productId) {
    String key = "product:" + productId;
    Product product = (Product) memcachedClient.get(key);
    
    if (product == null) {
        product = productDao.getById(productId);
        if (product != null) {
            memcachedClient.set(key, 3600, product);
        }
    }
    
    return product;
}
```

### 6.3 计数器实现
```java
// 增加计数
public long incrementCounter(String key) {
    return memcachedClient.incr(key, 1);
}

// 减少计数
public long decrementCounter(String key) {
    return memcachedClient.decr(key, 1);
}
```

### 6.4 分布式锁
```java
public boolean acquireLock(String lockKey, int expireTime) {
    return memcachedClient.add(lockKey, expireTime, "locked");
}

public void releaseLock(String lockKey) {
    memcachedClient.delete(lockKey);
}
```

## 7. 与Redis的对比

| 特性 | Memcached | Redis |
|------|-----------|-------|
| 数据结构 | 仅键值对 | 多种数据结构 |
| 持久化 | 不支持 | 支持 |
| 复制 | 不支持 | 支持 |
| 事务 | 不支持 | 支持 |
| 内存管理 | Slab分配 | 动态分配 |
| 集群 | 客户端分片 | 原生支持 |

## 8. 总结

Memcached是一个简单高效的内存缓存系统，特别适合需要高性能、简单键值存储的场景。虽然功能相对简单，但在正确的使用场景下，它可以显著提高应用程序的性能和可扩展性。选择合适的缓存策略和配置参数，可以最大化Memcached的价值。
