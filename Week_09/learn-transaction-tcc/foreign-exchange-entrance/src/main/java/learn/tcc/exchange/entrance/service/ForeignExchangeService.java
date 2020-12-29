package learn.tcc.exchange.entrance.service;

import org.dromara.hmily.annotation.Hmily;

import java.math.BigDecimal;

/**
 * 外汇交易服务
 *
 * @author ykthree
 * 2020/12/28 20:05
 */
public interface ForeignExchangeService {

    /**
     * 美元换人名币
     *
     * @param fromUser 交易方
     * @param toUser   被交易方
     * @param amount   交易金额
     */
    void exchangeDollarForCNY(Integer fromUser, Integer toUser, BigDecimal amount);

}
