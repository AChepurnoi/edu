function [ lambda_res,x_res ] = PowerMethodSecond(M)
p = 3;
r = 2;
sigma = 0.001;
lambda = 10;
y = [1 1 1];
epsilon = 0.001;
z =  y / norm(y);
S = [1 2 3];
B = M^(2^r);
lambdas = [100 100 100];

for k = 1:inf
    y = B * z';
    S_prev = S;
    S = find(y > sigma);
    lambdas_prev = lambdas;
    lambdas = [];

    for i = 1:3
       approx = y(i) / z(i);
       lambdas = [lambdas approx];
    end


    shouldNorm = mod(k, p) == 0;

    z_prev = z;
    if shouldNorm
        z = y' / norm(y');
    else 
        z = y';
    end
    
    indecies = intersect(S, S_prev);
    
    criteria = PowerMethodStopCriteria(lambdas, lambdas_prev, indecies, epsilon);
    
    if criteria
        break
    end

end

y_last = M * z';
S = find(z > sigma);
lambdas = [];

for i = 1:3
   approx = y_last(i) / z(i);
   lambdas = [lambdas approx];
end


lambda_res = sum(lambdas) / norm(S);
x_res = y_last / norm(y_last);



end

