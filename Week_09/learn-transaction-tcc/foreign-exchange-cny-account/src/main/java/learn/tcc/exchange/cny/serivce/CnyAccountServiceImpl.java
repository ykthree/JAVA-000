package learn.tcc.exchange.cny.serivce;

import learn.tcc.exchange.api.domain.dto.AccountDTO;
import learn.tcc.exchange.api.mapper.CnyAccountMapper;
import learn.tcc.exchange.api.service.CnyAccountService;
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
@Service("cnyAccountService")
@Slf4j
public class CnyAccountServiceImpl implements CnyAccountService {

    @Autowired
    private CnyAccountMapper cnyAccountMapper;

    @Override
    @HmilyTCC(confirmMethod = "increaseConfirm", cancelMethod = "increaseCancel")
    public boolean increase(AccountDTO accountDTO) {
        return cnyAccountMapper.increase(accountDTO) > 0;
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
        cnyAccountMapper.increaseConfirm(accountDTO);
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
        cnyAccountMapper.increaseCancel(accountDTO);
        return Boolean.TRUE;
    }

    @Override
    @HmilyTCC(confirmMethod = "decreaseConfirm", cancelMethod = "decreaseCancel")
    public boolean decrease(AccountDTO accountDTO) {
        return cnyAccountMapper.decrease(accountDTO) > 0;
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
        cnyAccountMapper.decreaseConfirm(accountDTO);
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
        cnyAccountMapper.decreaseCancel(accountDTO);
        return Boolean.TRUE;
    }

}
