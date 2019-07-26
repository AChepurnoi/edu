import random
import re

reflections = {
    "am": "are",
    "was": "were",
    "i": "you",
    "i'd": "you would",
    "i've": "you have",
    "i'll": "you will",
    "my": "your",
    "are": "am",
    "you've": "I have",
    "you'll": "I will",
    "your": "my",
    "yours": "mine",
    "you": "me",
    "me": "you"
}

psychobabble = [
    (
        r'.* ?(I feel) (.*)',
        [
            "Why do {} {}?", # the groups in the regex above will be reflected, then inserted in place of {} here;
                             # if there are no {}, just the string will be returned
            "Why do you think {} {}?",
            "Where do yo think those feelings are coming from?"
        ]
    ),
    (
        r'quit',
        [
            "Thank you for talking with me.",
            "Good-bye.",
            "Thank you, that will be $150. Have a good day!"
        ]
    )
]


def reflect(fragment):
    tokens = fragment.lower().split()
    for i, token in enumerate(tokens):
        if token in reflections:
            tokens[i] = reflections[token]
    return ' '.join(tokens)


def analyze(statement):
    for pattern, responses in psychobabble:
        match = re.match(pattern, statement.rstrip(".!"))
        if match:
            response = random.choice(responses)
            return response.format(*[reflect(g) for g in match.groups()])


def main():
    print("Hello. How are you feeling today?")

    while True:
        statement = input("YOU: ")
        print("ELIZA: " + analyze(statement))

        if statement == "quit":
            break


if __name__ == "__main__":
    main()
