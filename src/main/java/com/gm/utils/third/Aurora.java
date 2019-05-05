package com.gm.utils.third;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.gm.utils.Utils;
import com.gm.utils.base.Logger;
import com.gm.utils.ext.Json;
import com.gm.utils.ext.Resource;

import java.util.Map;


/**
 * 极光推送Api
 *
 * @author Jason
 */
public class Aurora implements Utils {

    /**
     * 声音代码的扩展字段
     */
    public static final String SOUND = "Sound";

    private static String MASTER_SECRET = Resource.getProperty("aurora.master.secret","6a08dbc81eebd02c1559b1c5");
    private static String APP_KEY = Resource.getProperty("aurora.app.key","d12e18b58f6284c7243f4611");
    private static Long timeout = Resource.getLong("aurora.timeout", 3600*24);
    private static Boolean env = Resource.getBoolean("aurora.env");


    private static JPushClient client = new JPushClient(MASTER_SECRET, APP_KEY);

    /**
     * 发送给指定客户
     *
     * @param title   the title
     * @param content 发送内容
     * @param extras  额外字段
     * @param seq     用户编号
     * @return boolean boolean
     */
    public synchronized static boolean send(String title, String content,
                                                Map<String, String> extras, String... seq) {
        try {
            PushPayload payload = buildPushObject_all_alias_alert(null, title, content, extras,seq);
            PushResult result = client.sendPush(payload);
            return result.isResultOK();
        } catch (Exception e) {
            Logger.error("极光推送 指定用户{"+Json.toJson(seq)+"}发送失败");
        }
        return false;
    }


    /**
     * Send boolean.
     *
     * @param msgId   the msg id
     * @param title   the title
     * @param content the content
     * @param extras  the extras
     * @param seq     the seq
     * @return the boolean
     */
    public synchronized static boolean send(Long msgId, String title, String content,
                                                Map<String, String> extras, String... seq) {
        try {
            PushPayload payload = buildPushObject_all_alias_alert(msgId, title, content, extras,seq);
            PushResult result = client.sendPush(payload);
            return result.isResultOK();
        } catch (Exception e) {
            Logger.error("极光推送 指定用户{"+Json.toJson(seq)+"}发送失败");
        }
        return false;
    }

    /**
     * 发送给所有安卓用户
     *
     * @param noticeTitle 通知内容标题
     * @param msgTitle    消息内容标题
     * @param msgContent  消息内容
     * @param extras      扩展字段
     * @return 0推送失败 ，1推送成功
     */
    public synchronized static boolean sendToAllAndroid(String noticeTitle, String msgTitle, String msgContent, Map<String,String> extras) {
        try {
            PushPayload pushPayload = buildPushObject_android_all_alertWithTitle(noticeTitle, msgTitle, msgContent, extras);
            System.out.println(pushPayload);
            PushResult pushResult = client.sendPush(pushPayload);
            System.out.println(pushResult);
            return pushResult.isResultOK();
        } catch (Exception e) {
            Logger.error("极光推送 安卓用户推送失败");
        }
        return false;
    }

    /**
     * 发送给所有IOS用户
     *
     * @param noticeTitle 通知内容标题
     * @param msgTitle    消息内容标题
     * @param msgContent  消息内容
     * @param extras      扩展字段
     * @return false推送失败 ，true推送成功
     */
    public synchronized static boolean sendToAllIos(String noticeTitle, String msgTitle, String msgContent, Map<String,String> extras) {
        try {
            PushPayload pushPayload = buildPushObject_ios_all_alertWithTitle(noticeTitle, msgTitle, msgContent, extras);
            System.out.println(pushPayload);
            PushResult pushResult = client.sendPush(pushPayload);
            System.out.println(pushResult);
            return pushResult.isResultOK();
        } catch (Exception e) {
            Logger.error("极光推送 苹果用户推送失败");
        }
        return false;
    }

    /**
     * 发送给所有用户
     *
     * @param noticeTitle 通知内容标题
     * @param msgTitle    消息内容标题
     * @param msgContent  消息内容
     * @param extras      扩展字段
     * @return false推送失败 ，true推送成功
     */
    public synchronized static boolean sendToAll(String noticeTitle, String msgTitle, String msgContent, Map<String,String> extras) {
        try {
            PushPayload pushPayload = buildPushObject_android_and_ios(noticeTitle, msgTitle, msgContent, extras);
            System.out.println(pushPayload);
            PushResult pushResult = client.sendPush(pushPayload);
            System.out.println(pushResult);
            return pushResult.isResultOK();
        } catch (Exception e) {
            Logger.error("极光推送 全平台推送失败");
        }
        return false;
    }

    private static PushPayload buildPushObject_android_and_ios(String noticeTitle,
                                                               String msgTitle,
                                                               String msgContent,
                                                               Map<String,String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .setAlert(noticeTitle)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(noticeTitle)
                                .setTitle(noticeTitle)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtras(extras)
                                .build()
                        )
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(noticeTitle)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound(extras.get(SOUND))
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtras(extras)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // .setContentAvailable(true)

                                .build()
                        )
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtras(extras)
                        .build())

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(env)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(timeout)
                        .build()
                )
                .build();
    }


    private static PushPayload buildPushObject_all_alias_alert(Long msgId,
                                                               String title,
                                                               String content,
                                                               Map<String,String> extras,
                                                               String... alias) {
        Options.Builder builder = Options.newBuilder();
        if(msgId!=null) {
            builder.setOverrideMsgId(msgId);
        }
        return PushPayload
                .newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(
                        Notification
                                .newBuilder()
                                .addPlatformNotification(
                                        IosNotification.newBuilder()
                                                .addExtras(extras)
                                                .setAlert(content)
                                                .setSound(extras.get(SOUND)).build()
                                )
                                .addPlatformNotification(
                                        AndroidNotification.newBuilder()
                                                .addExtras(extras)
                                                .setAlert(content)
                                                .setTitle(title).build()
                                )
                                .build()
                )
                .setOptions(
                        builder.setApnsProduction(env).setTimeToLive(timeout).build()).build();
    }

    private static PushPayload buildPushObject_android_all_alertWithTitle(String noticeTitle, String msgTitle, String msgContent, Map<String,String> extras) {
        System.out.println("----------buildPushObject_android_registrationId_alertWithTitle");
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.android())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(noticeTitle)
                                .setTitle(noticeTitle)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtras(extras)
                                .build())
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtras(extras)
                        .build())

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(env)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(2)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(timeout)
                        .build())
                .build();
    }

    private static PushPayload buildPushObject_ios_all_alertWithTitle(String noticeTitle, String msgTitle, String msgContent, Map<String,String> extras) {
        System.out.println("----------buildPushObject_ios_registrationId_alertWithTitle");
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.ios())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(noticeTitle)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound(extras.get(SOUND))
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtras(extras)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // .setContentAvailable(true)

                                .build())
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtras(extras)
                        .build())

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(env)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(3)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(timeout)
                        .build())
                .build();
    }
}
