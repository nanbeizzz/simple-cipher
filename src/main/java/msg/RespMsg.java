package msg;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * @author Tanghf
 * @version V1.1
 * @Date 2019/7/4 13:55
 */
public class RespMsg implements Verifiable{

    public static Log logger = LogFactory.getLog(RespMsg.class);
    private static final String SuccessCode = "0000";

    private RespHeader header;
    private String body;
    private Map<String, Object> bodyData;
    private boolean verifyPass = false;

    public RespHeader getHeader() {
        return header;
    }

    public void setHeader(RespHeader header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, Object> getBodyData() {
        return bodyData;
    }

    public void setBodyData(Map<String, Object> bodyData) {
        this.bodyData = bodyData;
    }

    public void setSignData(String signData){
        getHeader().setSignData(signData);
    }

    public boolean getVerifyPass() {
        return verifyPass;
    }

    public void setVerifyPass(boolean verifyPass) {
        this.verifyPass = verifyPass;
    }

    public static boolean isSuccess(RespMsg resp){
        if (resp == null || resp.header == null){
            logger.error("interface failure");
            return false;
        }
        if (!resp.verifyPass){
            logger.error("sign verification failure");
            return false;
        }
        if (!SuccessCode.equals(resp.header.getErrorCode())){
            logger.error("result Code:" + resp.header.getErrorCode());
            logger.error("result Msg:" + resp.header.getErrorMsg());
            return false;
        }
        return true;
    }

    @Override
    public String getSignData(){
        return getHeader().getSignData();
    }

    /** 组装待验证原文：body+requestId+responseId */
    @Override
    public String getMd5Src(){
        return getBody()+getHeader().getRequestId()+getHeader().getResponseId();
    }

    @Override
    public String toString() {
        return "{" +
                "\"header\":" + header + "," +
                "\"body\":\"" + body + "\"" +
                '}';
    }

    public String toLog(){
        return "{\r\n" +
                "\t\"header\":" + header.toLog() + "," + "\r\n" +
                "\t\"body\":\"" + body + "\"" + "\r\n" +
                '}' +
                "\r\n\"bodyData\":" + formatBody(bodyData);
    }

}
