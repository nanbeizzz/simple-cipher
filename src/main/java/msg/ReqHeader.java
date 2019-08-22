package msg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Tanghf
 * @version V1.1
 * @Date 2019/7/4 13:59
 */
public class ReqHeader {

    /** 应用编号，由合作方申请，银行生成 */
    private String appId;
    private String requestId;
    private String requestTime;
    private String charset;
    private String signData;
    private String reserve;

    public ReqHeader(){
        this.appId = getAppId();
        this.requestId = getRequestId();
        this.requestTime = getRequestTime();
        this.charset = getCharset();
        this.signData = getSignData();
        this.reserve = getReserve();
    };

    public String getRandomNum(int length){
        if (length <= 0){ return ""; }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i<length; i++){
            sb.append(new Random().nextInt(10));
        }
        return sb.toString();
    }

    public String getAppId() {
        if (appId == null){
            return "10002009";
        }
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRequestId() {
        if (requestId == null){
            // TODO
            return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + getRandomNum(6);
        }
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestTime() {
        if (requestTime == null){
            if (requestId == null){
                setRequestId(getRequestId());
                return getRequestTime();
            }else{
                return getRequestId().substring(0,17);
            }
        }
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
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
        if (signData == null){
            return "";
        }
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
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
                "\"requestTime\":\"" + requestTime + "\"" + "," +
                "\"charset\":\"" + charset + "\"" + "," +
                "\"signData\":\"" + signData + "\"" + "," +
                "\"reserve\":\"" + reserve + "\"" +
                "}";
    }

    public String toLog(){
        return "{\r\n" +
                "\t\t\"appId\":\"" + appId + "\"" + "," + "\r\n" +
                "\t\t\"requestId\":\"" + requestId + "\"" + "," + "\r\n" +
                "\t\t\"requestTime\":\"" + requestTime + "\"" + "," + "\r\n" +
                "\t\t\"charset\":\"" + charset + "\"" + "," + "\r\n" +
                "\t\t\"signData\":\"" + signData + "\"" + "," + "\r\n" +
                "\t\t\"reserve\":\"" + reserve + "\"" + "\r\n" +
                "\t}";
    }
}
