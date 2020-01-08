/*
 * Copyright (c) 2014, kymjs 张涛 (kymjs123@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dim.ke.framework.core.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


/**
 * 文件与流处理工具类<br>
 * <p/>
 * <b>创建时间</b> 2014-8-14
 *
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.1
 */
public final class FileUtils {
    /**
     * 检测SD卡是否存在
     */
    public static boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 把一个文件转化为byte字节数组。
     *
     * @return
     */
    public static byte[] fileConvertToByteArray(File file) {
        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();

            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * 将文件保存到本地
     */
    public static void saveFileCache(byte[] fileData, String folderPath, String fileName) {
        File folder = new File(folderPath);
        folder.mkdirs();
        File file = new File(folderPath, fileName);
        ByteArrayInputStream is = new ByteArrayInputStream(fileData);
        OutputStream os = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
                os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    os.write(buffer, 0, len);
                }
                os.flush();
            } catch (Exception e) {
                throw new RuntimeException(FileUtils.class.getClass().getName(), e);
            } finally {
                closeIO(is, os);
            }
        }
    }

    /**
     * 从指定文件夹获取文件
     *
     * @return 如果文件不存在则创建, 如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator + fileNmae);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 获取SD卡下指定文件夹的绝对路径
     *
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    /**
     * 获取文件夹对象
     *
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + folderName + File.separator);
        file.mkdirs();
        return file;
    }

    /**
     * 输入流转byte[]<br>
     * <p/>
     * <b>注意</b> 你必须手动关闭参数inStream
     */
    public static final byte[] input2byte(InputStream inStream) {
        if (inStream == null) {
            return null;
        }
        byte[] in2b = null;
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in2b = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(swapStream);
        }
        return in2b;
    }

    /**
     * 把uri转为File对象
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    public static File uri2File(Activity aty, Uri uri) {
        if (Build.VERSION.SDK_INT < 11) {
            // 在API11以下可以使用：managedQuery
            String[] proj =
                    {MediaStore.Images.Media.DATA};
            @SuppressWarnings("deprecation")
            Cursor actualimagecursor = aty.managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            return new File(img_path);
        } else {
            // 在API11以上：要转为使用CursorLoader,并使用loadInBackground来返回
            String[] projection =
                    {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(aty, uri, projection, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        }
    }

    public static void copyDirectories(String srcPath, String destPath) {
        //创建目标文件夹
        (new File(destPath)).mkdirs();
        //获取源文件夹当前下的文件或目录
        File[] file = (new File(srcPath)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                //复制文件
                copyFile(file[i], new File(destPath + file[i].getName()));
            }
            if (file[i].isDirectory()) {
                //复制目录
                String sorceDir = srcPath + File.separator + file[i].getName();
                String targetDir = destPath + File.separator + file[i].getName();
                try {
                    copyDirectiory(sorceDir, targetDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制文件
     *
     * @param from
     * @param to
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            return;
        }
        if (null == to) {
            return;
        }
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);
            copyFileFast(is, os);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getClass().getName(), e);
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 快速复制文件（采用nio操作）
     *
     * @param is 数据来源
     * @param os 数据目标
     * @throws IOException
     */
    public static void copyFileFast(FileInputStream is, FileOutputStream os) throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0, in.size(), out);
    }

    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {

        //新建目标目录

        (new File(targetDir)).mkdirs();

        //获取源文件夹当下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();

        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                //源文件
                File sourceFile = file[i];
                //目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());

                copyFile(sourceFile, targetFile);

            }


            if (file[i].isDirectory()) {
                //准备复制的源文件夹
                String dir1 = sourceDir + file[i].getName();
                //准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();

                copyDirectiory(dir1, dir2);
            }
        }

    }

    /**
     * 关闭流
     *
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException(FileUtils.class.getClass().getName(), e);
            }
        }
    }

    /**
     * 图片写入文件
     *
     * @param bitmap   图片
     * @param filePath 文件路径
     * @return 是否写入成功
     */
    public static boolean bitmapToFile(Bitmap bitmap, String filePath) {
        boolean isSuccess = false;
        if (bitmap == null) {
            return isSuccess;
        }
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(filePath), 8 * 1024);
            isSuccess = bitmap.compress(CompressFormat.PNG, 90, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeIO(out);
        }
        return isSuccess;
    }

    /**
     * 从文件中读取文本
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName() + "readFile---->" + filePath + " not found");
        }
        return inputStream2String(is);
    }

    /**
     * 从assets中读取文本
     *
     * @param name
     * @return
     */
    public static String readFileFromAssets(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(name);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName() + ".readFileFromAssets---->" + name + " not found");
        }
        return inputStream2String(is);
    }


    /**
     * 输入流转字符串
     *
     * @param is
     * @return 一个流中的字符串
     */
    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch (Exception ex) {
        } finally {
            closeIO(is);
        }
        return null == resultSb ? null : resultSb.toString();
    }

    public static AssetManager getAssetDir(Context context) {
        AssetManager manager = context.getAssets();
        return manager;
    }

    public static boolean isSdCardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * @return
     * @throws
     * @Title: getSDFreeSize
     * @Description: 查看SD卡的剩余空间
     * @author Allick
     */
    public long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * @return
     * @throws
     * @Title: getSDAllSize
     * @Description: 查看SD卡总容量
     * @author Allick
     */
    public long getSDAllSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        // return allBlocks * blockSize; //单位Byte
        // return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }


    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        }
        else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 检查文件是否存在
     */
    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }


    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    public static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }


    private static String APP_ROOT_PATH = "/T11/";
    private static String APP_CACHE_DIR = "cache";
    private static String APP_IMG_DIR = "img";
    private static String APP_VIDEO_DIR = "video";
    private static String APP_APK_DIR="apk";
    private static String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory() + APP_ROOT_PATH;

    /**
     * 自动创建app储存路径，并返回
     * 当外部储存可以用的时候 /mnt/sdcard/app/folder
     * 外部储存不能使用的时候
     *
     * @param context
     * @param folder  无需带'/'
     * @return
     */
    public static File getAppStoragePath(Context context, String folder) {

        File file = null;
        if (isExternalStorageWritable() && isExternalStorageReadable()) {
            file = new File(EXTERNAL_STORAGE, folder);
        } else {
            file = new File(context.getApplicationContext().getFilesDir() + APP_ROOT_PATH, folder);
        }

        if (!file.exists()) {
            boolean maked = file.mkdirs();
        }

        return file;

    }

    public static File getAppImgPath(Context context){
        return getAppStoragePath(context, APP_IMG_DIR);
    }

    public static File getVideoFile(Context context){
        return getAppStoragePath(context, APP_VIDEO_DIR);
    }
    public static File getAPKFile(Context context){
        return getAppStoragePath(context, APP_APK_DIR);
    }


    /**
     * 缓存区大小默认20480
     */
    private final static int FILE_BUFFER_SIZE = 20480;

    /**
     * 将指定目录的ZIP压缩文件解压到指定的目录
     *
     * @param zipFilePath   ZIP压缩文件的路径
     * @param zipFileName   ZIP压缩文件名字
     * @param targetFileDir ZIP压缩文件要解压到的目录
     * @return flag    布尔返回值
     */
    public static boolean unZip(String zipFilePath, String zipFileName, String targetFileDir) {
        boolean flag = false;
        //1.判断压缩文件是否存在，以及里面的内容是否为空
        File file = null;   //压缩文件(带路径)
        ZipFile zipFile = null;
        file = new File(zipFilePath + "/" + zipFileName);
        LogUtils.d(">>>>>>解压文件【" + zipFilePath + "/" + zipFileName + "】到【" + targetFileDir + "】目录下<<<<<<");
        if (false == file.exists()) {
            LogUtils.d(">>>>>>压缩文件【" + zipFilePath + "/" + zipFileName + "】不存在<<<<<<");
            return false;
        } else if (0 == file.length()) {
            LogUtils.d(">>>>>>压缩文件【" + zipFilePath + "/" + zipFileName + "】大小为0不需要解压<<<<<<");
            return false;
        } else {
            //2.开始解压ZIP压缩文件的处理
            byte[] buf = new byte[FILE_BUFFER_SIZE];
            int readSize = -1;
            ZipInputStream zis = null;
            FileOutputStream fos = null;
            try {
                // 检查是否是zip文件
                zipFile = new ZipFile(file);
                zipFile.close();
                // 判断目标目录是否存在，不存在则创建
                File newdir = new File(targetFileDir);
                if (false == newdir.exists()) {
                    newdir.mkdirs();
                    newdir = null;
                }
                zis = new ZipInputStream(new FileInputStream(file));
                ZipEntry zipEntry = zis.getNextEntry();
                // 开始对压缩包内文件进行处理
                while (null != zipEntry) {
                    String zipEntryName = zipEntry.getName().replace('\\', '/');
                    //判断zipEntry是否为目录，如果是，则创建
                    if (zipEntry.isDirectory()) {
                        int indexNumber = zipEntryName.lastIndexOf('/');
                        File entryDirs = new File(targetFileDir + "/" + zipEntryName.substring(0, indexNumber));
                        entryDirs.mkdirs();
                        entryDirs = null;
                    } else {
                        try {
                            fos = new FileOutputStream(targetFileDir + "/" + zipEntryName);
                            while ((readSize = zis.read(buf, 0, FILE_BUFFER_SIZE)) != -1) {
                                fos.write(buf, 0, readSize);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(e.getCause());
                        } finally {
                            try {
                                if (null != fos) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e.getCause());
                            }
                        }
                    }
                    zipEntry = zis.getNextEntry();
                }
                flag = true;
            } catch (ZipException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getCause());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getCause());
            } finally {
                try {
                    if (null != zis) {
                        zis.close();
                    }
                    if (null != fos) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getCause());
                }
            }
        }
        return flag;
    }

    /**
     * 将指定目录的ZIP压缩文件解压到指定的目录
     *
     * @param zipFileName   ZIP压缩文件名字
     * @param targetFileDir ZIP压缩文件要解压到的目录
     * @return flag    布尔返回值
     */
    public static boolean unZip(String zipFileName, String targetFileDir) {
        boolean flag = false;
        //1.判断压缩文件是否存在，以及里面的内容是否为空
        File file = null;   //压缩文件(带路径)
        ZipFile zipFile = null;
        file = new File(zipFileName);
        LogUtils.d(">>>>>>解压文件【" + zipFileName + "】到【" + targetFileDir + "】目录下<<<<<<");
        if (!file.exists()) {
            LogUtils.d(">>>>>>压缩文件【" + "/" + zipFileName + "】不存在<<<<<<");
            return false;
        } else if (0 == file.length()) {
            LogUtils.d(">>>>>>压缩文件【" + "/" + zipFileName + "】大小为0不需要解压<<<<<<");
            return false;
        } else {
            //2.开始解压ZIP压缩文件的处理
            byte[] buf = new byte[FILE_BUFFER_SIZE];
            int readSize = -1;
            ZipInputStream zis = null;
            FileOutputStream fos = null;
            try {
                // 检查是否是zip文件
                zipFile = new ZipFile(file);
                zipFile.close();
                // 判断目标目录是否存在，不存在则创建
                File newdir = new File(targetFileDir);
                if (!newdir.exists()) {
                    newdir.mkdirs();
                    newdir = null;
                }
                zis = new ZipInputStream(new FileInputStream(file));
                ZipEntry zipEntry = zis.getNextEntry();
                // 开始对压缩包内文件进行处理
                while (null != zipEntry) {
                    String zipEntryName = zipEntry.getName().replace('\\', '/');
                    //判断zipEntry是否为目录，如果是，则创建
                    if (zipEntry.isDirectory()) {


                        int indexNumber = zipEntryName.lastIndexOf('/');
                        File entryDirs = new File(targetFileDir + "/" + zipEntryName.substring(0, indexNumber));
                        entryDirs.mkdirs();
                        entryDirs = null;
                    } else {
                        fos = new FileOutputStream(targetFileDir + "/" + zipEntryName);
                        while ((readSize = zis.read(buf, 0, FILE_BUFFER_SIZE)) != -1) {
                            fos.write(buf, 0, readSize);
                        }
                    }
                    zipEntry = zis.getNextEntry();
                }
                flag = true;
            } catch (Exception e) {
                LogUtils.d("unZip mothed " + e.getMessage());
            } finally {
                try {
                    if (null != zis) {
                        zis.close();
                    }
                    if (null != fos) {
                        fos.close();
                    }

                } catch (Exception e) {
                    LogUtils.d("unZip method finally " + e.getMessage());
                }
            }
        }
        return flag;
    }

    public static String getCacheSize(Context context) {

        long size = 0;
        // sd卡目录？
        // cache目录
        File sdFileFolder = new File(EXTERNAL_STORAGE + APP_CACHE_DIR);
        if (sdFileFolder != null)
            size += dirSize(sdFileFolder);

        File cacheFileFolder = context.getCacheDir();
        if (cacheFileFolder != null)
            size += dirSize(cacheFileFolder);

        return DigitFormatUtils.formatDecimal((float)size / 1024 / 1024, 2) + "M";
//        return android.text.format.Formatter.formatFileSize(context, size);

    }

    public static void deleteCache(Context context) {

        File sdFileFolder = new File(EXTERNAL_STORAGE);
        if (sdFileFolder != null)
            delFiles(sdFileFolder);

        File cacheFileFolder = context.getCacheDir();
        if (cacheFileFolder != null)
            delFiles(cacheFileFolder);

    }

    public static long dirSize(File dir) {

        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            if(fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    // Recursive call if it's a directory
                    if (fileList[i].isDirectory()) {
                        result += dirSize(fileList[i]);
                    } else {
                        // Sum the file size in bytes
                        result += fileList[i].length();
                    }
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    private static void delFiles(File dir) {

        if (dir.exists()) {
            File[] fileList = dir.listFiles();
            if(fileList == null)
                return;
            for (int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if (fileList[i].isDirectory()) {
                    delFiles(fileList[i]);
                } else {
                    // Sum the file size in bytes
                    boolean delete = fileList[i].delete();
                    if (!delete) {
                        LogUtils.e("delete failed " + fileList[i]);
                    }
                }
            }
        }
    }

    /**
     * 读取文件的内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String readFile(File file) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result += s;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}