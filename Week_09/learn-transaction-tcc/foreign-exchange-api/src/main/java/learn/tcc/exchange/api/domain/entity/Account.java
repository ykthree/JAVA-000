package learn.tcc.exchange.api.domain.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 人名币账户实体类
 *
 * @author ykthree
 * 2020/12/28 15:44
 */
@Data
public class Account {

    /**
     * 主键 ID
     */
    private Integer id;

    /**
     * 用户 ID
     */
    private Integer userId;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 冻结余额
     */
    private BigDecimal freezeAmount;

}
