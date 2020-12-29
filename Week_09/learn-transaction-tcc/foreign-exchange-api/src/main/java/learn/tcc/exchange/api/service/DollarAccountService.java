package learn.tcc.exchange.api.service;

import learn.tcc.exchange.api.domain.dto.AccountDTO;
import org.dromara.hmily.annotation.Hmily;

/**
 * 美元账户服务
 *
 * @author ykthree
 * 2020/12/28 15:59
 */
public interface DollarAccountService {


    boolean decrease(AccountDTO accountDTO);

    @Hmily
    boolean increase(AccountDTO accountDTO);

}
