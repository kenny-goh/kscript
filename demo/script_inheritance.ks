class Fruit {
  taste() {
    print "Damn, I taste good!";
  }
}

class Banana extends Fruit {}

class Durian extends Fruit {
  taste() {
     super.taste();
     print "...and I am the king of Fruit!";
  }
}

class Lemon extends Fruit {
  taste() {
    print "Don't eat me, I am sour!";
  }
}

print "Banana:";
Banana().taste();
print "Lemon:";
Lemon().taste();
print "Durian:";
Durian().taste();