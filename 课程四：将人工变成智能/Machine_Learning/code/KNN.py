from cffi.backend_ctypes import xrange
from numpy import *
import csv
import os


def toInt(array):
    array = mat(array)
    m, n = shape(array)
    newArray = zeros((m, n))
    for i in xrange(m):
        for j in xrange(n):
            newArray[i, j] = int(array[i, j])
    return newArray


def nomalizing(array):
    m, n = shape(array)
    for i in xrange(m):
        for j in xrange(n):
            if array[i, j] != 0:
                array[i, j] = 1
    return array


def loadTrainData():
    l = []
    with open(os.path.join(os.path.abspath('..') + '/dataset', 'train.csv')) as file:
        lines = csv.reader(file)
        for line in lines:
            l.append(line)  # 42001*785
    l.remove(l[0])
    l = array(l)
    label = l[:, 0]
    data = l[:, 1:]
    return nomalizing(toInt(data)), toInt(label)  # label 1*42000  data 42000*784
    # return trainData,trainLabel


def loadTestData():
    l = []
    with open(os.path.join(os.path.abspath('..') + '/dataset', 'test.csv')) as file:
        lines = csv.reader(file)
        for line in lines:
            l.append(line)  # 28001*784
    l.remove(l[0])
    data = array(l)
    return nomalizing(toInt(data))  # data 28000*784
    # return testData


def loadTestResult():
    l = []
    with open(os.path.join(os.path.abspath('..') + '/dataset', 'knn_benchmark.csv')) as file:
        lines = csv.reader(file)
        for line in lines:
            l.append(line)  # 28001*2
    l.remove(l[0])
    label = array(l)
    return toInt(label[:, 1])  # label 28000*1


# result是结果列表
# csvName是存放结果的csv文件名
def saveResult(result, csvName):
    with open(csvName, 'w', newline = "") as myFile:
        my_writer = csv.writer(myFile)
        for i in result:
            tmp = [i]
            my_writer.writerow(tmp)


# 调用scikit的knn算法包
from sklearn.neighbors import KNeighborsClassifier


def knnClassify(trainData, trainLabel, testData):
    print("正在使用KNN算法计算")
    knnClf = KNeighborsClassifier()  # default:k = 5,defined by yourself:KNeighborsClassifier(n_neighbors=10)
    knnClf.fit(trainData, ravel(trainLabel))
    testLabel = knnClf.predict(testData)
    saveResult(testLabel, os.path.join(os.path.abspath('..') + '/result', 'sklearn_knn_Result.csv'))
    return testLabel


# 调用scikit的SVM算法包
from sklearn import svm


def svcClassify(trainData, trainLabel, testData):
    print("正在使用SVM算法计算")
    svcClf = svm.SVC(
        C=5.0)  # default:C=1.0,kernel = 'rbf'. you can try kernel:‘linear’, ‘poly’, ‘rbf’, ‘sigmoid’, ‘precomputed’
    svcClf.fit(trainData, ravel(trainLabel))
    testLabel = svcClf.predict(testData)
    saveResult(testLabel, os.path.join(os.path.abspath('..') + '/result', 'sklearn_SVC_C=5.0_Result.csv'))
    return testLabel


# 调用scikit的朴素贝叶斯算法包,GaussianNB和MultinomialNB
from sklearn.naive_bayes import GaussianNB  # nb for 高斯分布的数据


def GaussianNBClassify(trainData, trainLabel, testData):
    print("正在使用高斯分布算法计算")
    nbClf = GaussianNB()
    nbClf.fit(trainData, ravel(trainLabel))
    testLabel = nbClf.predict(testData)
    saveResult(testLabel, os.path.join(os.path.abspath('..') + '/result', 'sklearn_GaussianNB_Result.csv'))
    return testLabel


from sklearn.naive_bayes import MultinomialNB  # nb for 多项式分布的数据


def MultinomialNBClassify(trainData, trainLabel, testData):
    print("正在使用多项式算法计算")
    nbClf = MultinomialNB(
        alpha=0.1)  # default alpha=1.0,Setting alpha = 1 is called Laplace smoothing, while alpha < 1 is called Lidstone smoothing.
    nbClf.fit(trainData, ravel(trainLabel))
    testLabel = nbClf.predict(testData)
    saveResult(testLabel, os.path.join(os.path.abspath('..') + '/result', 'sklearn_MultinomialNB_alpha=0.1_Result.csv'))
    return testLabel


def digitRecognition():
    trainData, trainLabel = loadTrainData()
    testData = loadTestData()
    # 使用不同算法
    result1 = knnClassify(trainData, trainLabel, testData)
    result2 = svcClassify(trainData, trainLabel, testData)
    result3 = GaussianNBClassify(trainData, trainLabel, testData)
    result4 = MultinomialNBClassify(trainData, trainLabel, testData)

    # 将结果与跟给定的knn_benchmark对比,以result1为例
    resultGiven = loadTestResult()
    m, n = shape(testData)
    different = 0  # result1中与benchmark不同的label个数，初始化为0
    print("以下为4种模型的评估：")
    for i in xrange(m):
        if result1[i] != resultGiven[0, i]:
            different += 1
    print("The KNN model error rate is: ", (different/m)*100, "%")
    different = 0
    for i in xrange(m):
        if result2[i] != resultGiven[0, i]:
            different += 1
    print("The svm model error rate is: ", (different / m) * 100, "%")
    different = 0
    for i in xrange(m):
        if result3[i] != resultGiven[0, i]:
            different += 1
    print("The GaussianNB model error rate is: ", (different / m) * 100, "%")
    different = 0
    for i in xrange(m):
        if result4[i] != resultGiven[0, i]:
            different += 1
    print("The MultinomialNB model error rate is: ", (different / m) * 100, "%")



