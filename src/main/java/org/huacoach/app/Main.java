package org.huacoach.app;

import org.huacoach.model.XMLmodels.XMLActivity;
import org.huacoach.parser.TcxFileParser;

import java.util.ArrayList;
import java.util.List;


public class Main {
    private static final TcxFileParser TCX_FILE_PARSER = new TcxFileParser();
    private static List<XMLActivity> activityModelList = new ArrayList<>();

    public static void main(String[] args){
        for (String j : args) {
            activityModelList.add(TCX_FILE_PARSER.readXMLFile(j));
        }
    }
}
