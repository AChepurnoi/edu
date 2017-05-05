function [ eigval, eigvec ] = scalarMuplitplications1( M )
%SCALARMUPLITPLICATIONS1 Summary of this function goes here
%   Detailed explanation goes here

y = [1 1 1 1 1]';
epsilon = 0.001;
lambda_prev = [1 1 1 1 1]';
lambda = [1 1 1 1 1]';

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
    
    if all((lambda - lambda_prev) <= epsilon)
        break;
    end
    
    
    
end

eigval = 1;
eigvec = 1;

end

