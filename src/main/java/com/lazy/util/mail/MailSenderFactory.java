package com.lazy.util.mail;

/**
 * Coding......
 * Created by D.SR on 2017/1/22.
 * Info:暂未启用
 */
public class MailSenderFactory {
    private static UtilMail utilMail;
    public static UtilMail getUtilMail(){
        if (utilMail ==null){
            //utilMail=new com.dsr.mail.UtilMail();
        }
        return utilMail;
    }



}
