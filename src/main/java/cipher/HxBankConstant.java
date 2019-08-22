package cipher;

/**
 * @author tanghf
 * @createTime 2019/7/15 15:33
 */
public interface HxBankConstant {

    String CHARSET = "UTF-8";

    String ALGORITHM_MD5 = "MD5";

    String ALGORITHM_RSA_KEY = "RSA";

    String ALGORITHM_RSA_CIPHER = "RSA/ECB/PKCS1Padding";

    /**
     * 加密算法key
     */
    String ALGORITHM_AES_KEY = "AES";
    /**
     * 默认的加密算法
     */
    String ALGORITHM_AES_CIPHER = "AES/ECB/PKCS5Padding";
    /**
     * AES 秘钥长度 （128位）
     */
    int KEY_LENGTH = 128;

    //测试私钥
    String privateStr = "";
    //测试公钥
    String publicStr = "";


    /** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
    int KEY_SIZE = 2048;

    String HOST = "";

    /* 银行分配的合作商标号*/
    String PARENT_MERCHANT_ID = "undefined";
    String HXBANK = "";
    String CLEAR_FILE_PATH = "";
}
