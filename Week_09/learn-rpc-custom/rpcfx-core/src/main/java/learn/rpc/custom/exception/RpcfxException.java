package learn.rpc.custom.exception;

/**
 * RPC 异常定义
 */
public class RpcfxException extends Exception {

    public RpcfxException(String message) {
        super(message);
    }

    public RpcfxException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcfxException(Throwable cause) {
        super(cause);
    }

    protected RpcfxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
