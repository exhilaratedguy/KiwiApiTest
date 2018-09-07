package excel;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;

public class WriteExcel {

    public static void main(String[] args) throws IOException, WriteException{
            String filename = "D:/teste.xls";

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
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
    }

}
