%  M = [ 1 2 3; 4 5 6; 7 8 9];
M = [1 0 0; 0 2 1; 1 0 1];

 
% M = [-2 -2 3 ; -10 -1 6; 10 -2 -9];

% T1M = [
%     -691 -585 593 212 733; 
%     -331 -308 342 -82 138;
%     56 -79 -778 27 267;
%     -134 400 -139 -758 -418;
%     704 -435 428 642 -743];



[eigvalue, eigvector] = PowerIteration(M);

B = inv(M);

[smalleig, smalleigvector] = PowerIteration(B);

[V D W] = eig(M);
res = eigs(M, 3, 'sm');