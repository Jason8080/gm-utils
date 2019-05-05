package com.gm.utils.third;

import com.gm.utils.Utils;
import com.gm.utils.base.Bool;
import com.gm.utils.base.ExceptionUtils;
import com.gm.utils.base.Logger;
import com.gm.utils.ext.Resource;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.*;

/**
 * 邮件工具类.
 *
 * @author Jason
 */
public class Mail implements Utils {

    private static final String username = "gmleemail";
    private static final String password = "QQ123456";
    private static Session session;
    private static Authenticator auth;
    private static Transport transport;

    /**
     * 重新加载邮件配置.
     *
     * @return the properties
     */
    private static Properties reLoad() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", Resource.getProperty("mail.transport.protocol", "smtp"));
        properties.setProperty("mail.smtp.host", Resource.getProperty("mail.smtp.host", "smtp.163.com"));
        properties.setProperty("mail.smtp.suffix", Resource.getProperty("mail.smtp.suffix", "@163.com"));
        properties.setProperty("mail.smtp.port", Resource.getProperty("mail.smtp.port", "25"));
        properties.setProperty("mail.smtp.auth", Resource.getProperty("mail.smtp.auth", "true"));
        properties.setProperty("mail.debug", Resource.getProperty("mail.debug", "true"));
        properties.setProperty("mail.smtp.ssl.enable", Resource.getProperty("mail.smtp.ssl.enable", "false"));
        properties.setProperty("mail.smtp.user", Resource.getProperty("mail.smtp.user", username));
        properties.setProperty("mail.smtp.pass", Resource.getProperty("mail.smtp.pass", password));
        return properties;
    }

    private static Authenticator reAuthentication() {
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Resource.getProperty("mail.auth.username", username),Resource.getProperty("mail.auth.password", password));
            }
        };
        return auth = authenticator;
    }


    /**
     * 重新验证.
     *
     * @return the session
     */
    private static Session reSession() {
        Session s = Session.getDefaultInstance(reLoad(), reAuthentication());
        return session = s;
    }


    /**
     * 获取通道
     *
     * @return transport transport
     */
    public static Transport reTransport() {
        try {
            Transport trans = reSession().getTransport();
            // 长时间连接可能会断
            trans.connect(session.getProperty("mail.smtp.host"), username, password);
            return transport = trans;
        } catch (MessagingException e) {
            return ExceptionUtils.cast(e);
        }
    }

    private static Transport getTransport(){
        if(transport!=null){
            return transport;
        }
        return reTransport();
    }

    /**
     * 发送纯文本内容.
     *
     * @param title    the title
     * @param content  the content
     * @param receiver the receiver
     * @return the boolean
     */
    public static boolean send(String title, String content, String receiver) {
        return send(null, title, content, null, null, Bool.isNull(receiver)?new String[0]:receiver.split(";"));
    }

    /**
     * 发送纯文本并接收答复.
     *
     * @param replyTo  the reply to
     * @param title    the title
     * @param content  the content
     * @param receiver the receiver
     * @return the boolean
     */
    public static boolean send(String replyTo, String title, String content, String receiver) {
        return send(replyTo, title, content, null, null, Bool.isNull(receiver)?new String[0]:receiver.split(";"));
    }

    /**
     * 发送无回复多媒体邮件.
     *
     * @param title    the title
     * @param content  the content
     * @param images   the images
     * @param attaches the attaches
     * @param sss      the sss
     * @return the boolean
     */
    public static boolean send(String title, String content, String[] images, String[] attaches, String[]... sss) {
        return send(null, title, content, images, attaches, sss);
    }


    /**
     * 发送带内嵌图片、附件、多收件人(显示邮箱姓名)、邮件优先级、阅读回执的完整的HTML邮件
     *
     * @param replyTo     the 回复人
     * @param title       the 标题
     * @param content     the HTML内容
     * @param images      the 图片绝对路径
     * @param accessories the 附件绝对路径
     * @param sss         the 接收人,抄送人,密送人
     * @return the boolean
     */
    public static boolean send(String replyTo,
                               String title, String content,
                               String[] images, String[] accessories,
                               String[]... sss) {
        try {
            // 创建一封邮件
            MimeMessage message = getMimeMessage(replyTo, title, sss);

            // 创建混合型载体
            message.setContent(getMimeMultipart(content, images, accessories));

            // 保存邮件
            long start = System.currentTimeMillis();
            message.saveChanges();
            Logger.debug("保存耗时".concat(String.valueOf(System.currentTimeMillis()-start)));
            /*File eml = buildEml(message);
            sendEml(eml);*/

            // 发送邮件
            start = System.currentTimeMillis();
            try {
                getTransport().sendMessage(message, message.getAllRecipients());
            } catch (IllegalStateException e) {
                reTransport().sendMessage(message, message.getAllRecipients());
            }
            Logger.debug("发送耗时".concat(String.valueOf(System.currentTimeMillis()-start)));
        } catch (Exception e) {
            Logger.error("邮件发送失败", e);
            return false;
        }
        return true;
    }

    private static MimeMultipart getMimeMultipart(String content, String[]... files) throws MessagingException, UnsupportedEncodingException {
        // 邮件
        MimeMultipart mailContent = new MimeMultipart();

        if (files.length > 0 && !Bool.isNull(files[0])) {
            for (String image : files[0]) {
                // 创建载体
                MimeBodyPart part = new MimeBodyPart();
                mailContent.addBodyPart(part);
                // 加载图片
                DataSource source = new FileDataSource(image);
                DataHandler data = new DataHandler(source);
                part.setDataHandler(data);
                part.setContentID(source.getName());
            }
        }
        if (!Bool.isNull(content)) {
            MimeBodyPart text = new MimeBodyPart();
            mailContent.addBodyPart(text);
            text.setContent(content, "text/html;charset=".concat(CHARSET));
        }
        if (files.length > 1 && !Bool.isNull(files[1])) {
            for (String attach : files[1]) {
                // 创建载体
                MimeBodyPart part = new MimeBodyPart();
                // 添加到邮件当中
                mailContent.addBodyPart(part);
                // 读取附件
                DataSource source = new FileDataSource(attach);
                DataHandler data = new DataHandler(source);
                part.setDataHandler(data);
                part.setFileName(source.getName());
            }
        }
        return mailContent;
    }

    private static MimeMessage getMimeMessage(String replyTo, String title, String[]... sss) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(session != null ? session : reSession());
        // 设置主题
        message.setSubject(title);
        // 设置发送人
        message.setFrom(new InternetAddress(session.getProperty("mail.smtp.user").concat(session.getProperty("mail.smtp.suffix")), "GmMails", CHARSET));
        // 设置接收人
        receivedBy(message, sss);
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置优先级(1:紧急   3:普通    5:低)
        message.setHeader("X-Priority", "1");
        if (!Bool.isNull(replyTo)) {
            // 设置回复人(收件人回复此邮件时,默认收件人)
            message.setReplyTo(InternetAddress.parse("\"" + replyTo + "\" <" + replyTo + ">"));
            // 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
            message.setHeader("Disposition-Notification-To", replyTo);
        }
        return message;
    }

    private static void receivedBy(MimeMessage message, String[]... sss) throws MessagingException, UnsupportedEncodingException {
        // 防止空参异常
        String[] toArray = Bool.isNull(sss)?new String[0]:sss[0];
        String[] ccArray = Bool.isNull(sss)?new String[0]:((sss.length>1&&!Bool.isNull(sss[1])?sss[1]:new String[0]));
        String[] bccArray = Bool.isNull(sss)?new String[0]:((sss.length>2&&!Bool.isNull(sss[2])?sss[2]:new String[0]));
        List<Address> addresses = new ArrayList();
        for (String to : toArray) {
            addresses.add(new InternetAddress(to, to, CHARSET));
        }
        // 设置收件人
        message.setRecipients(Message.RecipientType.TO, addresses.toArray(new Address[]{}));
        addresses.clear();
        for (String cc : ccArray) {
            addresses.add(new InternetAddress(cc, cc, CHARSET));
        }
        // 设置抄送
        addresses.add(new InternetAddress(session.getProperty("mail.smtp.user").concat(session.getProperty("mail.smtp.suffix")), "GmMails", CHARSET));
        message.setRecipients(Message.RecipientType.CC, addresses.toArray(new Address[]{}));
        addresses.clear();
        for (String bcc : bccArray) {
            addresses.add(new InternetAddress(bcc, bcc, CHARSET));
        }
        // 设置密送
        message.setRecipients(Message.RecipientType.BCC, addresses.toArray(new Address[]{}));
        addresses.clear();
    }


    /**
     * 将邮件内容生成eml文件
     *
     * @param message 邮件内容
     * @return the file
     * @throws MessagingException the messaging exception
     * @throws IOException        the io exception
     */
    public static File buildEml(Message message) throws MessagingException, IOException {
        File file = new File(MimeUtility.decodeText(message.getSubject()) + ".eml");
        message.writeTo(new FileOutputStream(file));
        return file;
    }

    /**
     * 发送本地已经生成好的email文件
     *
     * @param eml the eml
     * @throws Exception the exception
     */
    public static void sendEml(File eml) throws Exception {
        // 获得邮件会话
        Session session = reSession();
        // 获得邮件内容,即发生前生成的eml文件
        InputStream is = new FileInputStream(eml);
        MimeMessage message = new MimeMessage(session, is);
        // 发送邮件
        Transport.send(message);
    }
}
