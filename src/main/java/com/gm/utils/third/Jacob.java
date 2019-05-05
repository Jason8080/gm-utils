package com.gm.utils.third;

import com.gm.utils.Utils;
import com.gm.utils.base.Logger;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;  
import com.jacob.com.Variant;

/**
 * The type Jacob.
 *
 * @author Jason
 */
public class Jacob implements Utils {

    /**
     * 文字转音频
     *
     * @param content 文字内容
     * @param path    路径
     */
    public static void to(String content, String path) {
        try {
            ActiveXComponent ax = new ActiveXComponent("Sapi.SpVoice");
            Dispatch spVoice = ax.getObject();  
              
            ax = new ActiveXComponent("Sapi.SpFileStream");  
            Dispatch spFileStream = ax.getObject();  
              
            ax = new ActiveXComponent("Sapi.SpAudioFormat");  
            Dispatch spAudioFormat = ax.getObject();  
              
            //设置音频流格式  
            Dispatch.put(spAudioFormat, "Type", new Variant(22));  
              
            //设置文件输出流格式  
            Dispatch.putRef(spFileStream, "Format", spAudioFormat);  
            //调用输出 文件流打开方法，创建一个.wav文件  
            Dispatch.call(spFileStream, "Open", new Variant(path), new Variant(3), new Variant(true));
            //设置声音对象的音频输出流为输出文件对象  
            Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);  
            //设置音量 0到100  
            Dispatch.put(spVoice, "Volume", new Variant(100));  
            //设置朗读速度  
            Dispatch.put(spVoice, "Rate", new Variant(-2));  
            //开始朗读  
            Dispatch.call(spVoice, "Speak", new Variant(content));
              
            //关闭输出文件  
            Dispatch.call(spFileStream, "Close");  
            Dispatch.putRef(spVoice, "AudioOutputStream", null);

            spAudioFormat.safeRelease();  
            spFileStream.safeRelease();  
            spVoice.safeRelease();  
            ax.safeRelease();  
        } catch (Exception e) {
            Logger.debug("文字{}转音频出错",content,e);
        }  
    }  
}  