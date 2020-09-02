package com.company.dto;

import java.util.Scanner;

public class InputOutputFormula {
    private static Scanner scanner;
    static {
        scanner = new Scanner(System.in);
    }

    public static String inputFormula() {
        System.out.println("Введите формулу (завершение работы - q)");

        String inputFormula = scanner.nextLine();
        while ("".equals(inputFormula)){
            inputFormula = scanner.nextLine();
        }

        return inputFormula;
    }

    public static void terminateInput(){
        scanner.close();
    }
}
