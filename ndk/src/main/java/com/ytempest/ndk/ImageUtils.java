package com.ytempest.ndk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author ytempest
 * 使用JPEG图片压缩算法对图片进行压缩
 */
public class ImageUtils {

    public static final int SUCCESS_COMPRESS = 1;
    public static final int ERR_UNKNOWN = -1;
    public static final int ERR_UN_SUPPORT_FORMAT = -2;
    public static final int ERR_OPEN_OUT_FILE = -3;

    static {
        System.loadLibrary("compress-img");
        System.loadLibrary("compress-img");
    }


    public static int compressImage(String srcPath, String destPath, int quality) {
        return compressBitmap(decodeFile(srcPath), destPath, quality);
    }

    public static int compressImage(String srcPath, String destPath) {
        return compressBitmap(decodeFile(srcPath), destPath, 85);
    }


    /**
     * 根据路径获取图片，并返回规定宽度的图片
     *
     * @param path 图片路径
     */
    public static Bitmap decodeFile(String path) {
        // 规定图片的宽度，按需求自行规定
        int finalWidth = 900;

        // 先获取宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 不加载图片到内存只拿宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int bitmapWidth = options.outWidth;

        int inSampleSize = 1;

        // 循环计算得到小于指定finalWidth高度的最终宽度
        while (bitmapWidth > finalWidth) {
            inSampleSize *= 2;
            bitmapWidth /= 2;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * NDK方法加载图片
     *
     * @param bitmap   要进行压缩图片的bitmap
     * @param quality  压缩的质量；数值越低压缩率越大，质量越差；数值越高压缩率越小，质量越高。一般用85比较合适
     * @param destPath 压缩后的路径
     * @return 压缩成功：{@link ImageUtils#SUCCESS_COMPRESS}，
     * 压缩失败：{@link ImageUtils#ERR_UNKNOWN,ImageUtils#ERR_UN_SUPPORT_FORMAT,ImageUtils#ERR_OPEN_OUT_FILE}
     */
    public native static int compressBitmap(Bitmap bitmap, String destPath, int quality);
}
