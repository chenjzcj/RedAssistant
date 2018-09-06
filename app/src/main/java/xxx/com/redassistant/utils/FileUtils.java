package xxx.com.redassistant.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.blankj.utilcode.util.SDCardUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by MZIA(527633405@qq.com) on 2016/6/2 0002 11:12
 * 文件操作工具类
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    private static final int BUFFER = 8192;
    private static final long ONE_DAY_MILLIS = 0x5265c00L;
    public static String audioPath;

    /**
     * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @param context  上下文
     * @param fileName 文件名
     * @param content  文本内容
     */
    public static void write(Context context, String fileName, String content) {
        if (content == null) content = "";
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取/data/data/PACKAGE_NAME/files 目录下的文本文件
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return 读取出来的内容
     */
    public static String read(Context context, String fileName) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            return readInStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读取输入流信息
     *
     * @param inStream 输入流
     * @return 从输入流中读取的内容
     */
    public static String readInStream(InputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 在给定的目录内创建文件
     *
     * @param folderPath 文件目录
     * @param fileName   文件名
     * @return 创建的文件
     */
    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName);
    }

    /**
     * 向手机sdcard/picture目录下写图片
     *
     * @param context  上下文
     * @param buffer   图片字节码
     * @param fileName 文件名
     * @return 是否写成功, true表示写成功
     */
    public static boolean writeFile(Context context, byte[] buffer, String fileName) {
        File fileDir;
        if (SDCardUtils.isSDCardEnable()) {
            fileDir = PathUtils.getInstance().getExternalFilesDir(context, Environment.DIRECTORY_PICTURES);
        } else {
            return false;
        }
        File file = new File(fileDir, fileName);
        if (file.exists()) {
            return false;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 根据文件绝对路径获取文件名,包含扩展名
     *
     * @param absolutePath 文件绝对路径
     * @return 包含扩展名的文件名
     */
    public static String getFileNameByAbsolutePath(String absolutePath) {
        if (MyTextUtils.isEmpty(absolutePath)) return "";
        return absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 根据文件的绝对路径获取文件名但不包含扩展名
     *
     * @param absolutePath 文件绝对路径
     * @return 不包含扩展名的文件名
     */
    public static String getFileNameNoFormat(String absolutePath) {
        if (MyTextUtils.isEmpty(absolutePath)) return "";
        int point = absolutePath.lastIndexOf('.');
        return absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1, point);
    }

    /**
     * 根据文件的绝对路径获取文件扩展名
     *
     * @param absolutePath 文件绝对路径
     * @return 文件扩展名
     */
    public static String getFileFormat(String absolutePath) {
        if (MyTextUtils.isEmpty(absolutePath)) return "";
        int point = absolutePath.lastIndexOf('.');
        return absolutePath.substring(point + 1);
    }

    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return 文件大小
     */
    public static long getFileSize(String filePath) {
        long size = 0;
        File file = new File(filePath);
        if (file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 获取文件大小,带单位
     *
     * @param size 字节
     * @return 文件大小, 带单位
     */
    public static String getFileSize(long size) {
        if (size <= 0) return "0";
        DecimalFormat df = new DecimalFormat("##.##");
        float temp = (float) size / 1024;
        if (temp >= 1024) {
            return df.format(temp / 1024) + "M";
        } else {
            return df.format(temp) + "K";
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileS long
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir 需要查看的目录
     * @return 目录文件大小
     */
    public static long getDirSize(File dir) {
        if (dir == null || !dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 获取指定目录中的文件个数
     *
     * @param dir 需要查询文件个数的目录
     * @return 该目录下文件的个数
     */
    public long getFileList(File dir) {
        if (dir == null || !dir.isDirectory()) {
            return 0;
        }
        File[] files = dir.listFiles();
        long count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileList(file);// 递归
                count--;
            }
        }
        return count;
    }

    /**
     * 将输入流转换成字节数组
     *
     * @param in 需要被转换的输入流
     * @return 字节数组
     * @throws IOException
     */
    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            out.write(ch);
        }
        byte buffer[] = out.toByteArray();
        out.close();
        return buffer;
    }

    /**
     * 检查文件是否存在
     *
     * @param name
     * @return
     */
    public static boolean checkFileExists(String name) {
        boolean status;
        if (!name.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + name);
            status = newPath.exists();
        } else {
            status = false;
        }
        return status;
    }


    /**
     * 新建目录
     *
     * @param directoryName
     * @return
     */
    public static boolean createDirectory(String directoryName) {
        boolean status;
        if (!directoryName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + directoryName);
            status = newPath.mkdir();
            status = true;
        } else status = false;
        return status;
    }

    /**
     * 检查是否安装外置的SD卡
     *
     * @return
     */
    public static boolean checkExternalSDExists() {
        Map<String, String> evn = System.getenv();
        return evn.containsKey("SECONDARY_STORAGE");
    }

    /**
     * 删除sdcard/下的目录(包括：目录里的所有文件)
     *
     * @param dirName 目录名称
     * @return true为删除成功
     */
    public static boolean deleteDirectory(String dirName) {
        boolean status;
        SecurityManager checker = new SecurityManager();
        if (!dirName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path, dirName);
            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listfile = newPath.list();
                try {
                    for (String aListfile : listfile) {
                        File deletedFile = new File(newPath.toString() + "/" + aListfile);
                        deletedFile.delete();
                    }
                    newPath.delete();
                    status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    status = false;
                }

            } else status = false;
        } else status = false;
        return status;
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (!fileName.equals("")) {

            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if (newPath.isFile()) {
                try {
                    newPath.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            } else status = false;
        } else status = false;
        return status;
    }

    /**
     * 删除空目录
     * <p/>
     * 返回 0代表成功 ,1 代表没有删除权限, 2代表不是空目录,3 代表未知错误
     *
     * @return
     */
    public static int deleteBlankPath(String path) {
        File f = new File(path);
        if (!f.canWrite()) {
            return 1;
        }
        if (f.list() != null && f.list().length > 0) {
            return 2;
        }
        if (f.delete()) {
            return 0;
        }
        return 3;
    }

    /**
     * 重命名
     *
     * @param oldName
     * @param newName
     * @return
     */
    public static boolean reNamePath(String oldName, String newName) {
        File f = new File(oldName);
        return f.renameTo(new File(newName));
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static boolean deleteFileWithPath(String filePath) {
        SecurityManager checker = new SecurityManager();
        File f = new File(filePath);
        checker.checkDelete(filePath);
        if (f.isFile()) {
            f.delete();
            return true;
        }
        return false;
    }

    /**
     * 清空一个文件夹
     *
     * @param filePath
     */
    public static void clearFileWithPath(String filePath) {
        List<File> files = FileUtils.listPathFiles(filePath);
        if (files != null && files.isEmpty()) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                clearFileWithPath(f.getAbsolutePath());
            } else {
                f.delete();
            }
        }
    }

    /**
     * 获取SD卡的根目录
     *
     * @return
     */
    public static String getSDRoot() {

        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取手机外置SD卡的根目录
     *
     * @return
     */
    public static String getExternalSDRoot() {

        Map<String, String> evn = System.getenv();

        return evn.get("SECONDARY_STORAGE");
    }

    /**
     * 列出root目录下所有子目录
     *
     * @param root
     * @return 绝对路径
     */
    public static List<String> listPath(String root) {
        List<String> allDir = new ArrayList<String>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        // 过滤掉以.开始的文件夹
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory() && !f.getName().startsWith(".")) {
                    allDir.add(f.getAbsolutePath());
                }
            }
        }
        return allDir;
    }

    /**
     * 获取一个文件夹下的所有文件
     *
     * @param root
     * @return
     */
    public static List<File> listPathFiles(String root) {
        String pathString = Environment.getExternalStorageDirectory() + File.separator + root;
        List<File> allDir = new ArrayList<File>();
        SecurityManager checker = new SecurityManager();
        File path = new File(pathString);
        checker.checkRead(pathString);
        File[] files = path.listFiles();
        for (File f : files) {
            if (f.isFile()) allDir.add(f);
            else listPath(f.getAbsolutePath());
        }
        return allDir;
    }

    public enum PathStatus {
        SUCCESS, EXITS, ERROR
    }

    /**
     * 创建目录
     *
     * @param newPath
     */
    public static PathStatus createPath(String newPath) {
        File path = new File(newPath);
        if (path.exists()) {
            return PathStatus.EXITS;
        }
        if (path.mkdir()) {
            return PathStatus.SUCCESS;
        } else {
            return PathStatus.ERROR;
        }
    }

    /**
     * 截取路径名
     *
     * @return
     */
    public static String getPathName(String absolutePath) {
        int start = absolutePath.lastIndexOf(File.separator) + 1;
        int end = absolutePath.length();
        return absolutePath.substring(start, end);
    }

    /**
     * 获取应用程序缓存文件夹下的指定目录
     *
     * @param context
     * @param dir
     * @return
     */
    public static String getAppCache(Context context, String dir) {
        String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir + "/";
        File savedir = new File(savePath);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        return savePath;
    }

    public static InputStream byteToInputSteram(byte abyte0[]) {
        ByteArrayInputStream bytearrayinputstream = null;
        if (abyte0 != null && abyte0.length > 0)
            bytearrayinputstream = new ByteArrayInputStream(abyte0);
        return bytearrayinputstream;
    }

    public static void combineTextFile(File[] files, File file) throws IOException {
        if (file != null && files != null) {
            BufferedReader brSource = null;
            BufferedWriter brTarget = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            for (int i = 0; i < files.length; i++) {
                brSource = new BufferedReader(new InputStreamReader(new FileInputStream(files[i])));
                String s = brSource.readLine();
                String s1;
                while ((s1 = brSource.readLine()) != null) {
                    brTarget.write(s);
                    brTarget.newLine();
                    s = s1;
                }
                brTarget.write(s);
                if (i != -1 + files.length) brTarget.newLine();
                brTarget.flush();
            }
            if (brSource != null) brSource.close();
            if (brTarget != null) brTarget.close();
        }
    }

    public static void copy(File source, File target) throws IOException {
        if (source != null && !source.exists())
            Log.i(TAG, "the source file is not exists: " + source.getAbsolutePath());
        else if (source.isFile()) copyFile(source, target);
        else copyDirectory(source, target);
    }

    public static void copyDirectory(File sourceDir, File targetDir) throws IOException {
        targetDir.mkdirs();
        if (sourceDir != null) {
            File[] f = sourceDir.listFiles();
            for (int i = 0; i < f.length; i++) {
                if (f[i].isFile()) {
                    copyFile(f[i], new File((new StringBuilder(String.valueOf(targetDir.getAbsolutePath()))).append(File.separator).append(f[i].getName()).toString()));
                } else if (f[i].isDirectory()) {
                    copyDirectory(new File(sourceDir, f[i].getName()), new File(targetDir, f[i].getName()));
                }
            }
        }
    }

    public static void copyFile(File source, File target) throws IOException {
        if (source != null && target != null) {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));
            try {
                byte[] buffer = new byte[BUFFER];
                int i = -1;
                while ((i = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, i);
                }
                bos.flush();
            } finally {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            }
        }
    }

    public static boolean delete(File file) {
        boolean flag;
        if (file != null && !file.exists()) {
            Log.i(TAG, "the file is not exists: " + file.getAbsolutePath());
            return false;
        } else if (file != null && file.isFile()) flag = deleteFile(file);
        else flag = deleteDirectory(file, true);
        return flag;
    }

    public static boolean deleteDirectory(File file, boolean flag) {
        return deleteDirectory(file, null, flag, false);
    }

    public static boolean deleteDirectory(File file, String s, boolean flag, boolean flag1) {
        if (file == null) {
            return false;
        }
        if (!file.exists() || !file.isDirectory()) {
            Log.i(TAG, "the directory is not exists: " + file.getAbsolutePath());
            return false;
        }
        boolean flag3 = true;
        File[] f = file.listFiles();
        for (int i = 0; i < f.length; i++) {
            if (f[i].isFile()) {
                if (s == null || f[i].getName().toLowerCase().endsWith("." + s.toLowerCase())) {
                    flag3 = deleteFile(f[i]);
                    if (!flag3) {
                        break;
                    }
                }
            } else {
                if (!flag1) {
                    flag3 = deleteDirectory(f[i], true);
                    if (!flag3) {
                        break;
                    }
                }// goto _L7; else goto _L9
            }// goto _L6; else goto _L5
        }
        if (!flag3) {
            Log.i(TAG, "delete directory fail: " + file.getAbsolutePath());
        } else if (flag) {
            if (file.delete()) return true;
            else Log.i(TAG, "delete directory fail: " + file.getAbsolutePath());
        } else {
            return true;
        }
        return false;
    }

    public static boolean deleteDirectoryByTime(File file, int time) {
        boolean flag = true;
        if ((file == null || file.exists()) && file.isDirectory()) {
            if (file != null) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        File f = files[i];
                        if (System.currentTimeMillis() - f.lastModified() - ONE_DAY_MILLIS * (long) time > 0L)
                            if (f.isDirectory()) flag = deleteDirectory(f, true);
                            else flag = delete(f);
                    }
                }
            }
        } else {
            Log.i(TAG, "the directory is not exists: " + file.getAbsolutePath());
            return false;
        }
        return flag;
    }

    public static boolean deleteFile(File file) {
        if (file != null && file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            Log.i(TAG, "the file is not exists: " + file.getAbsolutePath());
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public static long getAvailableStorageSize(File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            StatFs statfs = new StatFs(file.getPath());
            return statfs.getBlockSize() * (long) statfs.getAvailableBlocks();
        }
        return -1;
    }

    public static void move(File file, File file1) throws IOException {
        copy(file, file1);
        delete(file);
    }

    public static byte[] readFileToByte(File file) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        InputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        try {
            int i = -1;
            byte[] buffer = new byte[1024];
            while ((i = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, i);
            }
            return bos.toByteArray();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readTextFile(File file) throws IOException {
        String text = null;
        if (file != null) {
            FileInputStream fis = new FileInputStream(file);
            text = readTextInputStream(fis);
            if (fis != null) fis.close();
        }
        return text;
    }

    public static String readTextInputStream(InputStream is) throws IOException {
        if (is != null) {
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            if (br != null) br.close();
            return sb.toString();
        }
        return null;
    }

    public static int writeFile(File file, InputStream is) throws IOException {
        long l = 0;
        if (is != null && file != null) {
            byte[] abyte0 = new byte[BUFFER];
            OutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            DataInputStream dis = new DataInputStream(is);
            int i = -1;
            while ((i = dis.read(abyte0)) != -1) {
                dos.write(abyte0, 0, i);
                l += i;
            }
            if (dis != null) dis.close();
            if (dos != null) dos.close();
        }
        return (int) (l / 1024L);
    }

    public static void writeFile(File file, byte[] data) throws Exception {
        if (file != null && data != null) {
            OutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.write(data);
            dos.close();
        }
    }

    public static void writeTextFile(File file, String content) throws IOException {
        if (file != null) {
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.write(content.getBytes());
            dos.close();
        }
    }

    public static void writeTextFile(File file, String[] lines) throws IOException {
        String content = "";
        if (file != null && lines != null) {
            for (int i = 0; i < lines.length; i++) {
                content = new StringBuilder(content).append(lines[i]).toString();
                if (i != -1 + lines.length)
                    content = new StringBuilder(content).append("\r\n").toString();
            }
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream os = new DataOutputStream(fos);
            os.write(content.getBytes());
            os.close();
        }
    }

    public static String getRootFilePath() {
        if (SDCardUtils.isSDCardEnable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data/";
        }
    }

    public static String getSaveFilePath() {
        if (SDCardUtils.isSDCardEnable()) {
            return FileUtils.getRootFilePath() + "com.ran/files/";
        } else {
            return FileUtils.getRootFilePath() + "com.ran/files";
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param dirPath 需要清空的文件夹路径
     * @return true为清空成功
     */
    public static boolean delAllFile(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        String[] tempList = file.list();
        File temp;
        for (String aTempList : tempList) {
            if (dirPath.endsWith(File.separator)) {
                temp = new File(dirPath + aTempList);
            } else {
                temp = new File(dirPath + File.separator + aTempList);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(temp.getAbsolutePath());//先删除文件夹里面的文件
                temp.delete();
                return true;
            }
        }
        return false;
    }

    /**
     * 删除指定文件或者文件目录,无返回值
     *
     * @param file 需要被删除的文件或者文件目录
     */
    public static void deleteFileOrDir(File file) {
        if (file.exists() && file.canWrite()) {
            if (file.isFile()) {
                file.delete();
            } else {
                File[] listFiles = file.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    for (File listFile : listFiles) {
                        if (listFile.isFile() && listFile.canWrite()) {
                            listFile.delete();
                        } else {
                            deleteFileOrDir(listFile);
                        }
                    }
                }
            }
        }
    }

    /**
     * 删除指定路径的文件或者文件目录
     *
     * @param filePath 指定路径
     */
    public static void romoveFileOrDir(String filePath) {
        deleteFileOrDir(new File(filePath));
    }

    /**
     * 下载一个文件，不能放主线程里面做
     *
     * @param url  文件的网络下载地址
     * @param path 文件下载后本地存放地址
     * @return 失败返回false，成功返回true
     */
    public static boolean downloadFile(String url, String path) {
        try {
            String dir = path.substring(0, path.lastIndexOf(File.separator));
            new File(dir).mkdirs();

            URL Url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream is = conn.getInputStream();
            // 没有下载流
            if (is == null) {
                return false;
            }
            FileOutputStream os = new FileOutputStream(path);

            byte buf[] = new byte[1024];
            int length;
            while ((length = is.read(buf)) != -1) {
                os.write(buf, 0, length);
            }
            is.close();
            os.getFD().sync();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}