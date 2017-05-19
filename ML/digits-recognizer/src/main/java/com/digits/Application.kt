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
import org.deeplearning4j.eval.Evaluation
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize


/**
 * Created by Sasha on 5/19/17.
 */
object Application {


  private val log = LoggerFactory.getLogger(this::class.java)
  val TRAIN = "data/train.csv"

  @JvmStatic
  fun main(args: Array<String>) {

    val labelIndex = 0
    val numClasses = 10
    val batchSize = 695

    val reader = CSVRecordReader(1, ",")
    val file = File(TRAIN)
    reader.initialize(FileSplit(file))


    val train = RecordReaderDataSetIterator(reader, batchSize, labelIndex, numClasses)

    val t1 = train.next()

    t1.normalize()

    val nConf = NeuralNetConfiguration.Builder()
            .seed(6)
            .iterations(10)
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .learningRate(0.1)
            .list()
            .layer(0, DenseLayer.Builder()
                    .nIn(784)
                    .weightInit(WeightInit.XAVIER)
                    .activation(Activation.RELU)
                    .nOut(100)
                    .build())
            .layer(1, OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .nIn(100)
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

    for (i in 1..320) {
      model.fit(t1)
    }

    val eval = Evaluation(numClasses)
    val output = model.output(t1.featureMatrix)
    eval.eval(t1.labels, output)
    log.info(eval.stats())



  }
}