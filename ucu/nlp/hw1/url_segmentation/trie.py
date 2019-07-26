from string import ascii_lowercase

class Trie(object):
    def __init__(self, letter="*"):
        self.children = dict()
        self.letter = letter
        self.word = None

    def add(self, word):
        self.__add_inner(word.lower().strip(), 0)

    def __add_inner(self, word, idx):
        if len(word) == idx:
            self.word = word
            return
        
        if word[idx] not in self.children.keys():
            self.children[word[idx]] = Trie(letter=word[idx])
        
        child_node = self.children[word[idx]]
        child_node.__add_inner(word, idx + 1)    

    def __contains_inner(self, word, idx):
        if len(word) == idx:
            return self.word == word

        letter = word[idx]
        if letter in self.children.keys():
            child = self.children[letter]
            return child.__contains_inner(word, idx+1)
        else:
            return False

    
    def contains(self, word):
        return self.__contains_inner(word.lower().strip(), 0)

    def __repr__(self):
        this_repr = f"TrieNode({self.letter}|{self.word})"
        children_repr = "|".join([ f"{k} -> {v.__repr__()}" for (k, v) in self.children.items()])
        return f"{this_repr}|{children_repr}"


def build_trie():
    words = open("words.txt")
    root = Trie()
    for word in words:
        root.add(word)

    return root

if __name__ == "__main__":
    root = build_trie()
    words = open("words.txt")
    for word in words:
        exists = root.contains(word)
        if not exists:
            print("Trie doesn't work ¯\_(ツ)_/¯")