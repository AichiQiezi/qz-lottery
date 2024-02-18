package cn.acqz.lottery.interfaces;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description: LotteryApplication
 */
@SpringBootApplication
@Configurable
@EnableDubbo
@ComponentScan(value = {"cn.acqz.lottery"})
@MapperScan(value = {"cn.acqz.lottery.infrastructure.dao"})
public class LotteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryApplication.class, args);
    }

}
