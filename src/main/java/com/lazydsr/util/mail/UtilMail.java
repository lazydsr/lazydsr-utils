package com.lazydsr.util.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * UtilMail
 * PROJECT_NAME: lazy
 * PACKAGE_NAME: com.lazy.com.lazydsr.util.UtilMail
 * Created by Lazy on 2017/5/13 20:16
 * Version: 0.1
 * Info: mail工具类
 */
public class UtilMail {
    private String host = "";
    private String username = "";
    private String password = "";
    private transient Properties properties;
    private transient MailAuthenticator mailAuthenticator;
    private transient Session session;
    //发送者昵称
    private String senderName;
    //是否开启Debug模式
    private boolean isDebug = false;
    //是否使用加密模式
    private boolean isTLS = false;
    //是否显示邮件内容
    private boolean showContent;

    /**
     * 从本地WEB-INF下的classes/prop文件夹中取配置文件Mail.pro
     */
    private UtilMail() {
        //从本地配置文件中取参数
    }

    /**
     * 有参构造
     *
     * @param host     类似xxx.xxx.com
     * @param username 用户名  类似xxx，不需要加@xxx.xxx.com
     * @param password 密码
     */
    public UtilMail(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
        //初始化发件服务器
        init();
    }

    /**
     * 有参构造
     *
     * @param host     类似xxx.xxx.com
     * @param username 用户名  类似xxx，不需要加@xxx.xxx.com
     * @param password 密码
     * @param isTLS    是否启用TLS
     */
    public UtilMail(String host, String username, String password, boolean isTLS) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.isTLS = isTLS;
        //初始化发件服务器
        init();
    }

    /**
     * 邮件服务器初始化
     */
    private void init() {
        //初始化property
        properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", isTLS ? "true" : "false");
        //验证
        mailAuthenticator = new MailAuthenticator(username, password);
        //创建Session
        session = Session.getInstance(properties, mailAuthenticator);
    }

    /**
     * 邮件发送----单个接收人
     *
     * @param recipient 接收人
     * @param subject   主题
     * @param content   内容
     * @param files     附件
     * @return 结果：true 成功，false 失败
     */
    public boolean send(String recipient, String subject, String content, String... files) {
        List<String> recipients = new ArrayList<String>();
        recipients.add(recipient);
        return send(recipients, null, null, subject, content, files);
    }

    /**
     * 邮件发送----多个接收人
     *
     * @param recipients 接收人
     * @param subject    主题
     * @param content    内容
     * @param files      附件
     * @return 结果：true 成功，false 失败
     */
    public boolean send(List<String> recipients, String subject, String content, String... files) {
        return send(recipients, null, null, subject, content, files);
    }

    /**
     * 邮件发送----单个接收人和一个抄送人
     *
     * @param recipient 接收人
     * @param CCUser    抄送人
     * @param subject   主题
     * @param content   内容
     * @param files     附件
     * @return 结果：true 成功，false 失败
     */
    public boolean send(String recipient, String CCUser, String subject, String content, String[] files) {
        List<String> recipients = new ArrayList<String>();
        recipients.add(recipient);
        List<String> CCUsers = new ArrayList<String>();
        CCUsers.add(CCUser);
        return send(recipients, CCUsers, null, subject, content, files);
    }

    /**
     * 邮件发送----单个接收人、一个抄送人和一个密送人
     *
     * @param recipient 接收人
     * @param CCUser    抄送人
     * @param CCUser    密送人
     * @param BCCUser   主题
     * @param content   内容
     * @param files     附件
     * @return 结果：true 成功，false 失败
     */
    public boolean send(String recipient, String CCUser, String BCCUser, String subject, String content, String[] files) {
        List<String> recipients = new ArrayList<String>();
        recipients.add(recipient);
        List<String> CCUsers = new ArrayList<String>();
        CCUsers.add(CCUser);
        List<String> BCCUsers = new ArrayList<String>();
        BCCUsers.add(BCCUser);
        return send(recipients, CCUsers, BCCUsers, subject, content, files);
    }

    /**
     * 邮件发送----多个接收人----自定义发件人显示名称
     *
     * @param recipients 接收人
     * @param CCUsers    抄送人
     * @param BCCUsers   密送人
     * @param subject    主题
     * @param content    内容
     * @param files      附件
     * @return 结果：true 成功，false 失败
     */
    public boolean send(List<String> recipients, List<String> CCUsers, List<String> BCCUsers, String subject, String content, String... files) {
        //设置是否启用Debug模式
        session.setDebug(isDebug);
        boolean result = false;
        // 创建mime类型邮件
        MimeMessage message = new MimeMessage(session);

        try {
            message.addHeader("charset", "UTF-8");
            // 设置发信人
            if (senderName == null || "".equals(senderName.trim()))
                message.setFrom(new InternetAddress(mailAuthenticator.getUsername()));
            else
                message.setFrom(new InternetAddress(mailAuthenticator.getUsername(), senderName));
            //设置发件人
            if (recipients == null || recipients.size() == 0) {
                System.out.println("发件人为空");
                return false;
            }
            int num = recipients.size();
            InternetAddress[] addresses = new InternetAddress[num];
            for (int i = 0; i < num; i++) {
                addresses[i] = new InternetAddress(recipients.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, addresses);
            //设置抄送人
            if (CCUsers != null && CCUsers.size() > 0) {
                int CCnum = CCUsers.size();
                InternetAddress[] CCaddresses = new InternetAddress[CCnum];
                for (int i = 0; i < CCnum; i++) {
                    CCaddresses[i] = new InternetAddress(CCUsers.get(i));
                }
                message.setRecipients(Message.RecipientType.CC, CCaddresses);
            }
            //设置密送人
            if (BCCUsers != null && BCCUsers.size() > 0) {
                int BCCnum = BCCUsers.size();
                InternetAddress[] BCCaddresses = new InternetAddress[BCCnum];
                for (int i = 0; i < BCCnum; i++) {
                    BCCaddresses[i] = new InternetAddress(BCCUsers.get(i));
                }
                message.setRecipients(Message.RecipientType.CC, BCCaddresses);
            }
            //设置发送时间
            Date date = new Date();
            message.setSentDate(date);
            // 设置主题
            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
            /*添加正文内容*/
            Multipart multipart = new MimeMultipart();
            BodyPart contentPart = new MimeBodyPart();

            contentPart.setContent(content, "text/html; charset=UTF-8");

            contentPart.setHeader("Content-Type", "text/html; charset=UTF-8");
            multipart.addBodyPart(contentPart);
            for (String file : files) {
                File usFile = new File(file);
                MimeBodyPart fileBody = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                fileBody.setDataHandler(new DataHandler(source));
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                fileBody.setFileName("=?GBK?B?"
                        + enc.encode(usFile.getName().getBytes()) + "?=");
                multipart.addBodyPart(fileBody);
            }

            message.setContent(multipart);

            message.saveChanges();
            // 发送
            Transport.send(message);
            //打印邮件内容
            if (showContent) {
                System.out.println("收件人：" + recipients.toString() + ",发送时间：" + date + ",邮件主题：" + subject + ",邮件内容：" + content.toString());
            }
            result = true;
        } catch (AuthenticationFailedException e) {
            System.out.println("发件人账户验证错误" + e);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("发件人地址错误");
            e.printStackTrace();
        } catch (MessagingException e) {
            System.out.println("邮件体错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public boolean isTLS() {
        return isTLS;
    }

    public void setTLS(boolean TLS) {
        isTLS = TLS;
    }

    public boolean isShowContent() {
        return showContent;
    }

    public void setShowContent(boolean showContent) {
        this.showContent = showContent;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    class MailAuthenticator extends Authenticator {
        private String username;
        private String password;

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }


        public MailAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}