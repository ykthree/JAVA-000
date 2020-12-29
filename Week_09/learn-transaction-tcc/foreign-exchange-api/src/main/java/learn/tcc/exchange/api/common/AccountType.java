package learn.tcc.exchange.api.common;

import lombok.Getter;

/**
 * 账户类型
 *
 * @author ykthree
 * 2020/12/28 19:19
 */
@Getter

public enum AccountType {

    /**
     * 美元账户
     */
    DOLLAR(1, ""),

    /**
     * 人名币账户
     */
    CNY(2, "");

    AccountType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final int code;

    private final String desc;

}
