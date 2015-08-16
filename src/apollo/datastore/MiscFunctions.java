package apollo.datastore;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MiscFunctions {

    private MiscFunctions() {}

    /**
     * Defines the date string format used by date conversion functions in this class
     */
    public static final String DATE_FORMAT_STRING = "yyyy/MM/dd HH:mm:ss Z";
    public static final String UTC_STRING = "UTC";

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
}
