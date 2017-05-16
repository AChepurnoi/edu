
import numpy as np
import sklearn as learn
import pandas as pd
train_data = pd.read_csv('train.csv', sep=',',header=None)
df = train_data.drop(train_data.index[0])
labels = df[0]
dataset = df.drop([0], axis = 1).reset_index(drop=True)
from sklearn.neural_network import MLPClassifier
clf = MLPClassifier(solver='lbfgs', alpha=1e-5, hidden_layer_sizes=(75, 600),learning_rate_init=0.1,
                    random_state=1, verbose=True)
dataset = dataset.apply(pd.to_numeric)
labels = labels.apply(pd.to_numeric)
size = dataset.shape[0]
trainsize = int(size / 1.3)
clf.fit(dataset, labels)
test = pd.read_csv('test.csv', sep=',',header=None).drop([0], axis = 0)
res = clf.predict(test)
submissions=pd.DataFrame({"ImageId": list(range(1,len(res)+1)),
                         "Label": res})
submissions.to_csv("DR.csv", index=False, header=True)



