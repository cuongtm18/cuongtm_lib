package com.cuongtm.table;

import com.cuongtm.utils.BaseCheckUltils;
import com.cuongtm.utils.Constant;
import com.cuongtm.utils.PrintWithColor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BaseController<T> {

    protected static StringBuilder datatableString = null;

    protected static void tableOfMenu(String header, List<String> menuName) {
        int menuRollNo = 1;
        datatableString = new StringBuilder();
        //Tiêu đề menu
        datatableString.append(Constant.MENU_TABLE_BOUND);
        datatableString.append("|" + StringUtils.center(header, Constant.SIZE_MENU_FUNCTION) + Constant.END_ROW_DATA);
        datatableString.append(Constant.MENU_FUNCTION_BOUND);

        //Duyệt và in ra tên chức năng
        for (String menu : menuName) {
            datatableString.append(String.format(Constant.ALIGN_FORMAT, StringUtils.center(String.valueOf(menuRollNo++), Constant.SIZE_MENU_ID), menu));
            datatableString.append(Constant.MENU_FUNCTION_BOUND);
        }

        //Chọn chức năng
        datatableString.append("======> Chọn chức năng ( 1 -> " + (menuName.size()) + " ): ");
        PrintWithColor.printDataTable(datatableString.toString());
    }

    protected void printDataTable(Collection<T> collectionInput, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        datatableString = new StringBuilder();
        T objectInstance = getClassNewInstanceEx(clazz);
        String bound = Constant.DATATABLE_ID_BOUND;

        //Lấy ra danh sách các Attributes và danh sách AttributeName
        List<Field> listField = new ArrayList<>(Arrays.asList(objectInstance.getClass().getDeclaredFields()));
        List<String> actualFieldNames = getFieldNames(listField);
        datatableString.append(Constant.NEW_LINE);

        //Tạo đường biên cho bảng
        for (int i = 1; i < listField.size(); i++) {
            bound += Constant.DATATABLE_FIELD_BOUND;
        }
        datatableString.append(bound + Constant.END_TABLE_LINE);

        //Tạo tiêu đề bảng
        datatableString.append("|" + StringUtils.center(String.format(Constant.DATATABLE_HEADER, StringUtils.upperCase(objectInstance.getClass().getSimpleName()))
                , bound.length() - 1) + Constant.END_ROW_DATA);
        datatableString.append(bound + Constant.END_TABLE_LINE);

        //Tạo dòng tiêu đề cho các Attributes
        int count = 0;
        for (String fieldName : actualFieldNames) {
            if (count++ == 0) {
                datatableString.append("|" + StringUtils.center(StringUtils.upperCase(fieldName), Constant.SIZE_DATATABLE_ID));
            } else {
                datatableString.append("|" + StringUtils.center(StringUtils.upperCase(fieldName), Constant.SIZE_DATATABLE_ROW));
            }
        }
        datatableString.append(Constant.END_ROW_DATA + bound + Constant.END_TABLE_LINE);

        int rowCount = 0;
        //Kiểm tra đầu vào không null và có phần tử
        if (BaseCheckUltils.checkCollectionNotNull(collectionInput)) {
            //In dữ liệu các Attributes của từng phần tử trong collectionInput ra bảng
            count = 0;
            for (T obj : collectionInput) {
                for (Field field : listField) {
                    field.setAccessible(true);
                    if (count++ == 0) {
                        datatableString.append("|" + StringUtils.center(Constant.SPACE + field.get(obj).toString(), Constant.SIZE_DATATABLE_ID));
                    } else {
                        datatableString.append("|" + StringUtils.rightPad(Constant.SPACE + field.get(obj).toString(), Constant.SIZE_DATATABLE_ROW));
                    }
                }
                //Phân biệt hết dữ liệu mỗi dòng
                count = 0;
                datatableString.append(Constant.END_ROW_DATA);
                datatableString.append(bound + Constant.END_TABLE_LINE);
                //Đếm số phần tử trong Danh sách
                rowCount++;
            }
        } else {
            //Nếu collectionInput rỗng thì in ra bảng trống
            datatableString.append("|" + StringUtils.rightPad(Constant.NULL_DATATABLE_INFO, bound.length() - 1) + Constant.END_ROW_DATA);
            datatableString.append(bound + Constant.END_TABLE_LINE);
        }

        //Tạo dòng tổng số bản ghi
        datatableString.append("|" + StringUtils.rightPad(String.format(Constant.ROW_COUNT, rowCount), bound.length() - 1) + Constant.END_ROW_DATA);
        datatableString.append(bound + Constant.END_TABLE_LINE);
        datatableString.append(Constant.NEW_LINE);
        //In ra bảng dữ liệu
        PrintWithColor.printDataTable(datatableString.toString());
    }

    private List<String> getFieldNames(List<Field> fields) {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }

    private T getClassNewInstanceEx(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

}
