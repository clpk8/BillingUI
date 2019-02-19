package com.shelter.majesco.stgbilling.test.output.modifier;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelPicker {
    //pick an excel
    public static File fileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Please select the excel file you want to modify");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            System.out.println("You've chosen " + file.getName());
            return file;
        } else {
            System.out.println("There is an error openning the file");
            return null;
        }

    }

    //copy the file
    public void copyFile(File original, File newFile) throws Exception {
        FileInputStream fis = new FileInputStream(original);
        FileOutputStream fos = new FileOutputStream(newFile);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) fis.close();
            if (fos != null) fos.close();
        }
    }
}
