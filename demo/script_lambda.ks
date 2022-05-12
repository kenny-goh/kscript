/**
 * This is a test
 */

print "lambda without block";
fun loop(fn) {
  for (var i = 0; i < 10; i = i + 1) {
    print fn(i, 5);
  }
}

loop(|x,y| x * y);

print "lambda with block closure test";
fun loop2(fn) {
  for (var i = 0; i < 10; i = i + 1) {
    print fn(i);
  }
}

loop2(|x| {
  return x * x * x;
});
