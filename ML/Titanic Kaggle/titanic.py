
from sklearn.linear_model import LogisticRegression
from sklearn import preprocessing
import pandas
import numpy as np

def getSexKey(sex):
	if(sex == "male"):
		return 1
	else: 
		return 0

def simplifySex(df):
	df.Sex = df.Sex.apply(lambda x: getSexKey(x))

filename = 'train.csv'

names = ['PassengerId','Survived','Pclass','Name','Sex','Age','SibSp','Parch','Ticket','Fare','Cabin','Embarked']
X = pandas.read_csv(filename, names=names).ix[1:]
Y = X['Survived']
X.drop(X.columns[[0, 1]], axis=1, inplace=True)
X.drop(['Name', 'SibSp', 'Parch', 'Ticket', 'Cabin', 'Embarked'],axis = 1, inplace=True)
simplifySex(X)
X.Age.fillna(-1, inplace=True)

cl = LogisticRegression()
cl.fit(X,Y)

res = cl.score(X,Y)
print res

test = pandas.read_csv("test.csv", names=['PassengerId','Pclass','Name','Sex','Age','SibSp','Parch','Ticket','Fare','Cabin','Embarked']).ix[1:].reset_index(drop=True)
test.drop(['Name', 'SibSp', 'Parch', 'Ticket', 'Cabin', 'Embarked'],axis = 1, inplace=True)
print(test)
data = test.drop(['PassengerId'],axis = 1)

simplifySex(data)
data.Age.fillna(-1, inplace=True)
data.Fare.fillna(0,inplace=True)
testRes = pandas.DataFrame(cl.predict(data))
print(testRes)
result = pandas.DataFrame()
result['PassengerId'] = test.PassengerId
result['Survived'] = testRes[0]
print(result)
result.to_csv("result.csv", index=False)


