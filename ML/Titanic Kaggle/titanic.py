
from sklearn.linear_model import LogisticRegression
from sklearn import preprocessing
import matplotlib.pyplot as plt
import pandas
import numpy as np


def getSexKey(sex):
	if(sex == "male"):
		return 1
	else: 
		return 0

def simplifySex(df):
	df.Sex = df.Sex.apply(lambda x: getSexKey(x))


def getAgeKey(ageStr):
	age = float(ageStr) 
	if age < 18:
		return 0
	elif age < 30:
		return 1
	elif age < 50:
		return 2
	else: 
		return 3

def simplifyAge(df):
	df.Age = df.Age.apply(lambda x: getAgeKey(x))



def getNameKey(str):
	titles = [('Mr', 0), ('Mrs', 1) ,('Miss', 2 ), ('Master',3 ),('Rev', 4)]

	for title, key in titles:
		if title in str:
			return key

	return -1


def simplifyName(df):
	df.Name = df.Name.apply(lambda x: getNameKey(x))


# Training classifier 

filename = 'train.csv'
names = ['PassengerId','Survived','Pclass','Name','Sex','Age','SibSp','Parch','Ticket','Fare','Cabin','Embarked']
X = pandas.read_csv(filename, names=names).ix[1:].reset_index(drop = True)
Y = X['Survived']

simplifyName(X)
simplifyAge(X)
simplifySex(X)
X.drop(['SibSp', 'Parch', 'Ticket', 'Cabin', 'Embarked', 'Fare', 'PassengerId', 'Survived'],axis = 1, inplace=True)

cl = LogisticRegression()
cl.fit(X,Y)

res = cl.score(X,Y)


# Prediciton

test_file  = 'test.csv'
test_columns = ['PassengerId','Pclass','Name','Sex','Age','SibSp','Parch','Ticket','Fare','Cabin','Embarked']
X_test = pandas.read_csv(test_file, names=test_columns).ix[1:].reset_index(drop = True)
simplifyName(X_test)
simplifyAge(X_test)
simplifySex(X_test)
print X_test
Ids = X_test.PassengerId
print Ids
X_test.drop(['SibSp', 'Parch', 'Ticket', 'Cabin', 'Embarked', 'Fare', 'PassengerId'],axis = 1, inplace=True)

prediciton = pandas.DataFrame(cl.predict(X_test))

result = pandas.DataFrame()
result['PassengerId'] = Ids
result['Survived'] = prediciton[0]
print(result)
result.to_csv("result2.csv", index=False)








