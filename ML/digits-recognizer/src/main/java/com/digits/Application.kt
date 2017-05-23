package com.digits

import org.datavec.api.records.reader.impl.csv.CSVRecordReader
import org.datavec.api.records.reader.impl.csv.CSVSequenceRecordReader
import org.datavec.api.split.FileSplit
import org.datavec.api.util.ClassPathResource
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator
import java.io.File
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.Updater
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.slf4j.LoggerFactory
import org.deeplearning4j.nn.modelimport.keras.trainedmodels.Utils.ImageNetLabels.getLabels
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory.model
import org.nd4j.linalg.api.ndarray.INDArray
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory.model
import org.datavec.api.records.reader.impl.LineRecordReader
import org.datavec.api.records.writer.impl.csv.CSVRecordWriter
import org.datavec.api.writable.IntWritable
import org.datavec.api.writable.Text
import org.datavec.api.writable.Writable
import org.deeplearning4j.eval.Evaluation
import org.nd4j.linalg.cpu.nativecpu.NDArray
import org.nd4j.linalg.dataset.api.DataSet
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize


/**
 * Created by Sasha on 5/19/17.
 */
object Application {


  private val log = LoggerFactory.getLogger(this::class.java)
  val TRAIN = "data/train.csv"
  val TEST = "data/test.csv"


  val labelIndex = 0
  val numClasses = 10
  val batchSize = 42000

  @JvmStatic
  fun main(args: Array<String>) {

    val train = getTrainIterator()
    val it1 = train.next().apply(DataSet::normalize).apply(DataSet::shuffle)
    val data = it1.splitTestAndTrain(.90)


    val nConf = NeuralNetConfiguration.Builder()
            .seed(6)
            .iterations(1)
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .learningRate(0.006)
            .updater(Updater.NESTEROVS).momentum(0.9)
            .list()
            .layer(0, DenseLayer.Builder()
                    .nIn(784)
                    .weightInit(WeightInit.XAVIER)
                    .activation(Activation.RELU)
                    .dropOut(0.5)
                    .nOut(1000)
                    .build())
            .layer(1, OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .nIn(1000)
                    .weightInit(WeightInit.XAVIER)
                    .activation(Activation.SOFTMAX)
                    .nOut(10)
                    .build())
            .pretrain(false)
            .backprop(true)
            .build()
    

    val model = MultiLayerNetwork(nConf)
    model.init()
    model.setListeners(ScoreIterationListener(10))

    for (i in 1..120) {
      model.fit(data.train)
    }

    val eval = Evaluation(numClasses)
    val output = model.output(data.test.featureMatrix)
    eval.eval(data.test.labels, output)
    log.info(eval.stats())


    val test = getTestIterator()
    val it2 = test.next().apply(DataSet::normalize)

    val labels = model.predict(it2.featureMatrix).mapIndexed { index, s -> listOf(IntWritable(index + 1), IntWritable(s)) }

    val predictions = listOf(listOf<Writable>(Text("ImageId"), Text("Label"))) + labels

    val writer = CSVRecordWriter(File("data/result.csv"))
    predictions.forEach { writer.write(it) }

  }

  private fun getTrainIterator(): RecordReaderDataSetIterator{
    val reader = CSVRecordReader(1, ",")
    val file = File(TRAIN)
    reader.initialize(FileSplit(file))
    val train = RecordReaderDataSetIterator(reader, batchSize, labelIndex, numClasses)
    return train
  }

  private fun getTestIterator(): RecordReaderDataSetIterator{
    val reader = CSVRecordReader(1, ",")
    val file = File(TEST)
    reader.initialize(FileSplit(file))
    val train = RecordReaderDataSetIterator(reader, batchSize)
    return train
  }

}