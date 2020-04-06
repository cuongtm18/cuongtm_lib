package com.cuongtm.utils;

import com.cuongtm.utils.Constant.Color;

public class PrintWithColor {
    public static void printErrorAlert(String alertMsg) {
        System.out.println(Color.RED + alertMsg + Color.RESET);
    }

    public static void printSuccessAlert(String alertMsg) {
        System.out.println(Color.GREEN + alertMsg + Color.RESET);
    }

    public static void printDataTable(String stringBuilderDtb) {
        System.out.println(Color.GREEN_BOLD + stringBuilderDtb + Color.RESET);
    }
}
