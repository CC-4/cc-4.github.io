#!/usr/bin/python3

import os
import gzip
import pickle
from autograder import cprint
from autograder import colored


# max score
SCORE = 63


# creates grading and grading/test-output directories
def start():
    os.system('clear')
    os.system('rm -rf ' + os.path.join('grading'))
    os.system('rm -rf ' + os.path.join('grading', 'test-output'))
    os.system('mkdir ' + os.path.join('grading'))
    os.system('mkdir ' + os.path.join('grading', 'test-output'))


# creates an output file
def create(name, data):
    if not os.path.exists(os.path.join('grading', name)):
        f = open(os.path.join('grading', name), 'w')
        f.write(data)
        f.close()


# executes a test
def execute(name):
    test = os.path.join('grading', name + '.cl')
    testout = os.path.join('grading', '.tmp.s')
    os.system('./mycoolc -o ' + testout + ' ' + test + ' 2>> grading/.tmp')
    os.system('jupitercl ' + testout + ' >> grading/.tmp 2>&1')
    testout = open('grading/.tmp', 'r')
    testresult = testout.read().strip()
    testresult = testresult.replace('Increasing heap...', '')
    testresult = testresult.split('\n')
    testresult = testresult[:-2]
    testresult = list(filter(lambda x: len(x) > 0, testresult))
    testresult = '\n'.join(testresult).strip()
    testout.close()
    return testresult


# copy result
def copy(name, data):
    f = open('grading/test-output/' + name + '.cl.out', 'w')
    f.write(data)
    f.close()


# calls diff
def diff(name):
    cmd = 'diff grading/%s.cl.out grading/test-output/%s.cl.out' % (name, name)
    cmd += ' > grading/test-output/%s.diff' % name
    os.system(cmd)


# clear temp files
def clear():
    os.system('rm -rf grading/.tmp')
    os.system('rm -rf grading/.tmp.s')


# main script
def main():
    f = gzip.open(os.path.join('autograder', 'data.pkl.gz'), 'rb')
    data = pickle.load(f)
    f.close()
    start()
    print('')
    cprint('                          Autograder', color='blue')
    print('')
    print('')
    os.system('make cgen > /dev/null 2>&1')
    grade = 0
    gc = []
    for sample in data:
        # unpack test data
        input, output, result, points, name, desc = sample
        # create test input
        create(name + '.cl', input)
        # create test expected result
        create(name + '.cl.out', result)
        # create test expected .s
        create(name + '.cl.s', output)
        # run test
        uresult = execute(name)
        # compare
        if uresult != result:
            copy(name, uresult)
            diff(name)
            info = colored('-%d' % points, 'red')
            info += colored('(%s)' % name, 'cyan').center(37, ' ')
            info += colored(' %s' % desc, 'white')
            print(info)
        else:
            grade += points
            if name in ['lam-gc', 'simple-gc']:
                gc.append(name)
            info = colored('+%d' % points, 'green')
            info += colored('(%s)' % name, 'cyan').center(37, ' ')
            info += colored(' %s' % desc, 'white')
            print(info)
        # clear output
        clear()
    # chek "garbage colllector" tests
    first = False
    if 'simple-gc' in gc:
        t1 = colored('\n[GC Test Case: simple-gc.cl ', 'white')
        t1 += colored('OK', 'green') + colored(']', 'white')
        print(t1)
        first = True
    if 'lam-gc' in gc:
        if not first:
            print('')
        t1 = colored('[GC Test Case: lam-gc.cl ', 'white')
        t1 += colored('OK', 'green') + colored(']', 'white')
        print(t1)
    print('\n')
    cprint('=> You got a score of %d out of 63.' % grade, color='yellow')
    # save score
    f = open('grading/SCORE', 'w')
    f.write('%d' % grade)
    f.close()


if __name__ == '__main__':
    main()
