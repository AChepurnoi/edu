import sys
import os
import re
import pprint

email_pattern_generic = '((?:[a-zA-Z]+\.*){1,3})\s*(?:@|&#x40;)\s*((?:[a-zA-Z]+\.){1}(?:\.*[a-zA-Z]+)+)'
email_pattern_three_subdomains = '(\w+)\sat\s(\w+)(?:\.|\sdot\s|;)(\w+)(?:\.|\sdot\s|;)(edu)'
email_pattern_two_subdomain_in_html_tag = '(\w+)(?:\s|%20)(?:at|WHERE)(?:\s|%20)(\w+)(?:\s|%20)(?:dt|dot|DOM)(?:\s|%20)(edu|com)'
email_obfuscate_pattern = 'obfuscate\(\'(.*)\',\'(.*)\'\)'
email_followed_by_pattern = '>(\w+\.*\w*)\s.*@(.*)(?:&|\")'
number_pattern = '(?:\D){0,1}([0-9]{3})\D{0,5}([0-9]{3})\D{1}([0-9]{4})(?:\D)'


def process_file(name, f):
    res = []
    for line in f:
        for pattern in [email_followed_by_pattern, email_pattern_generic]:
            email_matches = re.findall(pattern,line)
            for m in email_matches:
                email = '%s@%s' % m
                res.append((name,'e',email))

        email_matches = re.findall(email_pattern_three_subdomains,line)
        for m in email_matches:
            email = '%s@%s.%s.%s' % m
            res.append((name,'e',email))
        
        email_matches = re.findall(email_pattern_two_subdomain_in_html_tag,line)
        for m in email_matches:
            email = '%s@%s.%s' % m
            res.append((name,'e',email))
        
        email_matches = re.findall(email_obfuscate_pattern,line)
        for m in email_matches:
            email = '%s@%s' % (m[1], m[0])
            res.append((name,'e',email))

        number_matches = re.findall(number_pattern,line)
        for m in number_matches:
            number = '%s-%s-%s' % m
            res.append((name,'p',number))

    return res

def process_dir(data_path):
    # get candidates
    guess_list = []
    for fname in os.listdir(data_path):
        if fname[0] == '.':
            continue
        path = os.path.join(data_path,fname)
        f = open(path, 'r')
        f_guesses = process_file(fname, f)
        guess_list.extend(f_guesses)
    return guess_list

def get_gold(gold_path):
    # get gold answers
    gold_list = []
    f_gold = open(gold_path, 'r')
    for line in f_gold:
        gold_list.append(tuple(line.strip().split('\t')))
    return gold_list


def score(guess_list, gold_list):
    guess_list = [(fname, _type, value.lower()) for (fname, _type, value) in guess_list]
    gold_list = [(fname, _type, value.lower()) for (fname, _type, value) in gold_list]
    guess_set = set(guess_list)
    gold_set = set(gold_list)

    tp = guess_set.intersection(gold_set)
    fp = guess_set - gold_set
    fn = gold_set - guess_set

    pp = pprint.PrettyPrinter()
    #print 'Guesses (%d): ' % len(guess_set)
    #pp.pprint(guess_set)
    #print 'Gold (%d): ' % len(gold_set)
    #pp.pprint(gold_set)
    print('True Positives (%d): ' % len(tp))
    pp.pprint(tp)
    print('False Positives (%d): ' % len(fp))
    pp.pprint(fp)
    print('False Negatives (%d): ' % len(fn))
    pp.pprint(fn)
    print('Summary: tp=%d, fp=%d, fn=%d' % (len(tp),len(fp),len(fn)))


def main(data_path, gold_path):
    guess_list = process_dir(data_path)
    gold_list =  get_gold(gold_path)
    score(guess_list, gold_list)


if __name__ == '__main__':
    main("emails/", "res_gold.txt")
