package com.gennlife.platform.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by Chenjinfeng on 2017/12/8.
 */
public class ZipUtils {
    private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);
    private static AtomicInteger incr = new AtomicInteger(0);

    public static Map<String, String> createMd5Map(String fileName) {
        Map<String, String> md5List = new HashMap<>();
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        BufferedReader reader = null;
        String temp = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((temp = reader.readLine()) != null) {
                temp = temp.replace("\t", " ").replace("  ", " ").replace("\r", "").replace("\n", "");
                String[] maps = temp.split(" ");
                if (maps != null && maps.length == 2 && !StringUtils.isEmpty(maps[0]) && !StringUtils.isEmpty(maps[1])) {
                    md5List.put(maps[1], maps[0]);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
        return md5List;
    }

    public static boolean unZipFiles(File file, String descDir) {
        return unZipFiles(file.getAbsolutePath(), descDir);
    }

    public static boolean unZipFiles(String zipFilePath, String descDir) {
        File zipFile = new File(zipFilePath);
        File pathFile = new File(descDir);

        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = null;
        InputStream in = null;
        OutputStream out = null;

        try {
            zip = new ZipFile(zipFile, Charset.forName("GBK"));
            Enumeration<?> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                in = zip.getInputStream(entry);
                String outPath = (descDir + "/" + zipEntryName).replace("\\", "/");
                //判断路径是否存在，不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经创建,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                File outPathFile = new File(outPath);
                File prarent = outPathFile.getParentFile();
                if (!prarent.exists()) prarent.mkdirs();
                out = new FileOutputStream(outPathFile);
                byte[] buf = new byte[4 * 1024];
                int len;
                while ((len = in.read(buf)) >= 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();

            }
        } catch (Exception e) {
            logger.error("", e);
            return false;
        } finally {
            try {
                if (zip != null) {
                    zip.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            } catch (IOException e) {
                logger.error("", e);
                return false;
            }
        }
        return true;
    }


    public static String getMd5ByFile(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();

        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());

        String data = bigInt.toString(16);
        if (data.length() < 32) {
            StringBuffer buffer1=new StringBuffer();
            for (int i=0;i<32-data.length();i++)
                buffer1.append("0");
            buffer1.append(data);
            data=buffer1.toString();
        }
        return data;
    }

    public static LinkedList<File> getAllZip(String path) {
        LinkedList<File> zips = new LinkedList<>();
        File file = new File(path);
        if (file.isDirectory() || file.exists()) {
            File[] files = file.listFiles();
            for (File item : files) {
                if (item.getName().endsWith(".zip")) {
                    zips.add(item);
                }
            }
        }
        return zips;
    }

    public static void createZip(String sourcePath, String zipPath) throws Exception {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);
            //zos.setEncoding("gbk");//此处修改字节码方式。
            //createXmlFile(sourcePath,"293.xml");
            writeZip(new File(sourcePath), "", zos);
        } finally {
            if (zos != null) {
                zos.close();
            }
            if (fos != null)
                fos.close();

        }
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos) throws Exception {
        if (file.exists()) {
            if (file.isDirectory()) {//处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        writeZip(f, parentPath, zos);
                    }
                } else {       //空目录则创建当前目录
                    zos.putNextEntry(new ZipEntry(parentPath));

                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
        }
    }

}
