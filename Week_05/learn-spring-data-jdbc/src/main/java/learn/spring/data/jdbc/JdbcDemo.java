package learn.spring.data.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 使用原生 JDBC 操作数据库
 *
 * @author ykthree
 * 2020/11/17 21:09
 */
@Repository
@Slf4j
public class JdbcDemo {

    @Value("${spring.datasource.driver}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    private void init() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据 ID 查询
     *
     * @param id id
     * @return Foo
     */
    public Foo selectById(int id) {
        Foo foo = null;
        // 查询语句
        String selectSql = "SELECT ID, NAME FROM FOO WHERE ID = ?";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement prepareStatement = connection.prepareStatement(selectSql);
        ) {
            prepareStatement.setInt(1, id);
            try (ResultSet resultSet = prepareStatement.executeQuery()) {
                while (resultSet.next()) {
                    foo = new Foo();
                    foo.setId(resultSet.getInt(1));
                    foo.setName(resultSet.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foo;
    }

    /**
     * 根据 ID 删除 Foo
     *
     * @param id id
     */
    public void deleteById(int id) {
        // 删除语句
        String deleteSql = "DELETE FROM FOO WHERE ID = ?";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement prepareStatement = connection.prepareStatement(deleteSql);
        ) {
            prepareStatement.setInt(1, id);
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增 Foo
     *
     * @param foo Foo
     */
    public void insert(Foo foo) {
        if (foo == null) {
            return;
        }
        // 新增语句
        String insertSql = "INSERT INTO FOO(ID, NAME) VALUES(?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement prepareStatement = connection.prepareStatement(insertSql);
        ) {
            prepareStatement.setInt(1, foo.getId());
            prepareStatement.setString(2, foo.getName());
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新 Foo
     *
     * @param foo Name
     */
    public void update(Foo foo) {
        if (foo == null) {
            return;
        }
        // 更新语句
        String updateSql = "UPDATE FOO SET NAME = ? WHERE ID = ?";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement prepareStatement = connection.prepareStatement(updateSql);
        ) {
            prepareStatement.setString(1, foo.getName());
            prepareStatement.setInt(2, foo.getId());
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量新增 Foo
     *
     * @param fooList Foo 列表
     */
    public void batchInsert(List<Foo> fooList) {
        if (fooList == null || fooList.isEmpty()) {
            return;
        }
        // 新增语句
        String insertSql = "INSERT INTO FOO(ID, NAME) VALUES(?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement prepareStatement = connection.prepareStatement(insertSql);
        ) {
            // 关闭自动提交
            connection.setAutoCommit(false);
            for (Foo foo : fooList) {
                prepareStatement.setInt(1, foo.getId());
                prepareStatement.setString(2, foo.getName());
                prepareStatement.addBatch();
            }
            prepareStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * rollback
     *
     * @param fooList fooList
     */
    public void batchInsertWithTransaction(List<Foo> fooList) {
        if (fooList == null || fooList.isEmpty()) {
            return;
        }
        // 新增语句
        String insertSql = "INSERT INTO FOO(ID, NAME) VALUES(?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement prepareStatement = connection.prepareStatement(insertSql);
        ) {
            // 关闭自动提交
            connection.setAutoCommit(false);
            try {
                for (Foo foo : fooList) {
                    prepareStatement.setInt(1, foo.getId());
                    prepareStatement.setString(2, foo.getName());
                    prepareStatement.addBatch();
                }
                prepareStatement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                log.error("数据新增错误", e);
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
