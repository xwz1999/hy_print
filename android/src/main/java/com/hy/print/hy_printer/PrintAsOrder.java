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
//            printBarCode(code);
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
//            printBarCode(code);
            PrinterHelper.Align(PrinterHelper.LEFT);
            PrinterHelper.SetBold("3");
            PrinterHelper.AutCenter(PrinterHelper.TEXT, "45", "170", 500, 4, code);
            closeBold();
            if (fbaCode.isEmpty()) {
                fbaCode = "非FBA订单";
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


    public void printAsCodeNew(String code, String barCode, String channel, String country, String num, String sum,
                               int offset, boolean hasPlan) {

        if (hasPlan) {
            PrintPlanNew(code, barCode, channel, country, num, sum, offset);
        } else {
            PrintNoPlanNew(code, barCode, num, sum, offset);
        }
    }

    private void PrintPlanNew(String code, String barCode, String channel, String country, String num, String sum,
                              int offset) {
        System.out.println("print start");
        int cx = 0,cy = 0;
        int cx1 = 0,cy1 = 0;
        String country1 = "";
        String cMultiple = "3";
        String cSize = "55";
        String channel1 = "";
        if(country.length()>5){
            cMultiple = "3";
            cx = 65;cy = 220;
            country1 =  country.substring(4);
            cx1 = (165-country1.length()*25);
            cy1 = 280;

            country = country.substring(0,4);

        }else if(country.length()==5){
            cMultiple = "3";cx = 45;cy = 250;
            cSize = "55";
        }else if(country.length()==4){
            cMultiple = "4";cx = 40;cy = 245;
            cSize = "55";
        }
        else {
            cMultiple = "3";cy = 245;cx = 175-country.length()*40;
            cSize = "60";
        }
        int channelY = 460;
        int channel1Y = 460;
        if(channel.length()>11){
            channelY = 440;
            channel1Y = 490;
            channel1 = channel.substring(11);
            channel = channel.substring(0,11);
        }else{
            channelY = 460;
        }

        System.out.println("cx1");
        System.out.println(cx1);

        System.out.println("country1");
        System.out.println(country1);

        try {
            PrinterHelper.printAreaSize("0", "200", "200", "800", "1");
            //PrinterHelper.Encoding("gb2312");
            PrinterHelper.Box("0","10","575","790","3");
            PrinterHelper.Line("0", "160", "575", "160", "3");
            PrinterHelper.Line("0", "400", "575", "400", "3");
            PrinterHelper.Line("0", "560", "575", "560", "3");
            PrinterHelper.Line("380", "360", "540", "200", "3");
            PrinterHelper.Line("333", "160", "333", "400", "3");



            PrinterHelper.Align(PrinterHelper.CENTER);
            PrinterHelper.SetBold("4");
            PrinterHelper.SetMag("5","5");
            PrinterHelper.Text(PrinterHelper.TEXT, "55", "0", "0", "55", code);
            closeBold();
            PrinterHelper.SetMag("1","1");



            PrinterHelper.Align(PrinterHelper.LEFT);
            PrinterHelper.SetBold("3");
            PrinterHelper.SetMag(cMultiple,cMultiple);
            PrinterHelper.Text(PrinterHelper.TEXT,cSize , "0",String.valueOf(cx), String.valueOf(cy), country);
            if(!country1.isEmpty()){
                PrinterHelper.Text(PrinterHelper.TEXT,cSize , "0",String.valueOf(cx1), String.valueOf(cy1), country1);
            }
            closeBold();
            PrinterHelper.SetMag("1","1");



            PrinterHelper.Align(PrinterHelper.LEFT);
            PrinterHelper.SetBold("2");
            PrinterHelper.SetMag("3","3");
            PrinterHelper.Text(PrinterHelper.TEXT, "55", "0", "390", "210", num);
            PrinterHelper.Text(PrinterHelper.TEXT, "55", "0", String.valueOf(500-sum.length()*10), String.valueOf(315), sum);
            closeBold();
            PrinterHelper.SetMag("1","1");



            PrinterHelper.Align(PrinterHelper.CENTER);
            printBarCode(barCode,"0","590");


            PrinterHelper.Align(PrinterHelper.CENTER);
            PrinterHelper.SetMag("2","2");

            PrinterHelper.Text(PrinterHelper.TEXT,"55" , "0","0", "730", barCode);

            PrinterHelper.SetMag("1","1");



            PrinterHelper.Align(PrinterHelper.CENTER);
            PrinterHelper.SetMag("2","2");
            PrinterHelper.Text(PrinterHelper.TEXT,"3" , "0","0", String.valueOf(channelY), channel);

            if(!channel1.isEmpty()){
                PrinterHelper.Text(PrinterHelper.TEXT,"3" , "0","0", String.valueOf(channel1Y), channel1);
            }
            closeBold();
            PrinterHelper.SetMag("1","1");




            PrinterHelper.Form();
            PrinterHelper.Print();
        } catch (Exception e) {
            e.printStackTrace();

        }
        System.out.println("print finish");


    }


    private static void printBarCode(String code,String x,String y) {

        String width = "1.5";
        if (code.length() > 18) {
            width = "1";
        }
        try {
            PrinterHelper.Barcode(PrinterHelper.BARCODE, PrinterHelper.code128, width, "0", "130", x, y, false, "15", "15", "15", code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrintNoPlanNew(String code, String barCode, String num, String sum,
                                int offset) {

        System.out.println("print start");

        try {
            PrinterHelper.printAreaSize("0", "200", "200", "800", "1");
            PrinterHelper.Box("0","10","575","790","3");


            PrinterHelper.Line("0", "160", "575", "160", "3");
            PrinterHelper.Line("0", "400", "575", "400", "3");

            PrinterHelper.Line("380", "360", "540", "200", "3");
            PrinterHelper.Line("333", "160", "333", "400", "3");

            PrinterHelper.Align(PrinterHelper.CENTER);
            PrinterHelper.SetBold("4");
            PrinterHelper.SetMag("5","5");


            PrinterHelper.Text(PrinterHelper.TEXT, "55", "0", "0", "55", code);

            closeBold();
            PrinterHelper.SetMag("1","1");






            PrinterHelper.Align(PrinterHelper.LEFT);
            PrinterHelper.SetBold("3");

            PrinterHelper.SetMag("4","4");
            PrinterHelper.Text(PrinterHelper.TEXT,"55" , "0","40","245", "未建计划");

            closeBold();
            PrinterHelper.SetMag("1","1");



            PrinterHelper.Align(PrinterHelper.LEFT);
            PrinterHelper.SetBold("2");
            PrinterHelper.SetMag("3","3");
            PrinterHelper.Text(PrinterHelper.TEXT, "55", "0", "390", "210", num);

            PrinterHelper.Text(PrinterHelper.TEXT, "55", "0", String.valueOf(500-sum.length()*10), String.valueOf(315), sum);
            closeBold();
            PrinterHelper.SetMag("1","1");




            PrinterHelper.Align(PrinterHelper.CENTER);
            printBarCode(barCode,"0","520");

            PrinterHelper.Align(PrinterHelper.CENTER);
            PrinterHelper.SetMag("2","2");

            PrinterHelper.Text(PrinterHelper.TEXT,"55" , "0","0", "660", barCode);

            PrinterHelper.SetMag("1","1");



            PrinterHelper.Form();
            PrinterHelper.Print();
        } catch (Exception e) {
            e.printStackTrace();

        }
        System.out.println("print finish");
    }

    public int getStatus() throws Exception {
        return PrinterHelper.getstatus();
    }
}
