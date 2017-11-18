function [ eigval, eigvec ] = SMultNext( M, eigvecs, q)
%SMULTJ Summary of this function goes here
%   Detailed explanation goes here

y = ones(size(M,1),1);
epsilon = 0.001;
lambda_prev = 1;
lambda = 1;


sum = 0;
for kk = eigvecs
   sum = sum + (y' * kk) * kk; 
end
y = y - sum;

s = y' * y;
p = norm(y);
z = y ./ p;

for k = 1:inf
    y = M * z;
    s = y' * y;
    t = y' * z;
    p = norm(y);
    lambda_prev = lambda;
    lambda = s ./ t;
    z_prev = z;
    z = y ./ p;
    
    if(mod(k, q) == 0)
       sum = 0;
       for kk = eigvecs
          sum = sum + (z' * kk) * kk; 
       end
       z = z - sum;
    end
    
    if (or(abs(lambda - lambda_prev) <= epsilon,norm(z - z_prev) <= epsilon))
        break;
    end
    
    
    
end

eigval = lambda;
eigvec = z;

end

