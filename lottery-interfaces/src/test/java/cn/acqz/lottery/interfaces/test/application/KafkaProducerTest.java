package cn.acqz.lottery.interfaces.test.application;

import cn.acqz.lottery.application.mq.producer.KafkaProducer;
import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.activity.model.vo.InvoiceVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @description: Kafka 消息测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaProducerTest {

    private Logger logger = LoggerFactory.getLogger(KafkaProducerTest.class);

    @Resource
    private KafkaProducer kafkaProducer;

    @Test
    public void test_send() throws InterruptedException {

        InvoiceVO invoice = new InvoiceVO();
        invoice.setuId("fustack");
        invoice.setOrderId(1444540456057864192L);
        invoice.setAwardId("3");
        invoice.setAwardType(Constants.AwardType.DESC.getCode());
        invoice.setAwardName("Code");
        invoice.setAwardContent("苹果电脑");
        invoice.setShippingAddress(null);
        invoice.setExtInfo(null);

        kafkaProducer.sendLotteryInvoice(invoice);

        while (true){
            Thread.sleep(10000);
        }
    }
}
