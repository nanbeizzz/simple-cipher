package msg;

import com.alibaba.fastjson.JSON;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Tanghf
 * @version V1.1
 * @Date 2019/7/4 13:54
 */
public class ReqMsg implements Verifiable{

    private ReqHeader header;
    private String body;
    private Map<String, Object> bodyData;

    public ReqMsg() {
        this.header = getHeader();
        this.body = getBody();
        this.bodyData = getBodyData();
    }

    public ReqHeader getHeader() {
        if (header == null){
            return new ReqHeader();
        }
        return header;
    }

    public void setHeader(ReqHeader header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, Object> getBodyData() {
        if (bodyData == null){
            return new LinkedHashMap<>();
        }
        return bodyData;
    }

    public void setBodyData(Map<String, Object> bodyData) {
        this.bodyData = bodyData;
    }

    public boolean isAcquireKeyReq(){
        return bodyData == null || bodyData.isEmpty();
    }

    public void setSignData(String signData){
        getHeader().setSignData(signData);
    }

    @Override
    public String getSignData(){
        return getHeader().getSignData();
    }

    /** 组装待验证原文：body+requestId+requestTime */
    @Override
    public String getMd5Src(){
        return getBody()+getHeader().getRequestId()+getHeader().getRequestTime();
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
