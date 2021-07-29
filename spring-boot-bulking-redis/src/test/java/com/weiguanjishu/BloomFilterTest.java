package com.weiguanjishu;


import com.alibaba.fastjson.JSON;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BloomFilterTest {

    @Resource
    private RedisTemplate redisTemplate;


    @Test
    public void test1() {

        for (int i = 1000; i >= 1; i--) {
            int finalI = i;
            redisTemplate.executePipelined(new RedisCallback<Long>() {
                @Nullable
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.openPipeline();
                    for (int offset = finalI * 10000 - 1; offset >= ((finalI - 1) * 10000); offset--) {
                        boolean value = offset % 2 == 0 ? true : false;
                        connection.setBit("bloom-filter-data".getBytes(), offset, value);
                    }
                    connection.closePipeline();
                    System.out.println("数据预热批次：" + finalI);
                    return null;
                }
            });
        }

    }


    @Test
    public void test2() {

        redisTemplate.executePipelined(new RedisCallback<Long>() {
            @Nullable
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (int offset = 10000000; offset >= 0; offset--) {
                    boolean value = offset % 2 == 0 ? true : false;
                    connection.setBit("bloom-filter-data-1".getBytes(), offset, value);
                }
                connection.closePipeline();
                return null;
            }
        });
        System.out.println("数据预热完成");
    }

    @Test
    public void test3() {

        List<Object> res1 = redisTemplate.executePipelined(new RedisCallback<Long>() {
            @Nullable
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                connection.getBit("bloom-filter-data".getBytes(), 10000000 - 1);
                connection.getBit("bloom-filter-data".getBytes(), 10000000 - 2);
                connection.getBit("bloom-filter-data".getBytes(), 1);
                connection.getBit("bloom-filter-data".getBytes(), 0);
                return null;
            }
        });

        // 运行结果
        System.out.println(JSON.toJSONString(res1));

    }


    @Test
    public void test4() {

        List<Object> res1 = redisTemplate.executePipelined(new RedisCallback<Long>() {
            @Nullable
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                // 位值为1的总数
                connection.bitCount("bloom-filter-data".getBytes());
                return null;
            }
        });
        // 运行结果
        System.out.println(JSON.toJSONString(res1));

    }

    /**
     * @auth  公众号：微观技术
     */
    @Test
    public void test5() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://172.16.67.37:6379");
        RedissonClient cient = Redisson.create(config);
        RBloomFilter<String> bloomFilter = cient.getBloomFilter("test5-bloom-filter");
        // 初始化布隆过滤器，数组长度100W，误判率 1%
        bloomFilter.tryInit(1000000L, 0.01);
        bloomFilter.add("Tom哥");
        // 判断是否存在
        System.out.println(bloomFilter.contains("微观技术"));
        System.out.println(bloomFilter.contains("Tom哥"));
    }

}
