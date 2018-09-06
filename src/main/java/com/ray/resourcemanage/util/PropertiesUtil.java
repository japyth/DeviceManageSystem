package com.ray.resourcemanage.util;

import com.mchange.v2.log.LogUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
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