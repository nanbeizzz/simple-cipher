package commons.coder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.Md5Crypt;

/**
 * @author tanghf
 * @className commons.coder.Function.java
 * @createTime 2019/8/27 13:52
 */
public class Function {

    public static void main(String[] args) {
        String encodeBase64String = Base64.encodeBase64String("aaa".getBytes());
        System.out.println("encodeBase64String = " + encodeBase64String);

        byte[] decodeBase64 = Base64.decodeBase64(encodeBase64String);
        String s = new String(decodeBase64);
        System.out.println("s = " + s);

        String md5Crypt = Md5Crypt.md5Crypt(decodeBase64);
        System.out.println("md5Crypt = " + md5Crypt);
        System.out.println("md5Crypt = " + Md5Crypt.md5Crypt("aaa".getBytes()));
    }
}
