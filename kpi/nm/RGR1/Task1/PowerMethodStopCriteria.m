function [ res ] = PowerLawStopCriteria(ll, l, indexes, epsilon)
%POWERLAWSTOPCRITERIA Summary of this function goes here
%   Detailed explanation goes here

result = [];

for i = indexes
    result = [ll(i) - l(i) result];
end

result = abs(result) < epsilon;

res = all(result);



end

