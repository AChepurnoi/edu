## LESK
I choose the word `лист` and corrected 50 sentences after running the baseline.
Baseline quality of Lesk 34.00%

### Word count
I tried different ways to make use of word count but didn't find the way that will help me increase the score. 

### TF-IDF
I have calculated occurances words from signatures across all sentences but it didn't help much. The score was about the same or even less. 
Improved quality of Lesk 34.00%

### Improve the context
I added a few substrings from dataset to enrich the context of the word and score become significantly bigger. I have added a few chunks and got score of 66%

### My ideas
I noticed that one of the meanings is more popular in this corpus. To improve the performance we could simply choose the most popular meaning if we can multiple meanings with equal score. This works for current dataset but may not work for other datasets where the distribution of meanings will be different. 
Improved quality of Lesk 74.00%
