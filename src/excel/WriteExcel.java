package excel;

import com.fasterxml.jackson.databind.JsonNode;
import jxl.CellView;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;
import server.ApiCalls;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WriteExcel {

    public static void main(String[] args){


    }

    public void testWriteExcel(){
        String filename = "teste.xls";

        if(new File(filename).isFile()) {
            System.out.println("File already exists.");
        } else {
            try {
                WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));
                WritableSheet sheet = workbook.createSheet("Sheet1", 0);

                //Adding a label
                Label label = new Label(0, 0, "A label");
                sheet.addCell(label);

                Number number = new Number(0, 1, 3.1459);
                sheet.addCell(number);

                workbook.write();
                workbook.close();

                System.out.println("Excel file created successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void printYear(String dateString) {
        String filename = "teste.xls";

        if (new File(filename).isFile()) {
            System.out.println("File already exists.");

        } else {
            try {
                WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));

                SimpleDateFormat sdfUrl = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdfPrint = new SimpleDateFormat("dd MMMM yyyy");
                SimpleDateFormat sdfSheet = new SimpleDateFormat("ddMMMM");

                Date date = sdfUrl.parse(dateString);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date);
                calendar2.add(Calendar.MONTH, 1);

                ApiCalls api = new ApiCalls();

                for (int month=0; month<9; month++) {
                    int numberOfCells = 0;
                    String url = api.createSearchUrl(dateString, sdfUrl.format(calendar2.getTime()), "false", 10);
                    JsonNode rootNode = api.rootNodeTree(url);

                    //Sheet's gonna have the name like month1-month2
                    String sheetName = sdfSheet.format(calendar.getTime()) + "-" + sdfSheet.format(calendar2.getTime());
                    WritableSheet sheet = workbook.createSheet(sheetName, month);

                    //Add the titles
                    for(int column=0; column<4; column++)
                    {
                        WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 20);
                        cellFont.setBoldStyle(WritableFont.BOLD);
                        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);

                        Label label = new Label(column, 0, "", cellFormat);
                        switch (column){
                            case 0: label.setString("Destino"); break;
                            case 1: label.setString("Custo");   break;
                            case 2: label.setString("Duração"); break;
                            case 3: label.setString("Data");    break;
                            default: break;
                        }
                        sheet.addCell(label);
                        numberOfCells++;
                    }

                    //Print the matrix
                    for (int row=1; row<=10; row++)
                    {
                        WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 14);
                        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);

                        JsonNode tempNode = rootNode.get("data").get(row-1);

                        //For each field in one row
                        for(int column=0; column<4; column++)
                        {
                            String str = "";
                            Label label = new Label(column, row, str, cellFormat);
                            switch (column){
                                case 0: str = tempNode.get("cityTo").toString() + ", " + tempNode.get("countryTo").get("name").toString();
                                        break;
                                case 1: str = tempNode.get("conversion").get("EUR").toString() + "€";
                                        break;
                                case 2: str = tempNode.get("fly_duration").toString();
                                        break;
                                case 3: long dUnix = Long.parseLong(tempNode.get("dTimeUTC").toString());
                                        Date d8 = new Date(dUnix * 1000);
                                        str = sdfPrint.format(d8);
                                        break;
                                default: break;
                            }
                            str = str.replaceAll("\"", "");
                            label.setString(str);
                            sheet.addCell(label);
                            numberOfCells++;
                        }
                    }


                    for(int x=0; x<numberOfCells; x++)
                    {
                        CellView cell = new CellView();
                        cell = sheet.getColumnView(x);
                        cell.setAutosize(true);
                        sheet.setColumnView(x, cell);
                    }


                    //Increment a month to be printed
                    calendar.add(Calendar.MONTH, 1);
                    calendar2.add(Calendar.MONTH, 1);
                }

                workbook.write();
                workbook.close();
                System.out.println("Excel file created successfully.");
            } catch (Exception e) { e.printStackTrace(); }



        }
    }
}
