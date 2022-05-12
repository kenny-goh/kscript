// Recursive function to calculate Fibonacci sequence
fun fib(n) {
  if (n <= 1) return n;
  return fib(n - 2) + fib(n - 1);
}
var t1 = clock();
for (var i = 0; i < 30; i = i + 1) {
  print fib(i);
}
var t2 = clock() - t1;
print "Time taken: " + str(t2);