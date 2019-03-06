package imageprocess;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 对图像进行灰度化处理
 * @author lijinhua
 *
 */
public class ChangeBmpToYuv {
	public static void main(String[] args) {
		String fileName = "screen.bmp";

		convert2YUV(fileName, "screenYUV.bmp");
	}
	
	/**
	 *      对24位真彩图bmp进行灰度化处理
	 * @param originFile 原彩色图路径
	 * @param toFile 生成的灰度图的路径
	 */
	private static void convert2YUV(String originFile, String toFile) {
		byte[] bmpImage = readBmp(originFile);
		// 为从文件头到实际的位图数据的偏移字节数，即图 1.3 中前三个部分的长度之和
		int bfOffBits = Int2Bytes.bytes2Int(bmpImage, 10);
		// 图片宽几个像素
		int biWidth = Int2Bytes.bytes2Int(bmpImage, 18);
		// 图片宽几个像素
		int biHeight = Int2Bytes.bytes2Int(bmpImage, 22);
		 // 指定表示颜色时要用到的位数，常用的值为1(黑白二色图), 4(16 色图), 8(256 色),
        // 24(真彩色图)(新的.bmp 格式支持32 位色，这里就不做讨论了)。
		short biBitCount = Int2Bytes.bytes2Short(bmpImage, 28);
		
		System.out.printf("bfType=%x \n", Int2Bytes.bytes2Short(bmpImage, 0));
		System.out.println("bfSize=" + Int2Bytes.bytes2Int(bmpImage, 2));
		System.out.println("bfOffBits=" + bfOffBits);
		System.out.println("biSize=" + Int2Bytes.bytes2Int(bmpImage, 14));
		System.out.println("biWidth=" + biWidth);
		System.out.println("biHeight=" + biHeight);
		System.out.println("biBitCount=" + biBitCount);
		
		// 这是针对24位真彩图的处理算法
//		先看看真彩图。我们知道真彩图不带调色板，每个象素用3 个字节，表示R、G、
//		B 三个分量。所以处理很简单，根据R、G、B 的值求出Y 值后，将R、G、B
//		值都赋值成Y，写入新图即可。
		for(int i = bfOffBits; i < bmpImage.length; i += 3) {
			byte blue = (byte)(bmpImage[i] & 0xFF);
			byte green = (byte)(bmpImage[i + 1] & 0xFF);
			byte red = (byte)(bmpImage[i + 2] & 0xFF);
			// yuv
			byte y = (byte)(0.299 * red + 0.587 * green + 0.114 * blue);
			bmpImage[i] = y;
			bmpImage[i + 1] = y;
			bmpImage[i + 2] = y;
		}
		
		// 对于带调色板的bmp图的算法：
//		再来看看带调色板的彩色图，我们知道位图中的数据只是对应调色板中的一个索
//		引值，我们只需要将调色板中的彩色变成灰度，形成新调色板，而位图数据不用
//		动，就可以了。
		
		// 写入文件
		 try {
			File file = new File(toFile);
			 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			 bufferedOutputStream.write(bmpImage);
			 bufferedOutputStream.flush();
			 bufferedOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从bmp文件读取字节
	 * @param fileName bmp 文件
	 * @return 代表bmp文件的字节数组
	 */
	private static byte[] readBmp(String fileName) {
		try {
			File file = new File(fileName);
			byte[] bmpImage = new byte[(int)file.length()];
			byte[] temp = new byte[10 * 1024];
			int index = 0;
			int len = -1;
			FileInputStream fileInputStream = new FileInputStream(file);
			while((len = fileInputStream.read(temp)) != -1) {
				System.arraycopy(temp, 0, bmpImage, index, len);
				index += len;
			}	
			fileInputStream.close();
			
			return bmpImage;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
