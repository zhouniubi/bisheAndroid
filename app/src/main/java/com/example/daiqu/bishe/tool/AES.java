package com.example.daiqu.bishe.tool;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    //生成key
    public static String generateKey(){
        try{
            //生成Key
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //利用密码初始化
            kgen.init(128);
            //生成密钥
            SecretKey secretKey = kgen.generateKey();
            // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
            byte[] enCodeFormat = secretKey.getEncoded();
            System.out.println("密钥是："+Hex.encodeHexString(enCodeFormat));
            return Hex.encodeHexString(enCodeFormat);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //加密部分：code为原文，pwd为加密所需的密码
    public static String encrypt(String code){
        try{
            // 转换为AES专用密钥
            String pwd = "a18b952e6317db954328fd82b29dff63";
            Key key = new SecretKeySpec(Hex.decodeHex(pwd.toCharArray()),"AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] result = cipher.doFinal(code.getBytes());
            return Hex.encodeHexString(result);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //解密部分：code为密文，pwd为加密时的密码
    public static String decrypt(String code){
        try{
            // 转换为AES专用密钥
            String pwd = "a18b952e6317db954328fd82b29dff63";
            Key key = new SecretKeySpec(Hex.decodeHex(pwd.toCharArray()), "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            // 初始化为解密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(Hex.decodeHex(code.toCharArray()));
            return new String(result); // 明文
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args){
        String testCode ="周牛逼";
        System.out.println(testCode);

        String miwen = encrypt(testCode);
        System.out.println(miwen);
        String yuanwen = decrypt(miwen);
        System.out.println(yuanwen);

    }

}
