"""
Luke Jarvis
2/26/2023
"""
import datetime


class vm:
    _memory: list[str] = []
    _heap: dict[str, int] = {}
    # filename with timestamp ending in minute
    _file = 'vm_output_'+datetime.datetime.now().strftime('%x:%X').replace(':', '').replace('/', '.')[:-2]+'.txt'

    # creates new vm, calls load_to_memory()
    def __init__(self, file: str):
        self._load_to_memory(file)
        open(self._file, 'x')

    # writes output to file and terminal
    def _print_out(self, output):
        output = str(output)
        f = open(self._file, 'a')
        f.write(output+'\n')
        print(output)

    # stores the sum of num1 and num2 as name
    def _add(self, name: str, num1: int, num2: int) -> None:
        num1 += num2
        self._heap.update({name: num1})

    # stores the difference of num1 and num2 as name
    def _sub(self, name: str, num1: int, num2: int) -> None:
        num1 -= num2
        self._heap.update({name: num1})

    # stores the product of num1 and num2 as name
    def _mul(self, name: str, num1: int, num2: int) -> None:
        num1 *= num2
        self._heap.update({name: num1})

    # stores the quotient of num1 and num2 as name
    def _div(self, name: str, num1: int, num2: int) -> None:
        num1 /= num2
        self._heap.update({name: num1})

    # stores num as name
    def _sto(self, name: str, num: int) -> None:
        self._heap.update({name: num})

    # stores input as name
    def _in(self, name: str) -> None:
        i = int(input())
        self._print_out(i)
        self._heap.update({name: i})

    # prints output
    def _out(self, output: str) -> None:
        self._print_out(output)

    # loads instructions in file to memory
    def _load_to_memory(self, file: str) -> None:
        try:
            f = open(file, 'r')
            # adds each instruction in file to memory
            for line in f.readlines():
                # checks if line is a comment
                if ';' not in line:
                    self._memory.append(line.replace('\n', ''))
                elif line[0] != ';':
                    self._memory.append(line[:line.find(';')])
        except FileNotFoundError:
            print(f'File: {file} not found')

    # evaluates a string
    def parse(self, arg: str) -> int | str:
        # returns arg if it as a number
        if arg.isnumeric():
            return int(arg)
        # returns arg as a string without quotation marks
        elif arg[0] == '"':
            return arg[1:-1]
        # returns the numeric value of arg if it is a variable
        else:
            return self._heap.get(arg)

    def execute(self, line: str) -> None:
        # creates a string which is the operation in the line
        op = line[:line.find(' ')]
        # creates a list of strings which are the arguments after the operation
        args = line[line.find(' ') + 1:].split()
        # decodes the operation to its respective function
        match op:
            case 'ADD':
                self._add(args[0], self.parse(args[1]), self.parse(args[2]))
            case 'SUB':
                self._sub(args[0], self.parse(args[1]), self.parse(args[2]))
            case 'MUL':
                self._mul(args[0], self.parse(args[1]), self.parse(args[2]))
            case 'DIV':
                self._div(args[0], self.parse(args[1]), self.parse(args[2]))
            case 'STO':
                self._sto(args[0], self.parse(args[1]))
            case 'IN':
                self._in(args[0])
            case 'OUT':
                self._out(self.parse(line[line.find(' ') + 1:]))
            case 'HALT' | 'HAL':
                quit(0)
            # default case from unknown operation name
            case _:
                quit(-1)

    # runs the program
    def run(self):
        program_counter = 0
        # program terminates when a HALT instruction is decoded
        while True:
            instruction = self._memory[program_counter]
            program_counter += 1
            self.execute(instruction)
