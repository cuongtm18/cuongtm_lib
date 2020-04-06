package com.cuongtm;


import com.cuongtm.table.BaseController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main_CuongTM_Lib extends BaseController<Customer> {
    private Main_CuongTM_Lib() {

    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        System.out.println("Hello 500 anh em...");
        List<String> listMenu = new ArrayList<>(Arrays.asList("Nhập"
                , "Hiển thị danh sách"
                , "Cập nhật thông tin"
                , "Xóa"
                , "Sắp xếp"
                , "Tìm kiếm"
                , "Thoát chương trình"));
        tableOfMenu("header", listMenu);

        new Main_CuongTM_Lib().printList();
    }

    public void printList() throws InstantiationException, IllegalAccessException {
        printDataTable(new ArrayList<>(Arrays.asList(new Customer(1, "cv"))), Customer.class);
    }
}
