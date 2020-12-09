package learn.spring.data.sharding.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类
 *
 * @author ykthree
 * 2020/11/29
 */
@Data
public class Order {

    private Integer id;

    private String orderSn;

    private Integer customerId;

    private Short orderStatus;

    private Date createTime;

    private Date shipTime;

    private Date payTime;

    private Date receiveTime;

    private BigDecimal discountMoney;

    private BigDecimal shipMoney;

    private BigDecimal payMoney;

    private Short payMethod;

    private String address;

    private String receiveUser;

    private String shipSn;

    private String shipCompanyName;

}
