from __future__ import print_function
import torch


def diff_mse(x, y):
    x_vec = x.view(1, -1).squeeze()
    y_vec = y.view(1, -1).squeeze()
    return torch.mean(torch.pow((x_vec - y_vec), 2)).item()


def conv2d_scalar(x_in, conv_weight, conv_bias, device):
    batch_size, c_in, h, w = x_in.shape
    c_out, _, kernel_size, _ = conv_weight.shape
    output_height = h - kernel_size + 1
    output_width = w - kernel_size + 1

    res = torch.zeros((batch_size, c_out, output_height, output_width)).to(device)

    # Batch iter
    for batch in range(batch_size):
        for c in range(c_out):
            for height in range(output_height):
                for width in range(output_width):
                    # Filter convolution
                    conv_res = 0
                    for c_input in range(c_in):
                        for i in range(kernel_size):
                            for j in range(kernel_size):
                                conv_res += x_in[batch, c_input, height + i, width + j] * conv_weight[c, c_input, i, j]

                    res[batch, c, height, width] = conv_res + conv_bias[c]
    return res

def conv2d_vector(x_in, conv_weight, conv_bias, device):
    batch_size, c_in, h, w = x_in.shape
    c_out, _, kernel_size, _ = conv_weight.shape
    conv_weights = conv_weight2rows(conv_weight)

    output_height = h - kernel_size + 1
    output_width = w - kernel_size + 1
    
    res = torch.empty((batch_size, c_out, output_height, output_width)).to(device)

    for batch_item in range(batch_size):
            col = im2col(x_in[batch_item], kernel_size, device).t()
            # print(col.shape)
            # print(conv_weights.shape)
            res[batch_item] = (conv_weights.matmul(col) + conv_bias.view(-1, 1))\
                        .view(c_out, output_height, output_width)

    return res

def im2col(X, kernel_size, device, stride=1):
    channels, height, width = X.size()

    output_height = ((height - kernel_size) // stride) + 1
    output_width = ((width - kernel_size) // stride) + 1

    res = torch.zeros((output_height * output_width * channels, kernel_size * kernel_size)).to(device)
    
    for c in range(channels):
        for h in range(0, output_height):
            h_im = h * stride
            for w in range(0, output_width):
                w_im = w * stride
                res[c+h*output_width+w] = X[c, h_im:h_im+kernel_size, w_im:w_im+kernel_size].contiguous().view(1, -1)

    return res



def conv_weight2rows(conv_weight):
    channels_out, channels_in, _, _= conv_weight.shape
    return conv_weight\
        .clone()\
        .view((channels_out * channels_in, -1))


def pool2d_scalar(a, device):
    batch_size, channels, height, width = a.shape

    output_heigth = height // 2
    output_width = width // 2
    res = torch.zeros((batch_size, channels, output_heigth, output_width)).to(device)

    for batch in range(batch_size):
        for c in range(channels):
            for i in range(output_heigth):
                for j in range(output_width):
                    res[batch, c, i, j] = max(a[batch, c, i * 2, j * 2], a[batch, c, i * 2, j * 2 + 1],
                                         a[batch, c, i * 2 + 1, j * 2], a[batch, c, i * 2 + 1, j * 2 + 1])

    return res


def pool2d_vector(a, device):
    batch_size, channels, height, width = a.shape
    output_height, output_width = height // 2, width // 2

    res = torch.empty((batch_size, channels, output_height, output_width)).to(device)
    
    for batch_item in range(batch_size):
            col = im2col(a[batch_item], 2, device, stride=2).t()
            max_batch = col.max(dim=0)[0].view(channels, output_height, output_width)
            res[batch_item] = max_batch
            
    return res


def relu_scalar(a, device):
    batch_size, vector_size = a.shape
    res = torch.zeros((batch_size, vector_size)).to(device)

    for batch in range(batch_size):
        for i in range(vector_size):
            res[batch, i] = max(a[batch, i], 0)
    return res


def relu_vector(a, device):
    copy = a.clone().to(device)
    copy[a < 0] = 0
    return copy


def reshape_vector(a, device):
    return a.clone().to(device).view((a.shape[0], -1))

def reshape_scalar(a, device):
    batch_size, c_in, height, width = a.shape
    res = torch.zeros((batch_size, c_in * height * width)).to(device)

    for batch in range(batch_size):
        for c in range(c_in):
            for i in range(height):
                for j in range(width):
                    index = c * height * width + i * height + j
                    res[batch, index] = a[batch, c, i, j]
    return res

def fc_layer_scalar(a, weight, bias, device):
    batch_size, vector_size = a.shape
    output_size, _ = weight.shape
    res = torch.zeros((batch_size, output_size)).to(device)

    for batch in range(batch_size):
        for out_idx in range(output_size):
            res[batch, out_idx] = bias[out_idx]
            for in_idx in range(vector_size):
                res[batch, out_idx] += weight[out_idx, in_idx] * a[batch, in_idx]
            
    return res

def fc_layer_vector(a, weight, bias, device):
    a, weight, bias = a.to(device), weight.to(device), bias.to(device)
    return a.matmul(weight.t()).add(bias).clone()
