package com.eye.util;

import com.eye.constant.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BiyingImage {
    public static String ObtianBackImage() {
        BiyingImage getBingPicture = new BiyingImage();
        String home = "http://cn.bing.com";
        // 获取链接
        String filePath = null;
        try {
            String url = getBingPicture.GetUrl(home);
            // 保存图片
            filePath = getBingPicture.SavePicture(home + url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return filePath;
        }
    }


    //background-image:url(/th?id=OHR.PeruvianRainforest_ZH-CN4066508593_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp
    private String GetUrl(String home_url) throws Exception {
        InputStream is = new URL(home_url).openStream();
        byte[] buff = new byte[1024];
        StringBuilder builder = new StringBuilder();
        // 得到界面的字符串
        while (is.read(buff, 0, buff.length) > 0) {
            // 需要使用String的编码解码
            builder.append(new String(buff, "UTF-8"));
        }
        is.close();
//        System.out.println(builder.toString());
        // 开始正则匹配
        Matcher matcher = Pattern.compile("background-image:url[(]((/th[?]).*).jpg").matcher(builder.toString());
        // 找链接
        if (matcher.find()) {
            System.out.println("Find the url: " + matcher.group(1));
            return matcher.group(1);
        } else {
            throw new Exception("Not found the url");
        }
    }

    // 保存函数
    private String SavePicture(String url) throws IOException {
        // 打开链接
        InputStream is = new URL(url).openStream();
        // 链接处理一下得到名字
        int start = url.lastIndexOf("/") + 1;
        int end = url.indexOf("_");
        // 拼接出名字，substring函数前闭后开
        String name = "backimage" + (Constants.BACK_IMAGE_SAVE_INDEX % 5) + ".jpg";
        File file = new File(name);
        // 判断是否已经存在

        // 创建文件
//            file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff, 0, buff.length)) > 0) {
            fileOutputStream.write(buff, 0, len);
        }
        System.out.println(name + " was downloaded successfully");
        // 关掉才能保存到磁盘里
        fileOutputStream.close();

        is.close();
        return file.getAbsoluteFile().toURI().toString();
    }
}
