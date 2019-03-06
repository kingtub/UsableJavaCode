package imageprocess;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ��ͼ����лҶȻ�����
 * @author lijinhua
 *
 */
public class ChangeBmpToYuv {
	public static void main(String[] args) {
		String fileName = "screen.bmp";

		convert2YUV(fileName, "screenYUV.bmp");
	}
	
	/**
	 *      ��24λ���ͼbmp���лҶȻ�����
	 * @param originFile ԭ��ɫͼ·��
	 * @param toFile ���ɵĻҶ�ͼ��·��
	 */
	private static void convert2YUV(String originFile, String toFile) {
		byte[] bmpImage = readBmp(originFile);
		// Ϊ���ļ�ͷ��ʵ�ʵ�λͼ���ݵ�ƫ���ֽ�������ͼ 1.3 ��ǰ�������ֵĳ���֮��
		int bfOffBits = Int2Bytes.bytes2Int(bmpImage, 10);
		// ͼƬ��������
		int biWidth = Int2Bytes.bytes2Int(bmpImage, 18);
		// ͼƬ��������
		int biHeight = Int2Bytes.bytes2Int(bmpImage, 22);
		 // ָ����ʾ��ɫʱҪ�õ���λ�������õ�ֵΪ1(�ڰ׶�ɫͼ), 4(16 ɫͼ), 8(256 ɫ),
        // 24(���ɫͼ)(�µ�.bmp ��ʽ֧��32 λɫ������Ͳ���������)��
		short biBitCount = Int2Bytes.bytes2Short(bmpImage, 28);
		
		System.out.printf("bfType=%x \n", Int2Bytes.bytes2Short(bmpImage, 0));
		System.out.println("bfSize=" + Int2Bytes.bytes2Int(bmpImage, 2));
		System.out.println("bfOffBits=" + bfOffBits);
		System.out.println("biSize=" + Int2Bytes.bytes2Int(bmpImage, 14));
		System.out.println("biWidth=" + biWidth);
		System.out.println("biHeight=" + biHeight);
		System.out.println("biBitCount=" + biBitCount);
		
		// �������24λ���ͼ�Ĵ����㷨
//		�ȿ������ͼ������֪�����ͼ������ɫ�壬ÿ��������3 ���ֽڣ���ʾR��G��
//		B �������������Դ���ܼ򵥣�����R��G��B ��ֵ���Y ֵ�󣬽�R��G��B
//		ֵ����ֵ��Y��д����ͼ���ɡ�
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
		
		// ���ڴ���ɫ���bmpͼ���㷨��
//		������������ɫ��Ĳ�ɫͼ������֪��λͼ�е�����ֻ�Ƕ�Ӧ��ɫ���е�һ����
//		��ֵ������ֻ��Ҫ����ɫ���еĲ�ɫ��ɻҶȣ��γ��µ�ɫ�壬��λͼ���ݲ���
//		�����Ϳ����ˡ�
		
		// д���ļ�
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
	 * ��bmp�ļ���ȡ�ֽ�
	 * @param fileName bmp �ļ�
	 * @return ����bmp�ļ����ֽ�����
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
