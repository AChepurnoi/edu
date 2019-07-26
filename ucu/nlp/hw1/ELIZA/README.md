# ELIZA the therapist chatbot

EIZA acts as a therapist. Not just any therapist, though: a Rogerian psychotherapist.

This is key information in this case because Rogerian psychotherapy employs a unique approach called 'person-centered'. A Rogerian psychotherapist's process is to interact with the patient with complete empathy and lack of judgement.

To do this, the psychotherapist asks person-centric questions to the patient. For instance, if a patient were to say 'I feel depressed', a Rogerian psychotherapist would try to dig further by asking 'Why do you feel down?'.

This type of therapy is a back and forth of statements from the patient and questions from the therapist. The goal is to uncover realisations by digging deeper and deeper.

`reflections`  maps first-person pronouns to second-person pronouns and vice-versa. It is used to “reflect” a statement back against the user, like so:

```
YOU: I am feeling sad today.
ELIZA: So you are feeling sad today, why do you think that is?
```


`psychobabble`  is made up of a list of lists where the first element is a regular expression that matches the user’s statements and the second element is a list of potential responses. Many of the potential responses contain placeholders that can be filled in with fragments to echo the user’s statements.

Write at least 20 more regex that would handle different user's responses in a smart way.
