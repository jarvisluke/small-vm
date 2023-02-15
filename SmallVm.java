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
    private String[] memory = new String[MAX_MEMORY_SIZE]; // Stores instructions
    private HashMap<String, String> heap = new HashMap<>(); // Stores variables
    int programCounter = 0; // Tracks where in the memory the instruction is

    public SmallVm(File file) {
        loadToMemory(file);
    }

    // Loads the program instructions into memory
    void loadToMemory(File file) {
        try {
            Scanner scan = new Scanner(file);
            int loader = 0; // Keeps track of where in memory the instructions are being loaded
            try {
                while (scan.hasNextLine()) {
                    String nextLine = scan.nextLine();
                    if (nextLine.charAt(0) != ';') { // Checks if the line is a comment
                        memory[loader] = nextLine;
                        loader++;
                    }
                }
            }
            catch (ArrayIndexOutOfBoundsException e) { // Exception if memory capacity is exceeded
                System.out.println("Error: program exceeds memory capacity; " + e);
            }
            scan.close();
        }
        catch (FileNotFoundException e) { // Exception if file is not located
            e.printStackTrace();
        }
    }

    // Stores the result of adding num1 and num2 as name
    void ADD(String name, int num1, int num2) {
        int result = num1 + num2;
        heap.put(name, String.valueOf(result));
    }

    // Stores the result of subtracting from num1 num2 as name
    void SUB(String name, int num1, int num2) {
        int result = num1 - num2;
        heap.put(name, String.valueOf(result));
    }

    // Stores the result of multiplying num1 by num2 as name
    void MUL(String name, int num1, int num2) {
        int result = num1 * num2;
        heap.put(name, String.valueOf(result));
    }

    // Stores the result of diving num1 by num2 as name
    void DIV(String name, int num1, int num2) {
        int result = num1 / num2;
        heap.put(name, String.valueOf(result));
    }

    // Stores a variable from num
    void STO(String name, String num) {
        heap.put(name, num);
    }

    // Stores a variable from the terminal
    void IN(String name) {
        Scanner scan = new Scanner(System.in);
        heap.put(name, scan.next());
    }

    // Outputs output to the terminal
    void OUT(String output) {
        System.out.println(output);
    }

    // Returns the instruction at the program counter in memory
    String fetch() {
        return memory[programCounter];
    }

    // Evaluations variables to ints
    int evaluateArg(String arg) {
        try {
            return Integer.parseInt(arg);
        }
        catch (NumberFormatException e) {
            return Integer.parseInt(heap.get(arg));
        }
    }

    // Evaluates variables to strings
    String evaluateRegular(String arg) {
        arg = arg.trim();
        if (heap.containsKey(arg)) {
            return heap.get(arg);
        }
        else return arg.substring(1, arg.length()-1);
    }

    // Decodes instruction and calls operation
    void execute(String instruction) {
        Scanner scan = new Scanner(instruction); // Nathan Vrieland suggested using the scanner to pass arguments
        switch (scan.next()) {
            case "ADD" -> ADD(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "SUB" -> SUB(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "MUL" -> MUL(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "DIV" -> DIV(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "STO" -> STO(scan.next(), scan.next());
            case "IN" -> IN(scan.next());
            case "OUT" -> OUT(evaluateRegular(scan.nextLine()));
            case "HALT" -> System.exit(0);
            default -> System.exit(1); // Operation not recognized
        }
        scan.close();
    }

    // Prints the required display
    static void printGreeting(File file) {
        System.out.println("Luke Jarvis, CSCI 4200 (Spring 2023)");
        for (int i = 0; i < 42; i ++) {
            System.out.print("*");
        }
        System.out.println();
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()) {
                System.out.println(scan.nextLine());
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 42; i ++) {
            System.out.print("*");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        File file = new File("mySmallVm_Prog.txt");
        printGreeting(file);
        SmallVm myVm = new SmallVm(file);
        while (true) {
            String instruction = myVm.fetch();
            myVm.programCounter++;
            myVm.execute(instruction);
        }
    }
}
