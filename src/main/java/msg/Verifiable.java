package msg;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @author tanghf
 * @createTime 2019/7/17 11:31
 */
public interface Verifiable {

    /** 获得签名数据 */
    String getSignData();

    /** 组装待验证原文 */
    String getMd5Src();

    /**
     * 便于查找日志，不影响交易
     * 优化：格式替换规则
     * */
    default String formatBody(Map bodyData){
        if (bodyData.isEmpty()){
            return "{}";
        }
        try {
            String bd = JSON.toJSONString(bodyData);
            bd = bd.replace("{", "{\r\n\t")
                    .replaceAll("\",\"", "\",\r\n\t\"")
                    .replace("}", "\r\n}");
            return bd;
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
