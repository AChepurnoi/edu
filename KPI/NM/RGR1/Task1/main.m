% M = [ 1 2 3; 4 5 6; 7 8 9];
M = [1 0 0; 0 2 1; 1 0 1];

% M = [-2 -2 3 ; -10 -1 6; 10 -2 -9];

% [lambda, x] = PowerMethodSecond(M);

[lambda_low, x_low] = PowerMethodSecond(M);

% [y, lam, iters] = power_method(3, inv(M), [100 100 100], 500, 0.01);
[V W D] = eig(M);
