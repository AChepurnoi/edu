function [ val, index ] = Jacoby_find_max( M )
%JACOBY_FIND_MAX Summary of this function goes here
%   Detailed explanation goes here

val = 0;
index = [];
for i = 1:size(M,1)
   for j = 1:size(M,2)
       if and(abs(M(i,j)) > val, i ~= j)
          val = abs(M(i, j));
          index = [i j];
       end
   end
    
end

end

