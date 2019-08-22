package cipher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import msg.ReqMsg;
import msg.RespMsg;
import msg.Verifiable;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.Objects;

/**
 * @author tanghf
 * @createTime 2019/7/15 14:31
 */
public class HxBankUtil implements HxBankConstant{

    public static Log logger = LogFactory.getLog(HxBankUtil.class);
    private ReqMsg reqMsg;
    private RespMsg respMsg;

    public static String appSecret = "";
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private SecretKeySpec aecKey;

    public HxBankUtil() {
        this.privateKey = HxCipherUtils.getPrivateKey(privateStr);
        this.publicKey = HxCipherUtils.getPublicKey(publicStr);
        final String AppSecret = appSecret;
        this.aecKey = HxCipherUtils.getSecretKey(AppSecret);
    }

    public RespMsg sendMsg(String url, Map<String, Object> bodyMap) throws Exception {
        //1.拼装ReqMsg报文对象
        createReqMsg(bodyMap);
        logger.info("请求报文：" + reqMsg.toLog());

        //2.发送HTTP请求
        logger.info("请求URL：" + url);
        String response = doPostQueryCmdJson(url, reqMsg.toString());
        logger.info("响应报文：" + response);

        //3.解析应答获得RespMsg报文对象
        createRespMsg(response);
        logger.info("响应报文体：" + respMsg.formatBody(respMsg.getBodyData()));

        return respMsg;
    }

    private void createReqMsg(Map<String, Object> bodyMap) throws UnsupportedEncodingException {
        if (reqMsg == null){
            reqMsg = new ReqMsg();
        }
        if (bodyMap == null || bodyMap.isEmpty()){
            //body数据为空时，默认是申请密钥请求，使用平台公钥进行RSA加密
            byte[] bytes = HxCipherUtils.encryptRSA(publicKey, "");
            reqMsg.setBody(Base64.encodeBase64String(bytes));
        }else{
            //使用加密密钥进行AES加密
            String str = JSON.toJSONString(bodyMap);
            byte[] bytes = HxCipherUtils.encryptAES(aecKey, str);
            reqMsg.setBody(Base64.encodeBase64String(bytes));
        }
        //签名
        reqMsg.setSignData(signData(reqMsg, privateKey));
        reqMsg.setBodyData(bodyMap);
    }

    private void createRespMsg(String response) throws UnsupportedEncodingException {
        JSONObject respJsonObj = JSON.parseObject(response);
        respMsg = (RespMsg) JSONObject.toJavaObject(respJsonObj, RespMsg.class);
        if (respMsg == null || respMsg.getHeader() == null){
            throw new RuntimeException("receive message abnormal");
        }

        //验签
        respMsg.setVerifyPass(verifyData(respMsg, publicKey));
        //body解密
        String bodyEncrypt = respMsg.getBody();
        String bodyJson = null;
        if (StringUtils.isNotBlank(bodyEncrypt)){
            if (reqMsg.isAcquireKeyReq()){
                bodyJson = HxCipherUtils.decryptRSA(privateKey, Base64.decodeBase64(bodyEncrypt));
            }else {
                bodyJson = HxCipherUtils.decryptAES(aecKey, Base64.decodeBase64(bodyEncrypt));
            }
            respMsg.setBodyData(JSON.parseObject(bodyJson).getInnerMap());
        }
    }

    private String doPostQueryCmdJson(String url, String json) throws IOException {
        //1.创建HttpClient和httpResponse
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        //2.创建http请求，设置url和entity
        HttpPost httpPost = new HttpPost(url);
        // 实体
        StringEntity entity = new StringEntity(json);
        entity.setContentEncoding(CHARSET);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        // 配置
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
        httpPost.setConfig(requestConfig);
        //3.http请求
        httpResponse = httpClient.execute(httpPost);
        //4.解析http返回
        HttpEntity responseEntity = httpResponse.getEntity();
        if (responseEntity != null) {
            String result = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            return result;
        }
        return "";
    }

    //签名
    private String signData(Verifiable verifyObj, PrivateKey privateKey) throws UnsupportedEncodingException {
        //1.组装待签名报文 encodeMD5(加密后的body+requestId+requestTime)
        String originData = HxCipherUtils.MD5(verifyObj.getMd5Src());

        //2.签名，使用平台私钥RSA加密
        long startTime = System.currentTimeMillis();
        byte[] encryptedData = HxCipherUtils.encryptRSA(privateKey, originData);

        //私钥签名后的数据
        //报文头前面256位的私钥签名后的结果privateResult
        String signData = HxCipherUtils.byteArrayToHexString(encryptedData);
        System.out.println("签名耗时: " + (System.currentTimeMillis()-startTime) + "ms");
        String resData = Base64.encodeBase64String(signData.getBytes(CHARSET));

        return resData;
    }

    //验签
    private boolean verifyData(Verifiable verifyObj, PublicKey publicKey) throws UnsupportedEncodingException {
        //1.组装待验证原文MD5加密
        String originData = HxCipherUtils.MD5(verifyObj.getMd5Src());

        //2.获取请求报文头header里的签名数据signData，进行Base64解密
        byte[] signBytes = Base64.decodeBase64(verifyObj.getSignData());
        if (StringUtils.isAllBlank(signBytes.toString())){
            logger.error("message receive step,sign data error");
            return false;
        }
        // 将得到的16进制字符串转换成 一半长度的字节数组：每两个16进制字符分别作为字节的高四位，低四位。
        byte[] encryptedBytes = HxCipherUtils.hexStringToByte(new String(signBytes));

        //3.RSA验签，使用公钥对字节数组进行RSA解密
        String decryptedSignData = HxCipherUtils.decryptRSA(publicKey, encryptedBytes);
        if(decryptedSignData == null) {
            logger.error("message receive step,rsa encrypt failure");
            return false;
        }

        //4.比较
        if(!Objects.equals(decryptedSignData, originData)) {
            logger.error("message receive step,verify failure");
            return false;
        }

        return true;
    }

}
