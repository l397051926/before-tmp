package com.gennlife.platform.util;

import de.idyl.winzipaes.AesZipFileDecrypter;
import de.idyl.winzipaes.AesZipFileEncrypter;
import de.idyl.winzipaes.impl.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 压缩指定文件或目录为ZIP格式压缩文件
 * 支持中文(修改源码后)
 * 支持密码(仅支持256bit的AES加密解密)
 * 依赖bcprov项目(bcprov-jdk16-140.jar)
 *
 * @author zyh
 */
public class DecryptionZipUtil {
    private static final Logger logger = LoggerFactory.getLogger(DecryptionZipUtil.class);

    /**
     * 使用指定密码将给定文件或文件夹压缩成指定的输出ZIP文件
     *
     * @param srcFile  需要压缩的文件或文件夹
     * @param destPath 输出路径
     * @param passwd   压缩文件使用的密码
     */
    public static void zip(String srcFile, String destPath, String passwd) throws Exception {
        AESEncrypter encrypter = new AESEncrypterBC();
        AesZipFileEncrypter zipFileEncrypter = null;
        try {
            zipFileEncrypter = new AesZipFileEncrypter(destPath, encrypter);
            File sFile = new File(srcFile);
            /**
             * AesZipFileEncrypter提供了重载的添加Entry的方法,其中:
             * add(File f, String passwd)
             *          方法是将文件直接添加进压缩文件
             *
             * add(File f,  String pathForEntry, String passwd)
             *          方法是按指定路径将文件添加进压缩文件
             * pathForEntry - to be used for addition of the file (path within zip file)
             */
            doZip(sFile, zipFileEncrypter, "", passwd);
        } finally {
            zipFileEncrypter.close();
        }
    }

    /**
     * 具体压缩方法,将给定文件添加进压缩文件中,并处理压缩文件中的路径
     *
     * @param file         给定磁盘文件(是文件直接添加,是目录递归调用添加)
     * @param encrypter    AesZipFileEncrypter实例,用于输出加密ZIP文件
     * @param pathForEntry ZIP文件中的路径
     * @param passwd       压缩密码
     * @throws IOException
     */
    private static void doZip(File file, AesZipFileEncrypter encrypter,
                              String pathForEntry, String passwd) throws IOException {
        if (file.isFile()) {
            pathForEntry += file.getName();
            encrypter.add(file, pathForEntry, passwd);
            return;
        }
        pathForEntry += file.getName() + "/";
        for (File subFile : file.listFiles()) {
            doZip(subFile, encrypter, pathForEntry, passwd);
        }
    }

    /**
     * 使用给定密码解压指定压缩文件到指定目录
     *
     * @param inFile 指定Zip文件
     * @param outDir 解压目录
     * @param passwd 解压密码
     */
    public static void unzip(String inFile, String outDir, String passwd) throws Exception {
        File outDirectory = new File(outDir);
        if (!outDirectory.exists()) {
            outDirectory.mkdir();
        }
        AESDecrypter decrypter = new AESDecrypterBC();
        AesZipFileDecrypter zipDecrypter = null;
        try {
            zipDecrypter = new AesZipFileDecrypter(new File(inFile), decrypter);
            AesZipFileDecrypter.charset = "utf-8";
            /**
             * 得到ZIP文件中所有Entry,但此处好像与JDK里不同,目录不视为Entry
             * 需要创建文件夹,entry.isDirectory()方法同样不适用,不知道是不是自己使用错误
             * 处理文件夹问题处理可能不太好
             */
            List<ExtZipEntry> entryList = zipDecrypter.getEntryList();
            for (ExtZipEntry entry : entryList) {
                String eName = entry.getName();
                eName = eName.replace("\\", "/");
                String dir = eName.substring(0, eName.lastIndexOf("/") + 1);
                File extractDir = new File(outDir, dir);
                if (!extractDir.exists()) {
                    FileUtils.forceMkdir(extractDir);
                }
                /**
                 * 抽出文件
                 */
                File extractFile = new File(outDir + "/" + eName);
                zipDecrypter.extractEntry(entry, extractFile, passwd);
            }
        } finally {
            if (zipDecrypter != null) zipDecrypter.close();
        }
    }

}