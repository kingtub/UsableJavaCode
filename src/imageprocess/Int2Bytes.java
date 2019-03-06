package imageprocess;

/**
 * 整型与字节数组相互转化的工具类
 */
public class Int2Bytes {

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
     * 把字节数组转成整型
     * @param b 字节数组
     * @param start 开始的位置
     * @return 整型值
     */
    public static int bytes2Int(byte[] b, int start) {
        int len = 4;
        int sum = 0;
        int end = start + len;
//        System.out.print("bytes2Int ");
        for (int i = start; i < end; i++) {
//        	System.out.printf("b[%d] = %x ", i, b[i]);
        	int a = b[i] & 0xff;
            sum += a << 8 * (i - start);
        }
//        System.out.println();
        return sum;
    }

    /**
     *   把字节数组转成整型
     * @param b 字节数组
     * @param start 开始的位置
     * @return 整型值
     */
    public static short bytes2Short(byte[] b, int start) {
        int len = 2;
        short sum = 0;
        int end = start + len;
//        System.out.print("bytes2Short ");
        for (int i = start; i < end; i++) {
//        	System.out.printf("b[%d] = %x ", i, b[i]);
        	short a = (short)(b[i] & 0xff);
            sum += a << 8 * (i - start);
        }
//        System.out.println();
        return sum;
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

    /**
     * 把字节数组转成整型
     * @param b 字节数组
     * @param start 开始的位置
     * @return 整型值
     */
    public static int bytes2IntOld(byte[] b, int start) {
        int len = 4;
        int sum = 0;
        int end = start + len;
        for (int i = start; i < end; i++) {
            int n = ((int) b[i]) & 0xff;
            n <<= (--len) * 8;
            sum += n;
        }
        return sum;
    }
}
