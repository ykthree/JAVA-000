package learn.spring.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component("studentComponent")
//@Service("studentComponent")
//@Repository("studentComponent")
//@Controller("studentComponent")
public class Student {

    private int id;

    private String name;
}
