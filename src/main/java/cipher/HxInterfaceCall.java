package cipher;

import msg.RespMsg;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Tanghf
 * @version V1.1
 * @Date 2019/7/15 14:39
 *
 * 方法名对应接口交易码
 */
public class HxInterfaceCall {

    /** 实时短信发送 */
    public RespMsg call0001(Map<String, Object> map){
        final String url = HxBankConstant.HOST + "";
        Map<String, Object> bodyMap = getBaseBodyMap("0001");
        bodyMap.putAll(map);
        return callInterface(url, bodyMap);
    }

    private RespMsg callInterface(String url, Map<String, Object> map){
        RespMsg respMsg = null;
        try {
            respMsg = new HxBankUtil().sendMsg(url, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMsg;
    }

    private Map<String, Object> getBaseBodyMap(String transCode){
        Map<String, Object> bodyData = new LinkedHashMap<>();
        bodyData.put("transCode", transCode);
        bodyData.put("parentMerchantId", HxBankConstant.PARENT_MERCHANT_ID);
        return bodyData;
    }

}
