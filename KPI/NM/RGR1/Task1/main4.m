 M = [
     -691 -585 593 212 733; 
     -331 -308 342 -82 138;
     56 -79 -778 27 267;
     -134 400 -139 -758 -418;
     704 -435 428 642 -743];
 
%  M = [4 1 0; 1 2 1; 0 1 1];
 
 [eigval, eigvec] = SMult(M);
 [V D W] = eig(M);