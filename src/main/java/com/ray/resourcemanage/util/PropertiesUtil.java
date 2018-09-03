package com.ray.resourcemanage.util;

import com.mchange.v2.log.LogUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ProjectName: resourcemanage
 * @address: http://www.hikvision.com
 * @Auther: jiangsong7
 * @Date: 2018/8/24 16:59
 * @Description:
 */
public class PropertiesUtil {
    public static Properties readProperties(String fileName){
        InputStream inputStream=PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
        Properties config=new Properties();
        try {
            config.load(inputStream);
        }catch(IOException e){

        }finally{
            if(inputStream!=null){
                try{
                    inputStream.close();
                }catch(IOException e){

                }
            }
        }
        return config;
    }


}