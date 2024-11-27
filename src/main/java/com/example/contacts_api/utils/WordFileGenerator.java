package com.example.contacts_api.utils;

import com.vaadin.flow.component.notification.Notification;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.util.List;

public class WordFileGenerator {

    private WordFileGenerator() {
    }

    public static void saveToWordFile(List<String> content, String fileName) {
        try (XWPFDocument document = new XWPFDocument()) {
            // Добавление строк в документ
            for (String line : content) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(line);
                run.addBreak();
            }

            // Путь к папке "Загрузки"
            String downloadsDir = System.getProperty("user.home") + "/Downloads";
            String fullFileName = downloadsDir + "/" + fileName;

            // Сохранение файла
            try (FileOutputStream out = new FileOutputStream(fullFileName)) {
                document.write(out);
            }

            Notification.show("Word file saved to: " + fullFileName);
        } catch (Exception e) {
            Notification.show("Error creating Word file: " + e.getMessage());
        }
    }
}
