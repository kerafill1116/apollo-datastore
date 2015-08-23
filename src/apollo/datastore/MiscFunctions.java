package apollo.datastore;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class MiscFunctions {

    private MiscFunctions() {}

    /**
     * Defines the date string format used by date conversion functions in this class
     */
    public static final String DATE_FORMAT_STRING = "yyyy/MM/dd HH:mm:ss Z";
    public static final String UTC_STRING = "UTC";
    public static final String UTF_CHARSET = "UTF-8";
    public static final String KEY_SPEC_ALGO = "AES";
    public static final String CIPHER_ALGO = "AES/CBC/PKCS5Padding";

    public enum HashAlgorithms {
        SHA_256("SHA-256"),
        MD5("MD5");

        private final String name;

        private HashAlgorithms(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
          return this.name;
        }
    }

    /**
     * Encrypts a String using the specified HashAlgorithm
     * @param str String to be encrypted, null is converted to empty string ""
     * @return hashAlgorithm hash algorithm to be used for the encryption
     */
    public static String getEncryptedHash(String str, HashAlgorithms hashAlgorithm) {
        MessageDigest md = null;
        str = (str != null) ? str : "";
        StringBuffer sb = new StringBuffer();
        try {
            md = MessageDigest.getInstance(hashAlgorithm.toString());
            md.update(str.getBytes());
            byte byteData[] = md.digest();
            for(int i = 0; i < byteData.length; i++)
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        catch(Exception e) { // NoSuchAlgorithmException
            return str;
        }
        return sb.toString();
    }

    public static String toUTCDateString(Date date) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT_STRING);
        df.setTimeZone(java.util.TimeZone.getTimeZone(UTC_STRING));
        return df.format(date);
    }

    /**
     * Converts a formatted date string to a {@link Date} object with UTC time zone
     * @param utcDateString date string format must be "yyyy/MM/dd HH:mm:ss" or defined at public final constant {@link DATE_FORMAT_STRING}
     * @return {@link Date} object or <code>null</code> if utcDateString cannot be converted
     */
    public static Date toUTCDate(String utcDateString) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT_STRING);
        df.setTimeZone(java.util.TimeZone.getTimeZone(UTC_STRING));
        Date date = null;
        try {
            date = df.parse(utcDateString);
        }
        catch(ParseException e) { }
        catch(NullPointerException e) { }
        return date;
    }

    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String encryptAES(String encryptKey, String encryptString)
            throws Exception {
        byte[] key = Arrays.copyOf(encryptKey.getBytes(UTF_CHARSET), 16);
        SecretKeySpec secretKey = new SecretKeySpec(key, KEY_SPEC_ALGO);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        byte[] iv = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        return Base64.encodeBase64String(cipher.doFinal(encryptString.getBytes()));
    }

    public static String decryptAES(String decryptKey, String decryptString)
            throws Exception {
        byte[] key = Arrays.copyOf(decryptKey.getBytes(UTF_CHARSET), 16);
        SecretKeySpec secretKey = new SecretKeySpec(key, KEY_SPEC_ALGO);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        byte[] iv = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        return new String(cipher.doFinal(Base64.decodeBase64(decryptString)));
    }
}
