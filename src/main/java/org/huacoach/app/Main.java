package org.huacoach.app;

import org.huacoach.model.XMLActivity;
import org.huacoach.parser.TcxFileParser;
import org.huacoach.services.ActivityPrinter;
import org.huacoach.validation.ArgsValidator;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final TcxFileParser PARSER = new TcxFileParser();
    private static final ActivityPrinter PRINTER = new ActivityPrinter();
    private static final List<XMLActivity> activities = new ArrayList<>();

    public static void main(String[] args) {
        // 1. Validation
        ArgsValidator.ArgsResult argsResult;
        try {
            argsResult = ArgsValidator.validate(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }

        // 2. Parsing
        // SOS: Εδώ πρέπει να καλούμε την getTcxFiles() του validator, ΟΧΙ το σκέτο args
        for (String filePath : argsResult.getTcxFiles()) {
            try {
                XMLActivity activity = PARSER.readXMLFile(filePath);
                if (activity != null) {
                    activities.add(activity);
                }
            } catch (Exception e) {
                System.err.println("Failed to read: " + filePath);
            }
        }

        // 3. Printing
        if (activities.isEmpty()) {
            System.out.println("No valid activities found.");
            return;
        }

        for (XMLActivity activity : activities) {
            PRINTER.print(activity, argsResult.getWeight());
        }
    }
}