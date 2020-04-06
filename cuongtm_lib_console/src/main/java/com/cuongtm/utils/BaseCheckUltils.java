package com.cuongtm.utils;

import java.util.Collection;

public class BaseCheckUltils {

    private BaseCheckUltils() {
    }

    /*
     * 0 - Thỏa mãn các điều kiện.
     * 1 - Báo lỗi do không nhập giá trị.
     * 2 - Báo lỗi do sai kiểu dữ liệu [0-9].
     * 3 - Báo lỗi do sai khoảng giá trị cho phép [1-3].
     */
    public static final byte SATISFY = 0;
    public static final byte NULL_OPTION = 1;
    public static final byte NOT_NUMERIC = 2;
    public static final byte NOT_IN_RANGE = 3;
    public static final String REGEX_NUMBER = "\\d+";

    public static boolean isEmpty(String sValue) {
        return sValue.isEmpty();
    }

    public static boolean isNumeric(String sValue) {
        return sValue.matches(REGEX_NUMBER);
    }

    public static boolean isSatisfy(byte bValue, int rollNoMenu) {
        return bValue < 1 || bValue > rollNoMenu;
    }

    public static byte checkInputOption(String sValue, int rollNoMenu) {
        if (!isEmpty(sValue)) {
            if (isNumeric(sValue)) {
                byte bValue = Byte.parseByte(sValue);
                if (!isSatisfy(bValue, rollNoMenu)) {
                    return SATISFY;
                } else {
                    return NOT_IN_RANGE;
                }
            } else {
                return NOT_NUMERIC;
            }
        } else {
            return NULL_OPTION;
        }
    }

    public static boolean checkCollectionNotNull(Collection collectionInput) {
        return collectionInput != null && !collectionInput.isEmpty();
    }
}
