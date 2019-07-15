# DL HW2
## Training - Score 98.76%
```
Train Epoch: 20 [0/60000 (0%)]  Loss: 0.002840  Batch Time: 0.04 seconds
Train Epoch: 20 [6400/60000 (11%)]      Loss: 0.002581  Batch Time: 0.03 seconds
Train Epoch: 20 [12800/60000 (21%)]     Loss: 0.000628  Batch Time: 0.03 seconds
Train Epoch: 20 [19200/60000 (32%)]     Loss: 0.000575  Batch Time: 0.03 seconds
Train Epoch: 20 [25600/60000 (43%)]     Loss: 0.001139  Batch Time: 0.03 seconds
Train Epoch: 20 [32000/60000 (53%)]     Loss: 0.002483  Batch Time: 0.03 seconds
Train Epoch: 20 [38400/60000 (64%)]     Loss: 0.003727  Batch Time: 0.04 seconds
Train Epoch: 20 [44800/60000 (75%)]     Loss: 0.001257  Batch Time: 0.04 seconds
Train Epoch: 20 [51200/60000 (85%)]     Loss: 0.002347  Batch Time: 0.03 seconds
Train Epoch: 20 [57600/60000 (96%)]     Loss: 0.001752  Batch Time: 0.03 seconds

Test set: Average loss: -0.9853, Accuracy: 9876/10000 (98.76%)
```
## Scalar evaluation

#### >10m per batch size 2

## Vectorized evaluation 

#### <0.7s per batch size 2
```
(.env) sasha-macbook:dl sasha$ python simple_conv_net_train.py --log-interval=10 --batch-size=2
Train Epoch: 1 [0/60000 (0%)]   Loss: 2.375791  Batch Time: 0.75 seconds
Train Epoch: 1 [20/60000 (0%)]  Loss: 2.309984  Batch Time: 0.68 seconds
Train Epoch: 1 [40/60000 (0%)]  Loss: 2.331892  Batch Time: 0.70 seconds
Train Epoch: 1 [60/60000 (0%)]  Loss: 2.276419  Batch Time: 0.70 seconds
Train Epoch: 1 [80/60000 (0%)]  Loss: 1.898477  Batch Time: 0.72 seconds
Train Epoch: 1 [100/60000 (0%)] Loss: 1.780480  Batch Time: 0.72 seconds
Train Epoch: 1 [120/60000 (0%)] Loss: 2.746119  Batch Time: 0.69 seconds
Train Epoch: 1 [140/60000 (0%)] Loss: 0.852215  Batch Time: 0.70 seconds
Train Epoch: 1 [160/60000 (0%)] Loss: 1.396215  Batch Time: 0.72 seconds
Train Epoch: 1 [180/60000 (0%)] Loss: 1.906538  Batch Time: 0.71 seconds
Train Epoch: 1 [200/60000 (0%)] Loss: 0.560207  Batch Time: 0.71 seconds
Train Epoch: 1 [220/60000 (0%)] Loss: 1.017997  Batch Time: 0.80 seconds
Train Epoch: 1 [240/60000 (0%)] Loss: 1.112615  Batch Time: 0.69 seconds
```

## Pytorch evaluation

#### <0.01s per batch size 2
```
(.env) sasha-macbook:dl sasha$ python simple_conv_net_train.py --log-interval=10 --batch-size=2
Train Epoch: 1 [0/60000 (0%)]   Loss: 2.407181  Batch Time: 0.03 seconds
Train Epoch: 1 [20/60000 (0%)]  Loss: 1.875978  Batch Time: 0.01 seconds
Train Epoch: 1 [40/60000 (0%)]  Loss: 2.108966  Batch Time: 0.01 seconds
Train Epoch: 1 [60/60000 (0%)]  Loss: 2.160023  Batch Time: 0.01 seconds
Train Epoch: 1 [80/60000 (0%)]  Loss: 1.268837  Batch Time: 0.01 seconds
Train Epoch: 1 [100/60000 (0%)] Loss: 0.451379  Batch Time: 0.01 seconds
Train Epoch: 1 [120/60000 (0%)] Loss: 1.993208  Batch Time: 0.01 seconds
Train Epoch: 1 [140/60000 (0%)] Loss: 1.308916  Batch Time: 0.01 seconds
Train Epoch: 1 [160/60000 (0%)] Loss: 1.128704  Batch Time: 0.01 seconds
Train Epoch: 1 [180/60000 (0%)] Loss: 1.426723  Batch Time: 0.01 seconds
Train Epoch: 1 [200/60000 (0%)] Loss: 0.068013  Batch Time: 0.01 seconds
Train Epoch: 1 [220/60000 (0%)] Loss: 0.560199  Batch Time: 0.01 seconds
Train Epoch: 1 [240/60000 (0%)] Loss: 1.356057  Batch Time: 0.01 seconds
```