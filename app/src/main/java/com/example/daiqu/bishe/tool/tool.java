package com.example.daiqu.bishe.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tool {
    //正则表达式判断是不是手机号
    public static boolean isPhone(String phone) {
        Pattern pattern = Pattern.compile("^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }
    //正则表达式判断密码是否规范(6-15位数字密码组合而成)
    public static boolean isPwd(String pwd) {
        Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$");
        Matcher matcher = pattern.matcher(pwd);
        return matcher.find();
    }


}
