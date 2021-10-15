package com.hy.print.hy_printer;

import cpcl.PrinterHelper;

public class PrintAsOrder {
    private static void setBold() {
        try {
            PrinterHelper.SetBold("2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void closeBold() {
        try {
            PrinterHelper.SetBold("0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int print(String code, String fbaCode, String country, String channel, String count, Boolean hasPlan) {
        int re = 0;
        if (!hasPlan) {
            re = printNoPlan(code, count);
        } else {
            re = printHasPlan(code, fbaCode, country, channel, count);
        }
        return re;
    }


    public static int printNoPlan(String code, String count) {
        int reusult = 0;
        System.out.println("print start");
        try {
            PrinterHelper.printAreaSize("0", "200", "200", "400", "1");
            PrinterHelper.Align(PrinterHelper.CENTER);
            PrinterHelper.Barcode(PrinterHelper.BARCODE, PrinterHelper.code128, "2", "1", "130", "0", "26", false, "", "", "", code);
            PrinterHelper.Align(PrinterHelper.LEFT);
            PrinterHelper.SetBold("3");
            PrinterHelper.AutCenter(PrinterHelper.TEXT, "45", "170", 500, 4, code);
            closeBold();

            PrinterHelper.Line("42", "228", "550", "228", "2");
            setBold();
            PrinterHelper.SetMag("2", "2");
            PrinterHelper.Text(PrinterHelper.TEXT, "20", "0", "45", "254", "未建计划");

            PrinterHelper.Align(PrinterHelper.RIGHT);
            PrinterHelper.Text(PrinterHelper.TEXT, "20", "0", "42", "254", "件数");

            PrinterHelper.Text(PrinterHelper.TEXT, "20", "0", "42", "300", count);
            PrinterHelper.SetMag("1", "1");
            closeBold();
            PrinterHelper.Align(PrinterHelper.LEFT);
            PrinterHelper.Form();
            PrinterHelper.Print();
        } catch (Exception e) {
            e.printStackTrace();
            reusult = -1;
        }
        System.out.println("print finish");
        return reusult;
    }

    public static int printHasPlan(String code, String fbaCode, String country, String channel, String count) {
        int reusult = 0;
        System.out.println("print start");
        try {
            PrinterHelper.printAreaSize("0", "200", "200", "400", "1");
            PrinterHelper.Align(PrinterHelper.CENTER);
            PrinterHelper.Barcode(PrinterHelper.BARCODE, PrinterHelper.code128, "2", "1", "130", "0", "26", false, "", "", "", code);
            PrinterHelper.Align(PrinterHelper.LEFT);
            PrinterHelper.SetBold("3");
            PrinterHelper.AutCenter(PrinterHelper.TEXT, "45", "170", 500, 4, code);
            closeBold();
            if (fbaCode.isEmpty()) {
                fbaCode = "非BFA订单";
            } else {
                fbaCode = "FBA号：" + fbaCode;
            }
            PrinterHelper.AutCenter(PrinterHelper.TEXT, "45", "222", 500, 8, fbaCode);
            PrinterHelper.Line("42", "269", "550", "269", "2");
            setBold();
            PrinterHelper.Text(PrinterHelper.TEXT, "8", "0", "45", "290", "渠道名称：");
            closeBold();
            PrinterHelper.Text(PrinterHelper.TEXT, "8", "0", "180", "290", channel);
            country = "目的国：" + country;
            PrinterHelper.Text(PrinterHelper.TEXT, "8", "0", "45", "337", country);
            PrinterHelper.Align(PrinterHelper.RIGHT);
            setBold();
            count = "件数：" + count;
            PrinterHelper.Text(PrinterHelper.TEXT, "8", "0", "40", "337", count);
            closeBold();
            PrinterHelper.Align(PrinterHelper.LEFT);
            PrinterHelper.Form();
            PrinterHelper.Print();
        } catch (Exception e) {
            e.printStackTrace();
            reusult = -1;
        }
        System.out.println("print finish");
        return reusult;
    }
}
