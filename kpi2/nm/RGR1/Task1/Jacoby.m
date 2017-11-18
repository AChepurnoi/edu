function [ eigval, eigvec ] = Jacoby( M )
%JACOBY 0 Algorithm

tol = 0.1;

B = M;
[maxval, idx] = Jacoby_find_max(M);
i = idx(0);
j = idx(1);
p = 2 * B(i,j);
q = B(i, i) - B(j, j);
d = sqrt(p^2 + q^2);
r = abs(q) / (2 * d);
c = sqrt(1/2 + r);
s = sign(p * q) * sqrt(1/2 - r);
B_tmp = B;

B_tmp(i, i) = c^2 * B(i, i) + s^2 * B(j, j) + 2 * s * c * B(i, j);
B_tmp(j, j) = s^2 * B(i, i) + c^2 * B(j, j) - 2 * s * c * B(i, j);







end

