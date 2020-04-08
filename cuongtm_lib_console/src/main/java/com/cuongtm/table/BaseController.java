
package com.cuongtm.table;

import com.cuongtm.utils.BaseCheckUltils;
import com.cuongtm.utils.Constant;
import com.cuongtm.utils.PrintWithColor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author CuongTM
 */

public class BaseController<T> {

    private T object;
    private StringBuilder datatableString = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private List<String> listMenu = new ArrayList<>(Arrays.asList(
            String.format(Constant.FUNC_INSERT, getGenericName()),
            String.format(Constant.FUNC_EDIT, getGenericName()),
            String.format(Constant.FUNC_DELETE, getGenericName()),
            String.format(Constant.FUNC_SEARCH, getGenericName()),
            String.format(Constant.FUNC_SHOW, getGenericName()),
            String.format(Constant.FUNC_EXIT, getGenericName())));


    /**
     * @apiNote Hàm lấy tên class con
     */
    private String getGenericName() {
        String name = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].toString();
        name = name.replace("class ", "");
        int indexOfLastDot = name.lastIndexOf('.');
        name = name.substring(indexOfLastDot + 1);
        return name;
    }

    /**
     * @param header : tiêu đề Menu
     * @apiNote Hàm in Menu
     */
    protected void printMenuTable(String header) {
        this.tableMenu(header, listMenu);
    }


    /**
     * @param header   : tiêu đề Menu
     * @param menuName : List tên Menu
     * @apiNote Hàm tạo bảng Menu
     */
    private void tableMenu(String header, List<String> menuName) {
        int menuRollNo = 1;
        datatableString = new StringBuilder();
        //Tiêu đề menu
        //datatableString.append(Constant.MENU_TABLE_BOUND);
        datatableString.append(StringUtils.center(header, Constant.SIZE_MENU_FUNCTION) + Constant.NEW_LINE);
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


    /**
     * @param collectionInput : Danh sách dữ liệu
     * @param clazz           : Class type của danh sách dữ liệu
     * @apiNote Hàm in bảng dữ liệu
     */
    protected void printDataTable(Collection<T> collectionInput, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        datatableString = new StringBuilder();
        object = getClassNewInstanceEx(clazz);
        String bound = Constant.DATATABLE_ID_BOUND;

        //Lấy ra danh sách các Attributes và danh sách AttributeName
        List<Field> listField = new ArrayList<>(Arrays.asList(object.getClass().getDeclaredFields()));
        List<String> actualFieldNames = getFieldNames(listField);
        datatableString.append(Constant.NEW_LINE);

        //Tạo đường biên cho bảng
        for (int i = 1; i < listField.size(); i++) {
            bound += Constant.DATATABLE_FIELD_BOUND;
        }
        datatableString.append(bound + Constant.END_TABLE_LINE);

        //Tạo tiêu đề bảng
        datatableString.append("|" + StringUtils.center(String.format(Constant.DATATABLE_HEADER, StringUtils.upperCase(object.getClass().getSimpleName()))
                , bound.length() - 1) + Constant.END_ROW_DATA);
        datatableString.append(bound + Constant.END_TABLE_LINE);

        //Tạo dòng tiêu đề cho các Attributes
        int count = Constant.FIRST_ELEMENT;
        for (String fieldName : actualFieldNames) {
            if (count++ == Constant.FIRST_ELEMENT) {
                appendStrCenter(StringUtils.upperCase(fieldName), Constant.SIZE_DATATABLE_ID);
            } else {
                appendStrCenter(StringUtils.upperCase(fieldName), Constant.SIZE_DATATABLE_ROW);
            }
        }
        datatableString.append(Constant.END_ROW_DATA + bound + Constant.END_TABLE_LINE);

        int rowCount = Constant.FIRST_ELEMENT;
        //Kiểm tra đầu vào không null và có phần tử
        if (BaseCheckUltils.checkCollectionNotNull(collectionInput)) {
            //In dữ liệu các Attributes của từng phần tử trong collectionInput ra bảng
            count = Constant.FIRST_ELEMENT;
            for (T obj : collectionInput) {
                for (Field field : listField) {
                    field.setAccessible(true);
                    if (count++ == Constant.FIRST_ELEMENT) {
                        appendStrCenter(field.get(obj), Constant.SIZE_DATATABLE_ID);
                    } else {
                        appendStrDatatable(field.get(obj), Constant.SIZE_DATATABLE_ROW);
                    }
                }
                //Phân biệt hết dữ liệu mỗi dòng
                count = Constant.FIRST_ELEMENT;
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

    /**
     * @param fields : Danh sách Attributes
     * @apiNote Hàm lấy danh sách tên Attributes
     */
    private List<String> getFieldNames(List<Field> fields) {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }

    /**
     * @param clazz : Danh sách Attributes
     * @apiNote Hàm lấy new Instance của class con kế thừa
     */
    private T getClassNewInstanceEx(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    /**
     * @param obj : Đối tượng cần check
     * @apiNote Hàm boolean check đối tượng null hay không
     */
    private boolean checkNullField(Object obj) {
        if (obj != null) {
            return true;
        }
        return false;
    }

    /**
     * @param obj : dữ liệu của attribute
     * @apiNote Hàm lấy giá trị String của đối tượng
     */
    private String objFieldData(Object obj) {
        if (checkNullField(obj)) {
            if (checkObjectTypeOfDate(obj)) {
                return dateFormat.format(obj);
            }
            return obj.toString();
        } else {
            return Constant.SPACE;
        }
    }

    /**
     * @param obj : dữ liệu của attribute
     * @apiNote Hàm boolean kiểm tra đối tượng có thuộc type java.util.Date
     */
    private boolean checkObjectTypeOfDate(Object obj) {
        if (obj instanceof Date) {
            return true;
        }
        return false;
    }

    private void appendStrCenter(Object obj, int size) {
        datatableString.append("|" + StringUtils.center(objFieldData(obj), size));
    }

    private void appendStrRight(Object obj, int size) {
        datatableString.append("|" + StringUtils.rightPad(Constant.SPACE + objFieldData(obj), size));
    }

    private void appendStrLeft(Object obj, int size) {
        datatableString.append("|" + StringUtils.leftPad(objFieldData(obj), size) + Constant.SPACE);
    }

    private void appendStrDatatable(Object obj, int size) {
        if (checkObjectTypeOfDate(obj)) {
            appendStrCenter(obj, size);
        } else {
            appendStrRight(obj, size);
        }
    }

    /**
     * @param menuName : tên chức năng muốn thêm
     * @apiNote Hàm thêm chức năng vào menu
     */
    protected void addMenuToListMenu(String... menuName) {
        for (String menu : menuName) {
            listMenu.add(4, menu);
        }
    }

}
