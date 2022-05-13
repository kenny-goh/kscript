
/**
 * Search element in A list using binary search algorithm
 */
fun binarySearch(A, element) {
    var low = 0;
    var high = A.size()-1;
    while (low <= high) {
        var ix = floor((low + high) / 2);
        if (element == A[ix]) {
            return true;
        } else if (element < A[ix]) {
            high = ix - 1;
        } else {
            low = ix + 1;
        }
    }
    return false;
}

print "Search found: " + str(binarySearch([100,200,300,400,500,600], 400));
