package com.revature.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EnvironmentParser {
    public Map<String, String> parseVariables() {

        Properties prop = new Properties();

        ClassLoader classLoader = getClass().getClassLoader();

        InputStream is = classLoader.getResourceAsStream("jdbc.properties");

        String url, username, password;

        Map<String, String> varMap = new HashMap<>();

        String var="";

        try{
            prop.load(is);
            url = (String)prop.getProperty("url").split("\\{")[1].split("}")[0];
            var = System.getenv(url);
            varMap.put("URL", var);
            username = (String)prop.getProperty("username").split("\\{")[1].split("}")[0];
            var = System.getenv(username);
            varMap.put("USERNAME", var);
            password = (String)prop.getProperty("password").split("\\{")[1].split("}")[0];
            var = System.getenv(password);
            varMap.put("PASSWORD", var);

        } catch(IOException e){
            e.printStackTrace();
        }

        return varMap;
    }
}
