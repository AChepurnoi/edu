function [ eig, eigv ] = PowerIteration(A)
epsilon = 0.001;
r = 2;
p = 3;
sigma = 0.001;
y_init = [1 1 1];
z = (y_init / norm(y_init))';
B = A ^ (2 ^ r);
S_prev = [1 2 3];
z_k = z;

lambda_k = [100 100 100]';

for k = 1:inf
    z_k_prev = z_k;
    y_k = B * z_k_prev;
    lambda_k_prev = lambda_k;
    lambda_k = y_k ./ z_k_prev;
    
    S_prev_prev = S_prev;
    S_prev = find(abs(z_k_prev) > sigma);
    
    shouldNorm = mod(k , p) == 0;
    if shouldNorm 
       z_k = y_k / norm(y_k); 
    else 
       z_k = y_k;
    end
    
    intersection = intersect(S_prev, S_prev_prev)';
    
    shouldStop = PowerMethodStopCriteria(lambda_k, lambda_k_prev, intersection, epsilon);
    
    if shouldStop
        break;
    end
end

y_k_next = A * z_k;
lambda_next = y_k_next ./ z_k;
S = find(abs(z_k) > sigma);
eig = sum(lambda_next) / norm(S);
eigv = y_k_next / norm(y_k_next);

end

