package com.ray.resourcemanage.util;

import com.ray.resourcemanage.deviceManage.entity.Device;
import com.ray.resourcemanage.entity.SearchResult;
import com.sun.deploy.net.HttpResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExcelPoiUtil {
    public static void output(SearchResult<Device> searchResult) {
        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet("设备表");
        //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        HSSFRow row1 = sheet.createRow(0);
        //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        HSSFCell cell = row1.createCell(0);
        //设置单元格内容
        cell.setCellValue("设备一览表");
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        //在sheet里创建第二行
        HSSFRow row2 = sheet.createRow(1);
        //创建单元格并设置单元格内容
        row2.createCell(0).setCellValue("设备名称");
        row2.createCell(1).setCellValue("设备类型");
        row2.createCell(2).setCellValue("序列号");
        row2.createCell(3).setCellValue("拥有者");
        row2.createCell(4).setCellValue("设备用户名");
        row2.createCell(5).setCellValue("设备ip");
        row2.createCell(6).setCellValue("设备密码");
        row2.createCell(7).setCellValue("借用者");
        row2.createCell(8).setCellValue("预计归还时间");
        row2.createCell(9).setCellValue("是否归还");
        row2.createCell(10).setCellValue("标注");
        //在sheet里创建第三行
        List<Device> deviceList = searchResult.getRows();
        if (deviceList != null && deviceList.size() > 0) {
            for (int i = 0; i < deviceList.size(); i++) {
                Device device = deviceList.get(i);
                HSSFRow row = sheet.createRow(i + 2);
                row.createCell(0).setCellValue(device.getDeviceName());
                row.createCell(1).setCellValue(device.getDeviceType());
                row.createCell(2).setCellValue(device.getSerialNumber());
                row.createCell(3).setCellValue(device.getOwner());
                row.createCell(4).setCellValue(device.getDeviceUsername());
                row.createCell(5).setCellValue(device.getDeviceIp());
                row.createCell(6).setCellValue(device.getDevicePwd());
                row.createCell(7).setCellValue(device.getBorrower());
                if(device.getBackTime()!=null) {
                    row.createCell(8).setCellValue(device.getBackTime());
                } else {
                    row.createCell(8).setCellValue("");
                }

                row.createCell(9).setCellValue(device.getBack());
                row.createCell(10).setCellValue(device.getRemark());
            }
        }
        //输出Excel文件
        OutputStream out = null;
        try {
            out = new FileOutputStream("e://bookPoi.xls");
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
