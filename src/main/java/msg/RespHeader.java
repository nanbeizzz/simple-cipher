package msg;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tanghf
 * @version V1.1
 * @Date 2019/7/4 13:59
 */
public class RespHeader {

    /** 应用编号，由合作方申请，银行生成 */
    private String appId;
    private String requestId;
    private String responseId;
    private String responseTime;
    private String charset;
    private String signData;
    private String errorCode;
    private String errorMsg;
    private String subCode;
    private String subMsg;
    private String reserve;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResponseTime() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getCharset() {
        if (charset == null){
            return "UTF-8";
        }
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSignData() {
       /* if (signData == null){
            return "";
        }*/
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getReserve() {
        if (reserve == null){
            return "";
        }
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    @Override
    public String toString() {
        return "{" +
                "\"appId\":\"" + appId + "\"" + "," +
                "\"requestId\":\"" + requestId + "\"" + "," +
                "\"responseId\":\"" + responseId + "\"" + "," +
                "\"responseTime\":\"" + responseTime + "\"" + "," +
                "\"charset\":\"" + charset + "\"" + "," +
                "\"signData\":\"" + signData + "\"" + "," +
                "\"errorCode\":\"" + errorCode + "\"" + "," +
                "\"errorMsg\":\"" + errorMsg + "\"" + "," +
                "\"subCode\":\"" + subCode + "\"" + "," +
                "\"subMsg\":\"" + subMsg + "\"" + "," +
                "\"reserve\":\"" + reserve + "\"" +
                "}";
    }

    public String toLog(){
        return "{\r\n" +
                "\t\t\"appId\":\"" + appId + "\"" + "," + "\r\n" +
                "\t\t\"requestId\":\"" + requestId + "\"" + "," + "\r\n" +
                "\t\t\"responseId\":\"" + responseId + "\"" + "," + "\r\n" +
                "\t\t\"responseTime\":\"" + responseTime + "\"" + "," + "\r\n" +
                "\t\t\"charset\":\"" + charset + "\"" + "," + "\r\n" +
                "\t\t\"signData\":\"" + signData + "\"" + "," + "\r\n" +
                "\t\t\"errorCode\":\"" + errorCode + "\"" + "," + "\r\n" +
                "\t\t\"errorMsg\":\"" + errorMsg + "\"" + "," + "\r\n" +
                "\t\t\"subCode\":\"" + subCode + "\"" + "," + "\r\n" +
                "\t\t\"subMsg\":\"" + subMsg + "\"" + "," + "\r\n" +
                "\t\t\"reserve\":\"" + reserve + "\"" + "\r\n" +
                "\t}";
    }
}
