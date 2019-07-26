import pprint
from typing import Tuple
from collections import defaultdict
import re

def segment_line(root, line):
    matches = re.findall('^(?:#)?((?:\w+))(\.\d+)?(?:\.[a-zA-Z]*)?', line)[0] #We have only 1 line here
    clear_line = "".join(matches)
    res = segment(root, clear_line, len(clear_line), [])
    print(res)
    res = " ".join(res)
    return res

def segment(root, line, idx, acc):
    print(line, idx, acc)
    if idx == -1:
        print("Can't find match. SOS. Line: ", line)
        return acc
    if len(line) == 0:
        return acc

    substr = line[:idx]
    rest = line[idx:]

    numberMatch = re.match("^(?:\d+)(?:\.)?(?:\d*)$", substr)
    if numberMatch is not None:
        return segment(root, rest, len(rest), [*acc, substr]) 

    print("Checking: ", substr)
    contains = root.contains(substr)
    print("Result: ", contains)
    if contains:
        return segment(root, rest, len(rest), [*acc, substr]) 
    else:
        return segment(root, line, idx - 1, acc)

def segment_input_tokens(root):
    f = open("input.txt")
    for line in f:
        result = segment_line(root, line)
        yield result
    
def read_golden_file():
    f = open("output.txt")
    for line in f:
        yield line.strip()

def score(guess_list, gold_list):
    guess_set = set(guess_list)
    gold_set = set(gold_list)

    tp = guess_set.intersection(gold_set)
    fp = guess_set - gold_set
    fn = gold_set - guess_set

    pp = pprint.PrettyPrinter()
    print('True Positives (%d): ' % len(tp))
    pp.pprint(tp)
    print('False Positives (%d): ' % len(fp))
    pp.pprint(fp)
    print('False Negatives (%d): ' % len(fn))
    pp.pprint(fn)
    print('Summary: tp=%d, fp=%d, fn=%d' % (len(tp),len(fp),len(fn)))


from trie import Trie, build_trie

if __name__ == '__main__':
    trie = build_trie()
    results = segment_input_tokens(trie)
    golden_file = read_golden_file()
    score(results, golden_file)