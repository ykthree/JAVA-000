package learn.tcc.exchange.entrance.controller;

import learn.tcc.exchange.entrance.service.ForeignExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 外汇交易服务
 *
 * @author ykthree
 * 2020/12/28 16:40
 */
@RestController
@RequestMapping("/exchange")
@Slf4j
public class ForeignExchangeController {

    @Autowired
    private ForeignExchangeService exchangeService;

    @PostMapping(value = "/dollarForCNY")
    public String exchangeDollarForCNY(@RequestParam(value = "amount") BigDecimal amount) {
        final long start = System.currentTimeMillis();
        exchangeService.exchangeDollarForCNY(1, 2, BigDecimal.valueOf(1));
        log.info("消耗时间为: {}", (System.currentTimeMillis() - start));
        return "success";
    }

}
