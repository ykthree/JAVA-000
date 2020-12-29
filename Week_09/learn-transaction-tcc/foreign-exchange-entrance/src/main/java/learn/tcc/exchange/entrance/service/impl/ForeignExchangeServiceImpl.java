package learn.tcc.exchange.entrance.service.impl;

import learn.tcc.exchange.api.domain.dto.AccountDTO;
import learn.tcc.exchange.api.service.CnyAccountService;
import learn.tcc.exchange.api.service.DollarAccountService;
import learn.tcc.exchange.entrance.service.ForeignExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 外汇交易服务
 *
 * @author ykthree
 * 2020/12/28 16:44
 */
@Service
@Slf4j
public class ForeignExchangeServiceImpl implements ForeignExchangeService {

    @Autowired
    private DollarAccountService dollarAccountService;

    @Autowired
    private CnyAccountService cnyAccountService;

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public void exchangeDollarForCNY(Integer fromUser, Integer toUser, BigDecimal amount) {
        // A 用户（fromUser = 1） 用户减去 1 美元
        dollarAccountService.decrease(AccountDTO.createAccountDTO(fromUser, amount));
        // B 用户（toUser = 2） 用户加 1 美元
        dollarAccountService.increase(AccountDTO.createAccountDTO(toUser, amount));
        // A 用户（fromUser = 1） 用户加 7 元
        cnyAccountService.increase(AccountDTO.createAccountDTO(fromUser, BigDecimal.valueOf(7).multiply(amount)));
        // B 用户（toUser = 2） 用户减 7 元
        cnyAccountService.decrease(AccountDTO.createAccountDTO(toUser, BigDecimal.valueOf(7).multiply(amount)));
    }

    public void confirm(Integer fromUser, Integer toUser, BigDecimal amount) {
        log.info("confirm ...");
    }

    public void cancel(Integer fromUser, Integer toUser, BigDecimal amount) {
        log.info("cancel ...");
    }

}
