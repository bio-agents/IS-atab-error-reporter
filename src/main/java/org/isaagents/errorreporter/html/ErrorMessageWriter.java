package org.isaagents.errorreporter.html;

import org.isaagents.errorreporter.model.ErrorLevel;
import org.isaagents.errorreporter.model.ErrorMessage;

import java.util.List;
import java.util.Scanner;

public class ErrorMessageWriter {

    private static final String BASE_HTML_FILE = "/html-resources/error_full_template.html";
    private static final String TABLE_HTML_FILE = "/html-resources/error_list_template.html";

    private String injectedTableHTML;
    private String baseHTML;


    public String createHTMLRepresentationOfErrors(List<ErrorMessage> errors) {

        // only load the files if they are not already in memory
        if (baseHTML == null || injectedTableHTML == null) {
            loadFiles();
        }

        StringBuilder tables = new StringBuilder();
        for (ErrorMessage error : errors) {
            String tmpTable = injectedTableHTML;

            tmpTable = tmpTable.replaceAll("(TYPE)", error.getErrorLevel().toString());

            ErrorLevel level = error.getErrorLevel();
            String tag = null;

            if (level == ErrorLevel.ERROR)
                tag = "error-tag";
            else if (level == ErrorLevel.WARNING)
                tag = "warning-tag";
            else
                tag = "info-tag";

            tmpTable = tmpTable.replaceAll("(ERROR_CLASS)", tag);
            tmpTable = tmpTable.replaceAll("FILE", " - " + error.getFile());
            tmpTable = tmpTable.replaceAll("MESSAGE", error.getMessage());

            tables.append(tmpTable);
        }

        return baseHTML.replace("<INJECTED_CODE/>", tables.toString());
    }

    private void loadFiles() {
        baseHTML = loadAndAssignFileContents(BASE_HTML_FILE);
        injectedTableHTML = loadAndAssignFileContents(TABLE_HTML_FILE);
    }

    private String loadAndAssignFileContents(String fileLocation) {
        Scanner fileScanner = new Scanner(getClass().getResourceAsStream(fileLocation));

        StringBuilder sb = new StringBuilder();
        while (fileScanner.hasNextLine()) {
            sb.append(fileScanner.nextLine());
        }

        return sb.toString();
    }
}
