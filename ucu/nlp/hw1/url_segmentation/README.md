# Deterministic Url and HashTag Segmentation

You get email names or Twitter Hash Tags on the input and need to return only segmented words, like so:

Domain Name Examples:-
``www.checkdomain.com => check domain
www.bigrock.com => big rock
www.namecheap.com => name cheap
www.appledomains.in => apple domains``

Twitter Hash Tag Examples:
``#honestyhour => honesty hour
#beinghuman => being human
#followback => follow back
#socialmedia => social media
#30secondstoearth => 30 seconds to earth``

There is a file input.txt which should be processed, and output.txt, which is what you should be getting if your algorithm is working correctly. Use it to evaluate yourself.

There is a file words.txt, which is your dictionary. You should be looking up existing words only in that dictionary, plus you should be picking up numbers. So a token on the output can be:
	- a word from the dictionary
	- an integer or a decimal number


There might be cases where it might be possible to parse (or split) an input string into tokens in multiple possible ways.

``currentratesoughttogodown``

This can be split into:

``current rate sought to go down``
or
``current rates ought to go down.``

``thisisinsane``

This can be split into:
``this is in sane``
or
``this is insane``

Write your splitter in such a way, that as you tokenise a string from left to right; in case there are multiple possible ways to split the string, select the longest possible string from the left side, such that the remaining string can be split into valid tokens. So, for the two cases above, the appropriate ways to split the strings are:

``current rates ought to go down
this is insane``

In case there is no valid way to split the string into a valid sequence of tokens, output the original string itself, after scrubbing out the # for hashtags, the 'www' and extensions for domain names.

Please note, that the "words.txt" file is a list of several common words, but it will not necessarily produce the ideal natural language segmentation for each of the examples, samples or tests. That is the expected behavior: we are only trying to gauge how well you can segment these hashtags and domain names, with this limited list of words.

Tip: Use the trie data structure and recursion for best results.
