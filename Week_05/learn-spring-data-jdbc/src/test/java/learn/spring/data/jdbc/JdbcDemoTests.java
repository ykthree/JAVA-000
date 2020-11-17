package learn.spring.data.jdbc;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JDBC 测试类
 * @author ykthree
 * 2020/11/17 21:51
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcDemoTests {

    @Autowired
    JdbcDemo jdbcDemo;

    @Order(1)
    @Test
    public void testSelectById() {
        Foo foo = jdbcDemo.selectById(1);
        assertNotNull(foo);
        assertEquals(1, foo.getId());
        assertEquals("foo", foo.getName());
    }

    @Order(2)
    @Test
    public void testInsert() {
        Foo foo = new Foo(2, "foo2");
        jdbcDemo.insert(foo);
        assertEquals(foo, jdbcDemo.selectById(2));
    }

    @Order(3)
    @Test
    public void testUpdate() {
        Foo foo = jdbcDemo.selectById(1);
        assertNotNull(foo);
        assertEquals("foo", foo.getName());
        Foo foo1 = new Foo(1, "foo1");
        jdbcDemo.update(foo1);
        assertEquals("foo1", jdbcDemo.selectById(1).getName());
    }

    @Order(4)
    @Test
    public void testDeleteById() {
        assertNotNull(jdbcDemo.selectById(1));
        jdbcDemo.deleteById(1);
        assertNull(jdbcDemo.selectById(1));
    }

    @Order(5)
    @Test
    public void testBatchInsert() {
        List<Foo> fooList = Arrays.asList(new Foo(3, "foo3"), new Foo(4, "foo4"));
        jdbcDemo.batchInsert(fooList);
        assertNotNull(jdbcDemo.selectById(3));
        assertNotNull(jdbcDemo.selectById(4));
    }

    @Order(6)
    @Test
    public void testBatchInsertWithTransaction() {
        // 新增数据主键相同，发生错误回滚
        List<Foo> fooList = Arrays.asList(new Foo(5, "foo5"), new Foo(5, "foo5"));
        jdbcDemo.batchInsertWithTransaction(fooList);
        assertNull(jdbcDemo.selectById(5));
    }

}
