package learn.tcc.exchange.api.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * AccountDTO
 *
 * @author ykthree
 * 2020/12/28 19:55
 */
@Data
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 6358534320032747870L;

    private Integer userId;

    private BigDecimal amount;

    public AccountDTO() {}

    AccountDTO(Integer userId, BigDecimal amount) {
        this.userId = Objects.requireNonNull(userId, "UserId can not be null!");
        this.amount = Objects.requireNonNull(amount, "Amount can not be null!");;
    }

    public static AccountDTO createAccountDTO(Integer userId, BigDecimal amount) {
        return new AccountDTO(userId, amount);
    }

}
