"""
Luke Jarvis
2/26/2023
"""
import vm

# creates a new vm and runs is
if __name__ == '__main__':
    filename = 'mySmallVm_Prog.txt'
    SmallVm = vm.vm(filename)
    SmallVm.run()
