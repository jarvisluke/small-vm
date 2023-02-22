/*
Author: Luke Jarvis
Date: 2/14/2023
Class: CSCI 4200
Instructor: Abi Salimi
 */
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class SmallVm {

    private final static int MAX_MEMORY_SIZE = 500;
    private String[] memory = new String[MAX_MEMORY_SIZE];
    private HashMap<String, String> heap = new HashMap<>();

    public SmallVm(File file) {
        loadToMemory(file);
    }

    // Loads the program into memory
    private void loadToMemory(File file) {
        try {
            Scanner scan = new Scanner(file);
            int i = 0;
            try {
                while (scan.hasNextLine()) {
                    String nextLine = scan.nextLine();
                    // Removes any comments from nextLine
                    int endPoint = nextLine.length();
                    for (int j = 0; j < endPoint; j++) {
                        if (nextLine.charAt(j) == ';') {
                            endPoint = j;
                        }
                    }
                    nextLine = nextLine.substring(0, endPoint);
                    // Adds nextLine to memory if nextLine is not empty (i.e., if it does not begin with a comment symbol)
                    if (nextLine.length() > 0) {
                        memory[i] = nextLine;
                        i++;
                    }
                }
            }
            // Exception if memory capacity is exceeded
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: program exceeds memory capacity; " + e);
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Stores the result of adding num1 and num2 as name
    private void add(String name, int num1, int num2) {
        int result = num1 + num2;
        heap.put(name, String.valueOf(result));
    }

    // Stores the result of subtracting from num1 num2 as name
    private void sub(String name, int num1, int num2) {
        int result = num1 - num2;
        heap.put(name, String.valueOf(result));
    }

    // Stores the result of multiplying num1 by num2 as name
    private void mul(String name, int num1, int num2) {
        int result = num1 * num2;
        heap.put(name, String.valueOf(result));
    }

    // Stores the result of diving num1 by num2 as name
    private void div(String name, int num1, int num2) {
        int result = num1 / num2;
        heap.put(name, String.valueOf(result));
    }

    // Stores a variable from num
    private void sto(String name, String num) {
        heap.put(name, num);
    }

    // Stores a variable from the terminal
    private void in(String name) {
        Scanner scan = new Scanner(System.in);
        heap.put(name, scan.next());
    }

    // Outputs output to the terminal
    private void out(String output) {
        System.out.println(output);
    }

    // Evaluations variables to ints
    int evaluateArg(String arg) {
        try {
            return Integer.parseInt(arg);
        }
        // Exception occurs when arg cannot be parsed to an int, which is when arg is a variable
        catch (NumberFormatException e) {
            return Integer.parseInt(heap.get(arg));
        }
    }

    // Evaluates variables to strings
    String evaluateRegular(String arg) {
        arg = arg.trim();
        if (heap.containsKey(arg)) return heap.get(arg);
        else return arg.substring(1, arg.length()-1);
    }

    // Decodes instruction and calls operation
    void execute(String instruction) {
        Scanner scan = new Scanner(instruction); // Nathan Vrieland suggested using the scanner to pass arguments
        switch (scan.next()) {
            case "ADD" -> add(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "SUB" -> sub(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "MUL" -> mul(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "DIV" -> div(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "STO" -> sto(scan.next(), scan.next());
            case "IN" -> in(scan.next());
            case "OUT" -> out(evaluateRegular(scan.nextLine()));
            case "HALT" -> System.exit(0);
            default -> System.exit(-1); // Operation not recognized
        }
        scan.close();
    }

    public void run() {
        int programCounter = 0;
        while (true) {
            String instruction = memory[programCounter];
            programCounter++;
            execute(instruction);
        }
    }

    // Prints the required display
    static void printGreeting(File file) {
        System.out.println("Luke Jarvis\nCSCI 4200, Spring 2023");
        System.out.println("**********************************************");
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()) {
                System.out.println(scan.nextLine());
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace(); }
        System.out.println("**********************************************");
    }

    public static void main(String[] args) {
        File file = new File("mySmallVm_Prog.txt");
        printGreeting(file);
        SmallVm vm = new SmallVm(file);
        vm.run();
    }
}
