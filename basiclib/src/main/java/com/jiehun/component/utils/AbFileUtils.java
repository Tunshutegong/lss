package com.jiehun.component.utils;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.jiehun.component.config.BaseLibConfig;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

/**
 * 文件与流处理工具类<br>
 * <b>创建时间</b> 2014-8-14
 *
 * @version 1.1
 */
public final class AbFileUtils {
	/**
	 * 检测SD卡是否存在
	 */
	public static boolean checkSDcard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
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
				throw new RuntimeException(AbFileUtils.class.getClass().getName(), e);
			} finally {
				closeIO(is, os);
			}
		}
	}

	/**
	 * 从指定文件夹获取文件
	 *
	 * @return 如果文件不存在则创建,如果如果无法创建文件或文件名为空则返回null
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
	 * <b>注意</b> 你必须手动关闭参数inStream
	 */
	public static final byte[] input2byte(InputStream inStream) {
		if (inStream == null) { return null; }
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
	public static File uri2File(Activity aty, Uri uri) {
		if (AbSystemTool.getSDKVersion() < 11) {
			// 在API11以下可以使用：managedQuery
			String[] proj = { MediaStore.Images.Media.DATA };
			@SuppressWarnings("deprecation")
            Cursor actualimagecursor = aty.managedQuery(uri, proj, null, null, null);
			int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			actualimagecursor.moveToFirst();
			String img_path = actualimagecursor.getString(actual_image_column_index);
			return new File(img_path);
		} else {
			// 在API11以上：要转为使用CursorLoader,并使用loadInBackground来返回
			String[] projection = { MediaStore.Images.Media.DATA };
			CursorLoader loader = new CursorLoader(aty, uri, projection, null, null, null);
			Cursor cursor = loader.loadInBackground();
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return new File(cursor.getString(column_index));
		}
	}

	/**
	 * 复制文件
	 *
	 * @param from
	 * @param to
	 */
	public static void copyFile(File from, File to) {
		if (null == from || !from.exists()) { return; }
		if (null == to) { return; }
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
			throw new RuntimeException(AbFileUtils.class.getClass().getName(), e);
		} finally {
			closeIO(is, os);
		}
	}

	/**
	 * 快速复制文件（采用nio操作）
	 *
	 * @param is
	 *            数据来源
	 * @param os
	 *            数据目标
	 * @throws IOException
	 */
	public static void copyFileFast(FileInputStream is, FileOutputStream os) throws IOException {
		FileChannel in = is.getChannel();
		FileChannel out = os.getChannel();
		in.transferTo(0, in.size(), out);
	}

	/**
	 * 关闭流
	 *
	 * @param closeables
	 */
	public static void closeIO(Closeable... closeables) {
		if (null == closeables || closeables.length <= 0) { return; }
		for (Closeable cb : closeables) {
			try {
				if (null == cb) {
					continue;
				}
				cb.close();
			} catch (IOException e) {
				throw new RuntimeException(AbFileUtils.class.getClass().getName(), e);
			}
		}
	}

	/**
	 * 图片写入文件
	 *
	 * @param bitmap
	 *            图片
	 * @param filePath
	 *            文件路径
	 * @return 是否写入成功
	 */
	public static boolean bitmapToFile(Bitmap bitmap, String filePath) {
		boolean isSuccess = false;
		if (bitmap == null) { return isSuccess; }
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(filePath), 8 * 1024);
			isSuccess = bitmap.compress(CompressFormat.PNG, 70, out);
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
			throw new RuntimeException(AbFileUtils.class.getName() + "readFile---->" + filePath + " not found");
		}
		return inputStream2String(is);
	}

	public static String getAssetsString(String filePath){

		try {
			InputStream is = BaseLibConfig.getContext().getAssets().open(filePath);
			return inputStream2String(is);
		} catch (Exception e) {

		}
		return  null;

	}

	/**
	 * 取assets目录下文件path
	 * @param context
	 * @return
	 */
	public static String getAssetsPath(Context context, String fileName, String filePath) {
		File f = new File(context.getCacheDir() + "/" + fileName);
		if (!f.exists())
			try {
				InputStream is = context.getAssets().open(filePath);
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();

				FileOutputStream fos = new FileOutputStream(f);
				fos.write(buffer);
				fos.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		return f.getPath();
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
			throw new RuntimeException(AbFileUtils.class.getName() + ".readFileFromAssets---->" + name + " not found");
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
		if (null == is) { return null; }
		StringBuilder resultSb = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			resultSb = new StringBuilder();
			String len;
			while (null != (len = br.readLine())) {
				resultSb.append(len);
			}
		} catch (Exception ex) {} finally {
			closeIO(is);
		}
		return null == resultSb ? null : resultSb.toString();
	}

	public static String getMimeType(String fileUrl) throws IOException, MalformedURLException {
		String type = null;
		URL u = new URL(fileUrl);
		URLConnection uc = null;
		uc = u.openConnection();
		type = uc.getContentType();
		return type;
	}
}