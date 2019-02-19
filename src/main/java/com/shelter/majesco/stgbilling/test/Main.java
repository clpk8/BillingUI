package com.shelter.majesco.stgbilling.test;
import com.shelter.majesco.stgbilling.test.automationScripts.SearchPolicy;
import com.shelter.majesco.stgbilling.test.input.modifier.InputModifier;
import com.shelter.majesco.stgbilling.test.output.modifier.ExpectedResultModifier;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        int choice = 0;
        System.out.println("Please make selection on what you want to do:");
        System.out.println("1. Modify input file.");
        System.out.println("2. Modify expected result file.");
        System.out.println("3. Run the automation.");

        Scanner reader = new Scanner(System.in);
        choice = reader.nextInt();

        switch (choice){
            case 1:
                InputModifier inputModifier = new InputModifier();
                try {
                    inputModifier.main(args);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                ExpectedResultModifier expectedResultModifier = new ExpectedResultModifier();
                ExpectedResultModifier.main(args);
                break;
            case 3:
                SearchPolicy searchPolicy = new SearchPolicy();
                try {
                    searchPolicy.main(args);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Please enter valid choice.");
        }
        reader.close();

    }
}
