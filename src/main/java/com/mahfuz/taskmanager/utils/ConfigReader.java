package com.mahfuz.taskmanager.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    public static String read(String key) {
        Properties props;
        try {
            props = new Properties();
            // instead of hardcoding the path, we can get the path using any function that get current directory path
            // and then append the config file name

            String currentDirectory = System.getProperty("user.dir");
            System.out.println("The current working directory is " + currentDirectory);

            FileInputStream fis = new FileInputStream("/home/mahfuz/task-manager-servlet-rest-api/config.properties");
            props.load(fis);
        } catch (IOException e) {
            throw new CustomHttpException(500, e.getMessage());
        }
        return props.getProperty(key);
    }
}
