package com.ag2.util;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourcesPath {

    public static String ABS_PATH_CONFIG_AG2 = ""; //URLUtil.fileToURL(new File("resources/")).toString().replaceFirst("file:/","");
    public static String ABS_PATH_IMGS = "";// URLUtil.fileToURL(new File("resources/images/")).toString();
    public static String ABS_PATH_CSS = "";//URLUtil.fileToURL(new File("resources/css/")).toString();

    static {
        try {
            ABS_PATH_CONFIG_AG2 = new File("resources/").toURL().toString().replaceFirst("file:/", "");
            ABS_PATH_IMGS = new File("resources/images/").toURL().toString();
            ABS_PATH_CSS = new File("resources/css/").toURL().toString();

        } catch (MalformedURLException ex) {
            Logger.getLogger(ResourcesPath.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
