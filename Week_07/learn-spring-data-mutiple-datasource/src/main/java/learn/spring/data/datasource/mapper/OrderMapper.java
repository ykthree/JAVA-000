package learn.spring.data.datasource.mapper;

import learn.spring.data.datasource.domain.entity.Order;
import learn.spring.data.datasource.routing.DataSourceType;
import learn.spring.data.datasource.routing.DynamicDSContextHolder;
import learn.spring.data.datasource.routing.annotation.DynamicDS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 订单 Mapper
 *
 * @author ykthree
 * 2020/11/29
 */
@Repository
@Slf4j
public class OrderMapper {

    /**
     * 插入语句
     */
    private final String insertSql = "INSERT INTO mydb.t_order_master\n" +
            "(order_sn,\n" +
            "customer_id,\n" +
            "order_status,\n" +
            "create_time,\n" +
            "ship_time,\n" +
            "pay_time,\n" +
            "receive_time,\n" +
            "discount_money,\n" +
            "ship_money,\n" +
            "pay_money,\n" +
            "pay_method,\n" +
            "address,\n" +
            "receive_user,\n" +
            "ship_sn,\n" +
            "ship_company_name)\n" +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("dynamicRoutingDataSource")
    private DataSource dataSource;

    @PostConstruct
    private void init() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 批量插入订单数据
     *
     * @param orders 订单数据列表
     * @return int[]
     */
    @DynamicDS(DataSourceType.PRIMARY)
    public int[] batchInsert(final List<Order> orders) {
        return jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                setOrderValues(ps, orders.get(i));
            }

            @Override
            public int getBatchSize() {
                return orders.size();
            }
        });
    }

    /**
     * 新增订单
     *
     * @param order 订单实体类
     * @return int
     */
    @DynamicDS(DataSourceType.PRIMARY)
    public int insert(Order order) {
        log.info("Current DataSource: [{}]", DynamicDSContextHolder.peek());
        return jdbcTemplate.update(insertSql, ps -> setOrderValues(ps, order));
    }

    /**
     * 根据订单 ID 查询订单
     *
     * @param id 订单 ID
     * @return 订单
     */
    @DynamicDS(DataSourceType.SECONDARY)
    public Order selectById(int id) {
        String selectSql = "select * from mydb.t_order_master where id = ?";
        log.info("Current DataSource: [{}]", DynamicDSContextHolder.peek());
        return jdbcTemplate.queryForObject(selectSql, this::getOrderValues, id);
    }

    /**
     * 保存时，设置订单值
     *
     * @param ps    PreparedStatement
     * @param order 订单实体类
     * @throws SQLException SQLException
     */
    private void setOrderValues(PreparedStatement ps, Order order) throws SQLException {
        ps.setString(1, order.getOrderSn());
        ps.setInt(2, order.getCustomerId());
        ps.setShort(3, order.getOrderStatus());
        ps.setDate(4, new java.sql.Date(order.getCreateTime().getTime()));
        ps.setDate(5, new java.sql.Date(order.getShipTime().getTime()));
        ps.setDate(6, new java.sql.Date(order.getPayTime().getTime()));
        ps.setDate(7, new java.sql.Date(order.getReceiveTime().getTime()));
        ps.setBigDecimal(8, order.getDiscountMoney());
        ps.setBigDecimal(9, order.getShipMoney());
        ps.setBigDecimal(10, order.getPayMoney());
        ps.setShort(11, order.getPayMethod());
        ps.setString(12, order.getAddress());
        ps.setString(13, order.getReceiveUser());
        ps.setString(14, order.getShipSn());
        ps.setString(15, order.getShipCompanyName());
    }

    /**
     * 读取时，获取订单值
     */
    private Order getOrderValues(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt(1));
        order.setOrderSn(rs.getString(2));
        order.setCustomerId(rs.getInt(3));
        order.setOrderStatus(rs.getShort(4));
        order.setCreateTime(rs.getDate(5));
        order.setShipTime(rs.getDate(6));
        order.setPayTime(rs.getDate(7));
        order.setReceiveTime(rs.getDate(8));
        order.setDiscountMoney(rs.getBigDecimal(9));
        order.setShipMoney(rs.getBigDecimal(10));
        order.setPayMoney(rs.getBigDecimal(11));
        order.setPayMethod(rs.getShort(12));
        order.setAddress(rs.getString(13));
        order.setReceiveUser(rs.getString(14));
        order.setShipSn(rs.getString(15));
        order.setShipCompanyName(rs.getString(16));
        return order;
    }

}
