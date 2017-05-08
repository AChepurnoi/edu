function [ eigval, eigvec ] = SMult( M )
%1 alg

y = ones(size(M,1),1);
epsilon = 0.001;
lambda_prev = ones(size(M,1),1);
lambda = 1;

s = norm(y * y');
p = norm(y);
% z_prev = [1 1 1 1 1];
z = y ./ p;

for k = 1:inf
    y = M * z;
    s = norm(y * y');
    t = norm(y * z');
    p = norm(y);
    lambda_prev = lambda;
    lambda = s ./ t;
    z = y ./ p;
    
    if (abs(lambda - lambda_prev) <= epsilon)
        break;
    end
    
    
    
end

eigval = lambda;
eigvec = z;

end

