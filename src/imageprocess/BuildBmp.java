package imageprocess;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildBmp {
    public static void main(String[] args) {
        try {
        	boolean b = true;
        	if(b) {
        		test2(0xff0000, 3);
        		return;
        	}
            byte[] bmpbytes = buildBMPImage();
            System.out.println(new String(bmpbytes, 0, 2));
            File file = new File("a.bmp");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bufferedOutputStream.write(bmpbytes);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void test2(int value, int len) {
    	byte[] bs = int2Bytes(value, len);
    	for(int i = 0; i < bs.length; i++) {
    		System.out.printf("%x ", bs[i]);
    	}
    }
    private static byte[] buildBMPImage() {
        List<byte[]> list = new ArrayList<>();

        // BITMAPFILEHEADER 14字节
        //  指定文件类型，必须是 0x424D，即字符串“BM”，也就是说所有.bmp 文件的头两个字节都是“BM”
        short bfType = 0x424D;
        // 指定文件大小，包括这 14 个字节。
        int bfSize = -1; // TODO
        // 为保留字，不用考虑
        short bfReserved1 = 0, bfReserved2 = 0;
        // 为从文件头到实际的位图数据的偏移字节数，即图 1.3 中前三个部分的长度之和
        int bfOffBits = 14 + 40;

        // BITMAPINFOHEADER 40字节
        // 指定这个结构的长度，为 40
        int biSize = 40;
        // 指定图象的宽度，单位是象素
        int biWidth = 300;
        // 指定图象的高度，单位是象素
        int biHeight = 300;
        // 必须是 1，不用考虑
        short biPlanes = 1;
        // 指定表示颜色时要用到的位数，常用的值为1(黑白二色图), 4(16 色图), 8(256 色),
        // 24(真彩色图)(新的.bmp 格式支持32 位色，这里就不做讨论了)。
        short biBitCount = 24;
        // 指定位图是否压缩，有效的值为BI_RGB，BI_RLE8，BI_RLE4，BI_BITFIELDS(都
        // 是一些Windows 定义好的常量)。要说明的是，Windows 位图可以采用RLE4，
        // 和RLE8 的压缩格式，但用的不多。我们今后所讨论的只有第一种不压缩的情况，
        // 即biCompression 为BI_RGB 的情况。
        int biCompression = 0; // BI_RGB
        // 指定实际的位图数据占用的字节数，其实也可以从以下的公式中计算出来：
        // biSizeImage=biWidth’ × biHeight
        // 要注意的是：上述公式中的biWidth’必须是4 的整倍数(所以不是biWidth，而是
        // biWidth’，表示大于或等于biWidth 的，最接近4 的整倍数。举个例子，如果
        // biWidth=240，则biWidth’=240；如果biWidth=241，biWidth’=244)。
        // 如果biCompression 为BI_RGB，则该项可能为零
        int biSizeImage = biWidth * biHeight * 3;
        // 指定目标设备的水平分辨率，单位是每米的象素个数
        int biXPelsPerMeter = 0;
        // 指定目标设备的垂直分辨率，单位同上
        int biYPelsPerMeter = 0;
        // 指定本图象实际用到的颜色数，如果该值为零，则用到的颜色数为2biBitCount
        int biClrUsed = 0;
        // 指定本图象中重要的颜色数，如果该值为零，则认为所有的颜色都是重要的。
        int biClrImportant = 0;

        bfSize = bfOffBits + biWidth * biHeight * 3;

        //  调色板Palette，24位真彩色图不需要


        // 实际的位图数据ImageDate
        // 要注意两点：
//        (1) 每一行的字节数必须是4 的整倍数，如果不是，则需要补齐。这在前面介
//        绍biSizeImage 时已经提到了。
//        (2) 一般来说，.bMP 文件的数据从下到上，从左到右的。也就是说，从文件中
//        最先读到的是图象最下面一行的左边第一个象素，然后是左边第二个象素……接
//        下来是倒数第二行左边第一个象素，左边第二个象素……依次类推 ，最后得到
//        的是最上面一行的最右一个象素。
        byte[] imageDate = new byte[biWidth * biHeight * 3];
        int tempIndex = 0;
        // 从下到上，红-绿-蓝
        int color;
        for(int i = 0; i < biHeight; i++) {
            if(i < 100) {
                // 红
                color = 0xff0000;
            } else if(i < 200) {
                // 绿
                color = 0x00ff00;
            } else {
                // 蓝
                color = 0x0000ff;
            }
            byte[] colorByte = int2Bytes(color, 3);
            for (int j = 0; j < biWidth; j++) {
                System.arraycopy(colorByte, 0, imageDate, tempIndex, 3);
                tempIndex += 3;
            }
        }

        // 这里特殊
        list.add(short2BytesOld(bfType));
        
        list.add(int2Bytes(bfSize));
        list.add(short2Bytes(bfReserved1));
        list.add(short2Bytes(bfReserved2));
        list.add(int2Bytes(bfOffBits));

        list.add(int2Bytes(biSize));
        list.add(int2Bytes(biWidth));
        list.add(int2Bytes(biHeight));
        list.add(short2Bytes(biPlanes));
        list.add(short2Bytes(biBitCount));
        list.add(int2Bytes(biCompression));
        list.add(int2Bytes(biSizeImage));
        list.add(int2Bytes(biXPelsPerMeter));
        list.add(int2Bytes(biYPelsPerMeter));
        list.add(int2Bytes(biClrUsed));
        list.add(int2Bytes(biClrImportant));

        list.add(imageDate);

        byte[] bmp = new byte[bfSize];
        int index = 0;
        for(int i = 0; i < list.size(); i++) {
            byte[] bs = list.get(i);
            System.arraycopy(bs, 0, bmp, index, bs.length);
            index += bs.length;
        }

        return bmp;
    }

    /**
     * 把一个int转成字节数组
     * @param value 整型值
     * @return 长度为4的字节数组
     */
    public static byte[] int2Bytes(int value) {
        int len = 4;
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

    /**
     * 把一个short转成字节数组
     * @param value short值
     * @return 长度为2的字节数组
     */
    public static byte[] short2Bytes(short value) {
        int len = 2;
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

    /**
     * 把一个整型转成字节数组
     * @param value 整型值
     * @return 长度为len的字节数组
     */
    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }
    
    /**
     * 把一个int转成字节数组
     * @param value 整型值
     * @return 长度为4的字节数组
     */
    public static byte[] int2BytesOld(int value) {
        int len = 4;
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

    /**
     * 把一个short转成字节数组
     * @param value short值
     * @return 长度为2的字节数组
     */
    public static byte[] short2BytesOld(short value) {
        int len = 2;
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

    /**
     * 把一个整型转成字节数组
     * @param value 整型值
     * @return 长度为len的字节数组
     */
    public static byte[] int2BytesOld(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

}
