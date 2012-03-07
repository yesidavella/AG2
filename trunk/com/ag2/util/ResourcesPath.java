package com.ag2.util;

import com.sun.deploy.util.URLUtil;
import java.io.File;

public class ResourcesPath {
    public final static String ABS_PATH_CONFIG_AG2 = URLUtil.fileToURL(new File("resources/")).toString().replaceFirst("file:/","");
    public final static String ABS_PATH_IMGS = URLUtil.fileToURL(new File("resources/images/")).toString();
    public final static String ABS_PATH_CSS = URLUtil.fileToURL(new File("resources/css/")).toString();

}
