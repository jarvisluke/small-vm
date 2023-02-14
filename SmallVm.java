import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class SmallVm {

    private final static int MAX_MEMORY_SIZE = 500;
    private String[] memory = new String[MAX_MEMORY_SIZE];
    private HashMap<String, String> vars = new HashMap<>(); // Stores variables
    int programCounter = 0; // Tracks where in the memory the instruction is

    // Loads the program instructions into memory
    public void loadToMemory(File file) {
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
            } catch (ArrayIndexOutOfBoundsException e) { // Exception if memory capacity is exceeded
                System.out.println("Error: program exceeds memory capacity; " + e);
            }
            scan.close();
        } catch (FileNotFoundException e) { // Exception if file is not located
            e.printStackTrace();
        }
    }

    // Stores the result of adding num1 and num2 as name
    void ADD(String name, int num1, int num2) {
        int result = num1 + num2;
        vars.put(name, String.valueOf(result));
    }

    // Stores the result of subtracting from num1 num2 as name
    void SUB(String name, int num1, int num2) {
        int result = num1 - num2;
        vars.put(name, String.valueOf(result));
    }

    // Stores the result of multiplying num1 by num2 as name
    void MUL(String name, int num1, int num2) {
        int result = num1 * num2;
        vars.put(name, String.valueOf(result));
    }

    // Stores the result of diving num1 by num2 as name
    void DIV(String name, int num1, int num2) {
        int result = num1 / num2;
        vars.put(name, String.valueOf(result));
    }

    // Stores a variable from num
    void STO(String name, String num) {
        vars.put(name, num);
    }

    // Stores a variable from the terminal
    void IN(String name) {
        Scanner scan = new Scanner(System.in);
        vars.put(name, scan.next());
    }

    // Outputs the output to the terminal
    void OUT(String output) {
        System.out.println(output);
    }

    // Returns the instruction at the program counter in memory
    String fetch() {
        return memory[programCounter];
    }

    // Evaluations variables where an int is expected as the argument
    int evaluateArg(String arg) {
        try {
            return Integer.parseInt(arg);
        }
        catch (NumberFormatException e) {
            return Integer.parseInt(vars.get(arg));
        }
    }

    // Evaluates variables where a string is expected as the argument
    String evaluateRegular(String arg) {
        arg = arg.trim();
        if (vars.containsKey(arg)) {
            return vars.get(arg);
        }
        else return arg.substring(1, arg.length()-1);
    }

    // Decodes instruction and calls operation
    void execute(String instruction) {
        Scanner scan = new Scanner(instruction);
        switch (scan.next()) {
            case "ADD" -> ADD(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "SUB" -> SUB(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "MUL" -> MUL(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "DIV" -> DIV(scan.next(), evaluateArg(scan.next()), evaluateArg(scan.next()));
            case "STO" -> STO(scan.next(), scan.next());
            case "IN" -> IN(scan.next());
            case "OUT" -> OUT(evaluateRegular(scan.nextLine()));
            case "HALT" -> System.exit(0);
            default -> System.exit(1);
        }
        scan.close();
    }

    public static void main(String[] args) {
        File file = new File("mySmallVm_Prog.txt");
        SmallVm myVm = new SmallVm();
        myVm.loadToMemory(file);
        while (true) {
            String instruction = myVm.fetch();
            myVm.programCounter++;
            myVm.execute(instruction);
        }
    }
}
