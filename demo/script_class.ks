// class Foo {
//     init(name) {
//         this.name = name;
//     }
//     bar() {
//         print "bar";
//     }
// }
// var f = Foo("Foo 1");
// f.bar();
// print f.name;
//


class LinkedNode {
   init(value, linkedNode) {
     this.value = value;
     this.next = linkedNode;
   }
}

class LinkedList {
  init() {
    this.head = nil;
    this.current = nil;
    this.length = 0;
  }
  add(value) {
    var node = LinkedNode(value, nil);
    if (this.head == nil) {
      this.head = node;
      this.head.next = node;
    }
    else if (this.current != nil) {
        this.current.next = node;
    }
    this.current = node;
    this.length = this.length + 1;
  }
  iterator(fn) {
      var curr = list.head;
      while (curr != nil) {
          fn(curr.value);
          curr = curr.next;
      }
  }
}

var list = LinkedList();
list.add(5);
list.add(6);
list.add(10);
list.add(11);
list.add(12);
list.iterator(|x| {
  print x;
});

print "Length: " + str( list.length );

