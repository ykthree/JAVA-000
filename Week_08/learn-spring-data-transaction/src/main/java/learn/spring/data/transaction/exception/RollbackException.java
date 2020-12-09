package learn.spring.data.transaction.exception;

/**
 * 自定义回滚异常
 *
 * @author ykthree
 * 2020/12/8 23:05
 */
public class RollbackException extends Exception {

    public RollbackException() {
        super();
    }

    public RollbackException(String message) {
        super(message);
    }

    public RollbackException(String message, Throwable cause) {
        super(message, cause);
    }

    public RollbackException(Throwable cause) {
        super(cause);
    }

}
