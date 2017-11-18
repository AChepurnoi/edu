%  M = [
%      -691 -585 593 212 733; 
%      -331 -308 342 -82 138;
%      56 -79 -778 27 267;
%      -134 400 -139 -758 -418;
%      704 -435 428 642 -743];
 
 M = [
     894 207 -248 -269 -281;
     207 646 -42 -42 464;
     -248 -42 970 225 -15;
     -269 -42 225 174 -5;
     -281 464 -15 -5 917
 ];
%  M = [4 1 0; 1 2 1; 0 1 1];
 
 [eigval, eigvec] = SMult(M);
 vals = eigval;
 vecs = eigvec;
 for k=2:size(M,1)
    [nextval, nextvec] = SMultNext(M, vecs, 1);
    vals = [nextval vals];
    vecs = [nextvec vecs];
 end
 disp('Values of Multip. Method 1Alg');
 disp(vals);
 disp(vecs);
 
 disp('Values of Matlab function');
 [V D W] = eig(M);
 disp(D);
 disp(V);