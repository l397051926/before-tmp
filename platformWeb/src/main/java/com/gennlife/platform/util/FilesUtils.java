package com.gennlife.platform.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by chensong on 2015/12/14.
 */
public class FilesUtils {
    private Logger logger = LoggerFactory.getLogger(FilesUtils.class);
    public static final String readString(InputStream in, String charset) throws IOException {
        return readString(new InputStreamReader(in,charset));
    }

    public static final String readString(Reader reader) throws IOException{
        StringBuilder sb = new StringBuilder();
        try {
            char[] buf = new char[1024];
            for (int i = 0; (i = reader.read(buf)) != -1;)
                sb.append(buf, 0, i);
        } finally {
            Closer.close(reader);
        }
        return sb.toString();
    }

    public static String readFile(String fileName) throws IOException {
        InputStream inputStream5 =  FilesUtils.class.getResourceAsStream(fileName);
        String data = FilesUtils.readString(inputStream5, "utf-8");
        inputStream5.close();
        return data;
    }


    public static void makeDirectory(String s) throws IOException {
        File file = new File(s);
        if(!file.exists()){
            file.createNewFile();
        }
    }

    public static String getTypePart(String name) {
        if (name == null){
            return null;
        }
        String[] data = name.split("\\.");
        return data[data.length - 1];
    }
}
