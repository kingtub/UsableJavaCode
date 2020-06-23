package imageprocess;

import java.io.*;
import java.util.Arrays;

/**
 * ��ͼ����лҶȻ�����������Щ���⣬ͼ���λ��
 * @author lijinhua
 *
 */
public class ChangeBmpToYuv2 {
	public static void main(String[] args) {
		String fileName = "screen.bmp";

		convert2YUV(fileName, "screenYUV2.bmp");
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


		int biWidth2;
		int biHeight2;
		if(biWidth % 4 == 0) {
			biWidth2 = biWidth;
		} else {
			biWidth2 = (biWidth / 4 + 1) * 4;
		}

		if(biHeight % 4 == 0) {
			biHeight2 = biHeight;
		} else {
			biHeight2 = (biHeight / 4 + 1) * 4;
		}

		int newBfSize = 54 + 256 * 4 + biWidth2 * biHeight2;
		short newBiBitCount = 8;
		int newBfOffBits = 54;
		byte[] palette = new byte[256 * 4];
		Arrays.fill(palette, (byte)255);
		int j = 0;
		for(int i = 0; i < 256; i++) {
			palette[j] = (byte)i;
			palette[j + 1] = (byte)i;
			palette[j + 2] = (byte)i;
			palette[j + 3] = (byte)0;
			j += 4;
		}

		byte[] n_bmp = new byte[newBfSize];
		System.arraycopy(bmpImage, 0, n_bmp, 0, 54);

		System.arraycopy(Int2Bytes.int2Bytes(newBfSize), 0, n_bmp, 2, 4);
		System.arraycopy(Int2Bytes.short2Bytes(newBiBitCount), 0, n_bmp, 28, 2);
		System.arraycopy(Int2Bytes.int2Bytes(newBfOffBits), 0, n_bmp, 10, 4);
		System.arraycopy(palette, 0, n_bmp, 54, palette.length);



		// �������24λ���ͼ�Ĵ����㷨
//		�ȿ������ͼ������֪�����ͼ������ɫ�壬ÿ��������3 ���ֽڣ���ʾR��G��
//		B �������������Դ���ܼ򵥣�����R��G��B ��ֵ���Y ֵ�󣬽�R��G��B
//		ֵ����ֵ��Y��д����ͼ���ɡ�

		int tempIndex = 54 + 256 * 4;
//		int start;
//		for(int i = 0; i < biWidth * biHeight; i++) {
//			start = bfOffBits + 3 * i;
//
//			byte blue = (byte)(bmpImage[start] & 0xFF);
//			byte green = (byte)(bmpImage[start + 1] & 0xFF);
//			byte red = (byte)(bmpImage[start + 2] & 0xFF);
//			// yuv
//			byte y = (byte)(0.299 * red + 0.587 * green + 0.114 * blue);
//			n_bmp[tempIndex] = y;
//			tempIndex++;
//		}

		int c = 0;
		for(int i = 0; i < biHeight2; i++) {
			if(i >= biHeight) {
				tempIndex += biWidth2;
				c += biWidth2;
				continue;
			}
			for(int k = 0; k < biWidth2; k++) {
				if(k >= biWidth) {
					tempIndex++;
					c++;
					continue;
				}

				int n = bfOffBits + 3 * c;

				byte blue = (byte) (bmpImage[n] & 0xFF);
				byte green = (byte) (bmpImage[n + 1] & 0xFF);
				byte red = (byte) (bmpImage[n + 2] & 0xFF);
				// yuv
				byte y = (byte) (0.299 * red + 0.587 * green + 0.114 * blue);
				n_bmp[tempIndex] = y;

				tempIndex++;
				c++;
			}
		}
		
		// ���ڴ���ɫ���bmpͼ���㷨��
//		������������ɫ��Ĳ�ɫͼ������֪��λͼ�е�����ֻ�Ƕ�Ӧ��ɫ���е�һ����
//		��ֵ������ֻ��Ҫ����ɫ���еĲ�ɫ��ɻҶȣ��γ��µ�ɫ�壬��λͼ���ݲ���
//		�����Ϳ����ˡ�
		
		// д���ļ�
		 try {
			File file = new File(toFile);
			 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			 bufferedOutputStream.write(n_bmp);
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
