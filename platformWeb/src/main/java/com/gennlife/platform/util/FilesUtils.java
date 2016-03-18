package com.gennlife.platform.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by chensong on 2015/12/14.
 */
public class FilesUtils {
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
}
