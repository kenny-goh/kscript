print [10, 20, 30];
print [10] + [20];
var list = [10, 30, 50, 60];
print list[0];
print list.size();
list.clear();
print list.size();
list.push(10);
list.push(20);
list.push(30);
list.insert(0,1);
print list;
list.remove(1);
print list;
print list.pop(0);
print list.copy();
print list.reverse();
print list.map(|x| x * x);

print [1,2,3,4,5,6,7,8,9,10].subList(0,5);

print max([1,5,100,200,50]);
print min([1,5,100,200,50]);
print sum([1,5,100,200,50]);

