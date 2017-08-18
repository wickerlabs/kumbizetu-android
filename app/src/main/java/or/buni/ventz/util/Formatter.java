package or.buni.ventz.util;

import java.text.DecimalFormat;

/**
 * Created by yusuphwickama on 8/17/17.
 */

public class Formatter {

    private static final char[] SUFFIXES = {'K', 'M', 'G', 'T', 'P', 'E'};

    public static String putCommas(float amount) {

        return new DecimalFormat("#,###").format(amount);
    }


    public static String shortenMoney(float number) {
        //9.00

        if (number < 1000) {
            // No need to format this
            return String.valueOf(number);
        }
        // Convert to a string
        String string = String.valueOf(number);
        String amount = string.substring(0, string.lastIndexOf("."));

        string = amount;

        // The suffix we're using, 1-based
        final int magnitude = (string.length() - 1) / 3;
        // The number of digits we must show before the prefix
        final int digits = (string.length() - 1) % 3 + 1;

        // Build the string
        char[] value = new char[4];
        for (int i = 0; i < digits; i++) {
            value[i] = string.charAt(i);
        }
        int valueLength = digits;
        // Can and should we add a decimal point and an additional number?
        if (digits == 1 && string.charAt(1) != '0') {
            value[valueLength++] = '.';
            value[valueLength++] = string.charAt(1);
        }
        value[valueLength++] = SUFFIXES[magnitude - 1];
        return new String(value, 0, valueLength);
    }
}
