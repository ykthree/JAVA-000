package learn.spring.data.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体类
 *
 * @author ykthree
 * 2020/11/17 21:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Foo {

    private int id;

    private String name;

}
