function [ eigval, eigvec ] = PowerIterationSecondPair( M, first_eig, first_vec, tol)
%POWERITERATIONSECONDPAIR Summary of this function goes here
%   Detailed explanation goes here
matrix_size = size(M, 1);
epsilon = tol * 2;
sigma = epsilon;

y_k = ones(matrix_size, 1) .* 5;

S_prev = 1:matrix_size;
y_k_prev = ones(matrix_size, 1) .* 5;
lambda_k_prev = ones(matrix_size, 1) .* 5;
lambda_k = ones(matrix_size, 1) .* 5;

check = norm(y_k - first_vec) > 100 * tol;

for k = 1:inf
   S_prev_prev = S_prev;
   y_k_prev_prev = y_k_prev;
   y_k_prev = y_k;
   lambda_k_prev_prev = lambda_k_prev;
   lambda_k_prev = lambda_k;
   
%    New iteration starts
   y_k =  M * y_k_prev;
   lambda_k = (y_k - first_eig * y_k_prev) ./ (y_k_prev - first_eig * y_k_prev_prev);
%    possibleEigVal = sum(lambda_k(S_prev)) / size(S_prev, 1)
   S_prev = find(abs(y_k_prev - first_eig * y_k_prev_prev) > sigma);
   
   

   
   shouldGarvik = or(all(lambda_k > 0), all(lambda_k < 0));
   if and(shouldGarvik, k ~= 1)
%        diffs = [abs(lambda_k - lambda_k_prev),abs(lambda_k_prev - lambda_k_prev_prev)]
       shouldStop = all(abs(lambda_k - lambda_k_prev) >= abs(lambda_k_prev - lambda_k_prev_prev));
       if shouldStop 
          y_k = y_k_prev;
          lambda_k = lambda_k_prev;
          S_prev = S_prev_prev;
          break;
       end
   end
   
   shouldStop = PowerMethodStopCriteria(lambda_k, lambda_k_prev, S_prev, epsilon);
 
   if shouldStop
       break;
   end
end

eigval = sum(lambda_k(S_prev)) / size(S_prev, 1);
eigvec = (y_k - first_eig * y_k_prev) ./ norm(y_k - first_eig * y_k_prev);

end

