## Obesrvations
### Features
To increase performance I decided to generate following features:
* Use XPOSTAG
* Use one more word from stack and queue as feature
* Check if UPOSTAGs of first word from stack and queue matches
* Check distance of first word from stack and second word from queue
* Check distance of second word from stack and second word from queue
* Check if UPOSTAGs of second word from stack and second word from queue matches
* Check if UPOSTAGs of first word from stack and second word from queue matches
* Check distance of first word from stack and third word from queue
* Check distance of third word from stack and third word from queue
* Check if UPOSTAGs of third word from stack and third word from queue matches
* Check if UPOSTAGs of first word from stack and third word from queue matches

### Classifier
As our feature space is very sparse it doesn't make sense to use neural netoworks of tree based models. 
I tried to train a SVM with rbf/linear kernel but didn't improve the result and decided to keep linear classifier. 

