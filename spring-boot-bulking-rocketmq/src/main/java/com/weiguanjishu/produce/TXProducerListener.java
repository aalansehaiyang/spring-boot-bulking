package com.weiguanjishu.produce;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author 微信公众号：微观技术
 */
@Component
@RocketMQTransactionListener(txProducerGroup = "tx_order_message")
public class TXProducerListener implements RocketMQLocalTransactionListener {

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        // 执行本地事务
        System.out.println("TXProducerListener 开始执行本地事务。。。");
        RocketMQLocalTransactionState result;
        try {
            // 模拟业务处理（ 如：创建订单 ）

//            int i = 1 / 0;  //模拟异常

            result = RocketMQLocalTransactionState.COMMIT;  // 成功

        } catch (Exception e) {
            System.out.println("本地事务执行失败。。。");
            result = RocketMQLocalTransactionState.ROLLBACK;
        }
        return result;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        // 检查本地事务（ 例如检查下订单是否成功 ）
        System.out.println("检查本地事务。。。");
        RocketMQLocalTransactionState result;
        try {
            //模拟业务处理（ 根据检查结果，决定是COMMIT或ROLLBACK ）
            result = RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            // 异常就回滚
            System.out.println("检查本地事务 error");
            result = RocketMQLocalTransactionState.ROLLBACK;
        }
        return result;
    }

}