package learn.tcc.exchange.dollar.serivce;

import learn.tcc.exchange.api.domain.dto.AccountDTO;
import learn.tcc.exchange.api.mapper.CnyAccountMapper;
import learn.tcc.exchange.api.mapper.DollarAccountMapper;
import learn.tcc.exchange.api.service.DollarAccountService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 人名币账户服务
 *
 * @author ykthree
 * 2020/12/28 16:02
 */
@Service("dollarAccountService")
@Slf4j
public class DollarAccountServiceImpl implements DollarAccountService {

    @Autowired
    private DollarAccountMapper dollarAccountMapper;

    @Override
    @HmilyTCC(confirmMethod = "increaseConfirm", cancelMethod = "increaseCancel")
    public boolean increase(AccountDTO accountDTO) {
        log.info("tcc increase");
        return dollarAccountMapper.increase(accountDTO) > 0;
    }

    /**
     * increaseConfirm
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseConfirm(AccountDTO accountDTO) {
        log.info("tcc increaseConfirm");
        dollarAccountMapper.increaseConfirm(accountDTO);
        return Boolean.TRUE;
    }

    /**
     * increaseCancel
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseCancel(AccountDTO accountDTO) {
        log.info("tcc increaseCancel");
        dollarAccountMapper.increaseCancel(accountDTO);
        return Boolean.TRUE;
    }

    @Override
    @HmilyTCC(confirmMethod = "decreaseConfirm", cancelMethod = "decreaseCancel")
    public boolean decrease(AccountDTO accountDTO) {
        log.info("tcc decrease");
        return dollarAccountMapper.decrease(accountDTO) > 0;
    }

    /**
     * decreaseConfirm
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseConfirm(AccountDTO accountDTO) {
        log.info("tcc decreaseConfirm");
        dollarAccountMapper.decreaseConfirm(accountDTO);
        return Boolean.TRUE;
    }

    /**
     * decreaseCancel
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseCancel(AccountDTO accountDTO) {
        log.info("tcc decreaseCancel");
        dollarAccountMapper.decreaseCancel(accountDTO);
        return Boolean.TRUE;
    }

}
