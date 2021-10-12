package com.hy.print.hy_printer;

import android.content.Context;

import cpcl.PrinterHelper;

public class PrintAsOrder {
    public static int print(String code, String fbaCode, String country, String channel, String count) {
        int reusult = 0;
        System.out.println("print start");
        try {
            PrinterHelper.printAreaSize("0", "200", "200", "400", "1");
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 10;
        }
//        try {
//            PrinterHelper.Align(PrinterHelper.CENTER);
//        } catch (Exception e) {
//            e.printStackTrace();
//            reusult = 1;
//        }
        try {
            PrinterHelper.Barcode(PrinterHelper.BARCODE, PrinterHelper.code128, "2", "1", "130", "45", "26", false, "", "", "", code);
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 2;
        }
        try {
            PrinterHelper.SetBold("3");
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 4;
        }
        try {
            PrinterHelper.AutCenter(PrinterHelper.TEXT, "45", "180", 500, 8, code);
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 5;
        }
        try {
            PrinterHelper.SetBold("0");
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 4;
        }

        if (fbaCode.isEmpty()) {
            fbaCode = "非FBA订单";
        } else {
            fbaCode = "FBA号：" + fbaCode;
        }
        try {
            PrinterHelper.AutCenter(PrinterHelper.TEXT, "45", "222", 500, 8, fbaCode);
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 5;
        }

//        try {
//            PrinterHelper.Prefeed("14");
//        } catch (Exception e) {
//            e.printStackTrace();
//            reusult =3;
//        }
        try {
            PrinterHelper.Line("42", "269", "550", "269", "2");
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 6;
        }
        channel = "渠道名称：" + channel;
        try {
            PrinterHelper.Text(PrinterHelper.TEXT, "8", "0", "45", "290", channel);
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 7;
        }
//        try {
//            PrinterHelper.Align(PrinterHelper.LEFT);
//        } catch (Exception e) {
//            e.printStackTrace();
//            reusult = 1;
//        }
        country = "目的国：" + country;
        try {
            PrinterHelper.Text(PrinterHelper.TEXT, "8", "0", "45", "337", country);
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 7;
        }
//        try {
//            PrinterHelper.Align(PrinterHelper.RIGHT);
//        } catch (Exception e) {
//            e.printStackTrace();
//            reusult = 1;
//        }
        count = "件数：" + count;
        try {
            PrinterHelper.Text(PrinterHelper.TEXT, "8", "0", "400", "337", count);
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 7;
        }
        try {
            PrinterHelper.Form();
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 8;
        }
        try {
            PrinterHelper.Print();
        } catch (Exception e) {
            e.printStackTrace();
            reusult = 9;
        }
        System.out.println("print finish");
        return reusult;
    }
}
