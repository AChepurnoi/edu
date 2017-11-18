%  M = [ 1 2 3; 4 5 6; 7 8 9];
M = [1 0 0; 0 2 1; 1 0 1];

 
% M = [-2 -2 3 ; -10 -1 6; 10 -2 -9];

 M = [
     -691 -585 593 212 733; 
     -331 -308 342 -82 138;
     56 -79 -778 27 267;
     -134 400 -139 -758 -418;
     704 -435 428 642 -743];


% M = [5 1 2; 1 4 1; 2 1 3];
[eigvalue, eigvector] = PowerIteration(M);
% M = [2 -1 1; -1 2 -1; 0 0 3];
[aeigvalue, aeigvector] = PowerIterationAiteken(M);

% [seigval, seigvec] = PowerIterationSecondPair(M, eigvalue, eigvector, 0.001);
% M is negative defined matrix, so do the shift
% B = M - eye(5) * eigvalue;

% [smallval, smallvec] = PowerIteration(B);
% [smallseigval, smallseigvec] = PowerIterationSecondPair(B, smallval, smallvec, 0.001);

% Adding Max eigval to results to get two smalles eigvals
% smallval = smallval + eigvalue;
% smallseigval = smallseigval + eigvalue;


[V D W] = eig(M);
% Check the result
eigvalues = eigs(M);


