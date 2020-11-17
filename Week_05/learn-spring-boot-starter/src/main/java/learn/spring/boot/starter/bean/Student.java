package learn.spring.boot.starter.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

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
