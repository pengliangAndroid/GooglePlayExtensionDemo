package com.hdgame.extensiondmeo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author pengl
 */
public class ExtensionsFileUtil {

    public static String getObbFilePath(Context context) {
        try {
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Android/obb/"
                    + context.getPackageName()
                    + File.separator
                    + "main."
                    + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode
                    + "."
                    + context.getPackageName()
                    + ".obb";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void unZipObbFile(Context context,String outputFilePath) throws IOException {
        String obbFilePath = getObbFilePath(context);
        if (obbFilePath == null) {
            return;
        } else {
            File obbFile = new File(obbFilePath);
            if (!obbFile.exists()) {
                //obb文件下载失败或被删除，手动下载google后台的obb文件
                //由于google后台下载文件非常缓慢，建议下载自己后台的资源文件
                //GoogleApkExpandHelper.getObbUrl(context);
            } else {
                File outputFolder = new File(outputFilePath);
                if (!outputFolder.exists()) {
                    //目录未创建 没有解压过
                    outputFolder.mkdirs();
                    unzip(obbFile, outputFolder.getAbsolutePath());
                } else {
                    //目录已创建 判断是否解压过
                    if (outputFolder.listFiles() == null) {
                        //解压过的文件被删除
                        unzip(obbFile, outputFolder.getAbsolutePath());
                    }else {
                        //文件存在 此处可添加文件对比逻辑，默认重新覆盖
                        unzip(obbFile, outputFolder.getAbsolutePath());
                    }
                }
            }
        }
    }

    //这里没有添加解压密码逻辑，可以自己修改添加以下
    public static void unzip(File zipFile, String outPathString) throws IOException {
        createDirectoryIfNeeded(outPathString);
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry;
        String szName;
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                File file = new File(outPathString + File.separator + szName);
                createDirectoryIfNeeded(file.getParent());
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = inZip.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    public static String createDirectoryIfNeeded(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
        return folderPath;
    }
}
