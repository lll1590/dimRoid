package com.dim.ke.framework.core.http.handler;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;

/**
 * Description: <ExceptionHandler><br>
 * Author:      mxdl<br>
 * Date:        2019/3/18<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class ExceptionHandler {

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, httpException.code(), "网络错误");
            SYSTEM_ERROR err = SYSTEM_ERROR.getMsgByCode(httpException.code());
            if(err != null){
                ex.code = err.getCode();
                ex.message = err.getMsg();
            }
            return ex;
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException || e instanceof MalformedJsonException) {
            ex = new ResponseThrowable(e, SYSTEM_ERROR.PARSE_ERROR.getCode(), SYSTEM_ERROR.PARSE_ERROR.getMsg());
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, SYSTEM_ERROR.NETWORD_ERROR.getCode(), SYSTEM_ERROR.NETWORD_ERROR.getMsg());
            return ex;
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex = new ResponseThrowable(e, SYSTEM_ERROR.SSL_ERROR.getCode(), SYSTEM_ERROR.SSL_ERROR.getMsg());
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponseThrowable(e, SYSTEM_ERROR.TIMEOUT_ERROR.getCode(), SYSTEM_ERROR.TIMEOUT_ERROR.getMsg());
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, SYSTEM_ERROR.TIMEOUT_ERROR.getCode(), SYSTEM_ERROR.TIMEOUT_ERROR.getMsg());
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new ResponseThrowable(e, SYSTEM_ERROR.TIMEOUT_ERROR.getCode(), SYSTEM_ERROR.TIMEOUT_ERROR.getMsg());
            return ex;
        } else {
            ex = new ResponseThrowable(e, SYSTEM_ERROR.UNKNOWN.getCode(), SYSTEM_ERROR.UNKNOWN.getMsg());
            return ex;
        }

    }

    public enum SYSTEM_ERROR {


        UNAUTHORIZED(401, "操作未授权"),
        FORBIDDEN(403, "请求被拒绝"),
        NOT_FOUND(404, "资源不存在"),
        REQUEST_TIMEOUT(408, "服务器执行超时"),
        INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
        SERVICE_UNAVAILABLE(503, "服务器不可用"),
        HTTP_ERROR(1003, "网络错误"),
        UNKNOWN(1000, "请求被拒绝"),
        PARSE_ERROR(1001, "解析错误"),
        NETWORD_ERROR(1002, "连接失败"),
        SSL_ERROR(1005, "证书错误"),
        TIMEOUT_ERROR(1006, "连接超时");

        private int code;
        private String msg;

        SYSTEM_ERROR(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public static SYSTEM_ERROR getMsgByCode(int code){
            for(SYSTEM_ERROR err : SYSTEM_ERROR.values()){
                if(code == err.getCode()){
                    return err;
                }
            }
            return null;
        }
    }

    public enum APP_ERROR {
        SUCCESS(200, "请求成功");

        private int code;
        private String msg;
        APP_ERROR(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
