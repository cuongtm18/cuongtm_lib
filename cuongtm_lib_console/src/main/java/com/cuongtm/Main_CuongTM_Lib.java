package com.cuongtm;


import com.cuongtm.table.BaseController;
import com.cuongtm.test.Customer;

public class Main_CuongTM_Lib extends BaseController<Customer> {

    private Main_CuongTM_Lib() {

    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        new Main_CuongTM_Lib().menu();
    }

    public void menu() throws InstantiationException, IllegalAccessException {
        printMenuTable("header");
    }

}
