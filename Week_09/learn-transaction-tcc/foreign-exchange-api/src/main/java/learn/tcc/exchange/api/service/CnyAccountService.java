package learn.tcc.exchange.api.service;

import learn.tcc.exchange.api.domain.dto.AccountDTO;
import org.dromara.hmily.annotation.Hmily;

/**
 * 外汇交易服务
 *
 * @author ykthree
 * 2020/12/28 16:39
 */
public interface CnyAccountService {

    @Hmily
    boolean increase(AccountDTO accountDTO);

    @Hmily
    boolean decrease(AccountDTO accountDTO);

}
