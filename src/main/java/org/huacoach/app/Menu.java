package org.huacoach.app;

import org.huacoach.database.Data;
import org.huacoach.interfaces.Sex;
import org.huacoach.model.XMLmodels.ActivityModel;
import org.huacoach.model.profile.UserProfile;
import org.huacoach.parser.TcxFileParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private static final Data database = new Data();
    private static final TcxFileParser TCX_FILE_PARSER = new TcxFileParser();
    private static final Check checkInput = new Check();
    private static List<ActivityModel> activityModelList = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public void menuAssistantCoach(String[] args){
        System.out.println("---Assistant Coach App---");
        System.out.println("Create account: ");
        UserProfile user = createAccount(args);
        database.addToDatabase(user.getUserName(), user);
    }

    private UserProfile createAccount(String[] args){
        System.out.print("\nUsername: ");
        String username = scanner.nextLine().trim();
        Sex sex = checkInput.checkInputForSex(scanner);
        int age = checkInput.checkInputForAge(scanner);
        double weight = checkInput.checkInputForWeight(scanner);
        readFile(args);
        return new UserProfile(activityModelList, username, sex, age, weight);
    }

    private static void readFile(String[] args){
        for(String j : args){
            activityModelList.add(TCX_FILE_PARSER.readXMLFile(j));
        }
    }

    private static void addToDataBase(String username, UserProfile user){
        database.addToDatabase(username, user);
    }
}
