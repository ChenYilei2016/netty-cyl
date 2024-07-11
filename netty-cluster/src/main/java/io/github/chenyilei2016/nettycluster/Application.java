package io.github.chenyilei2016.nettycluster;

import io.github.chenyilei2016.nettycluster.redis.RedisUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Resource
    private RedisUtil redisUtil;

    public static void main(String[] args) {
        ERedis.startAndFinallyShutdown(() -> {
            SpringApplication.run(Application.class, args);
        });
    }

    @Override
    public void run(String... args) throws Exception {
        redisUtil.clear();
    }

}
