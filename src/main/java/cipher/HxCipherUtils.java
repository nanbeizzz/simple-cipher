package cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author tanghf
 * @createTime 2019/8/7 17:04
 */
public final class HxCipherUtils{

    public static Log logger = LogFactory.getLog(HxCipherUtils.class);

    public static byte[] encryptAES(Key key, String str) throws UnsupportedEncodingException {
        byte[] bytes = str.getBytes(HxBankConstant.CHARSET);
        return cipherAES(key, bytes, Cipher.ENCRYPT_MODE);
    }

    public static String decryptAES(Key key, byte[] bytes) throws UnsupportedEncodingException {
        byte[] resBytes = cipherAES(key, bytes, Cipher.DECRYPT_MODE);
        return new String(resBytes, HxBankConstant.CHARSET);
    }

    public static byte[] encryptRSA(Key key, String str) throws UnsupportedEncodingException {
        byte[] bytes = str.getBytes(HxBankConstant.CHARSET);
        return cipherRSA(key, bytes, Cipher.ENCRYPT_MODE);
    }

    /**
     * RSA解密
     * 1.验签时使用RSA公钥
     * 2.申请加密密钥时，使用RSA私钥对body数据解密
     *
     * @param key
     * @param bytes
     * @return
     */
    public static String decryptRSA(Key key, byte[] bytes) throws UnsupportedEncodingException {
        byte[] resBytes = cipherRSA(key, bytes, Cipher.DECRYPT_MODE);
        return new String(resBytes, HxBankConstant.CHARSET);
    }

    public static byte[] cipherAES(Key key, byte[] bytes, int mode){
        String algorithm = "AES/ECB/PKCS5Padding";
        return cipher(key, bytes, algorithm, mode);
    }

    public static byte[] cipherRSA(Key key, byte[] bytes, int mode){
        String algorithm = "RSA/ECB/PKCS1Padding";
        return cipher(key, bytes, algorithm, mode);
    }

    public static byte[] cipher(Key key, byte[] bytes, String algorithm, int mode){
        //1.实例化Cipher对象
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            logger.error("cipher algorithm `"+ algorithm +"` setting error");
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            logger.error("cipher failure");
            e.printStackTrace();
        }

        //2.初始化模式-解密
        try {
            cipher.init(mode, key);
        } catch (InvalidKeyException e) {
            logger.error("cipher key `"+ key +"` invalid");
            e.printStackTrace();
        }

        //3.解密
        byte[] resBytes = new byte[0];
        try {
            resBytes = cipher.doFinal(bytes);
        } catch (IllegalBlockSizeException e) {
            logger.error("cipher failure");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            logger.error("cipher failure");
            e.printStackTrace();
        }

        //4.返回
        return resBytes;
    }

    /**
     * 获得应用公钥
     * */
    public static PublicKey getPublicKey(String keyStr){
        //1.keyStr是使用base64编码过的数据,需要解码
        byte[] bytes = Base64.decodeBase64(keyStr);

        //2.创建KeyFactory对象
        KeyFactory rsa = null;
        try {
            rsa = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //3.创建EncodedKeySpec对象
        EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);

        //4.生成PublicKey对象
        PublicKey publicKey = null;
        try {
            publicKey = rsa.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    //获得应用私钥
    public static PrivateKey getPrivateKey(String keyStr){
        //1.privateKeyStr 是使用 base64 编码过的数据，需要解码
        byte[] bytes = Base64.decodeBase64(keyStr);
        //2.使用密钥工厂 KeyFactory 加载规则
        KeyFactory rsa = null;
        try {
            rsa = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //3.规则编码？
        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        //4.获得私钥对象
        PrivateKey privateKey = null;
        try {
            privateKey = rsa.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return privateKey;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static SecretKeySpec getSecretKey(final String password){
        // 返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // AES 密钥长度为
        kg.init(HxBankConstant.KEY_LENGTH, new SecureRandom(password.getBytes()));
        // 生成一个密钥
        SecretKey secretKey = kg.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    //MD5 加密
    public static String MD5(String s){
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        //1.字符转成字节数组，byte占8位
        byte[] bytes = s.getBytes();
        //2.获得 encodeMD5 摘要算法的 MessageDigest 对象
        MessageDigest mdInst = null;
        try {
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        //3.更新字节数组
        mdInst.update(bytes);
        //4.获得摘要算法值
        byte[] digest = mdInst.digest();
        //5.将字节数组转化成十六进制的字符串，Java中用补码表示二进制{正数不变，负数除最高位，其余取反，加一}
        char[] chars = new char[digest.length * 2];
        int k = 0;
        for (int i=0; i<digest.length; i++){
            byte b = digest[i];
            chars[k++] = hexDigits[b >>> 4 & 0xf];
            chars[k++] = hexDigits[b & 0xf];
        }
        return new String(chars).toUpperCase();
    }

    // 将字节数组转换为十六进制字符串
    public static String byteArrayToHexString(byte[] bytearray) {
        String strDigest = "";
        for (int i = 0; i < bytearray.length; i++)
        {
            strDigest += byteHEX(bytearray[i]);
        }
        return strDigest;
    }

    /*
	 * byteHEX()，用来把\uFFFD byte类型的数转换成十六进制的ASCII表示\uFFFD
	 * \uFFFD因为java中的byte的toString无法实现这一点，我们又没有C语言中的 sprintf(outbuf,"%02X",ib)
	 */
    public static String byteHEX(byte ib) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F' };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    //16进制字符串转为字节数组
    public static byte[] hexStringToByte(String hex){
        int len = (hex.length()/2);
        byte[] result = new byte[len];
        char[] char0=hex.toCharArray();
        for(int i=0;i<len;i++){
            int pos=i*2;
            result[i]=(byte)(toByte(char0[pos])<<4|toByte(char0[pos+1]));
        }
        return result;
    }

    public static int toByte(char c){
        return "0123456789ABCDEF".indexOf(c);
    }
}
