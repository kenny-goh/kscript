KScript is a JVM-based interpreted scripting language based on the lessons from crafting interpreters.

The language currently supports the following features:

- Turing complete (it is more than a calculator! See examples in demo folder)
- Syntax is a mix of Java and Python
- Lambda function ( e.g  | x | x * x )
- List data structure ( e.g [0,1,2,3] )
- Extensible native functions ( You can add 'java' functions using the plugins. See [below](#plugins-for-extending-native-functions) )

If you are interested in developing an intuition on how interpreter works, refer to my [learning notes](Intepreter%20-%20notes.pdf)

## Installation
The project comes with the bundled kscript.jar file inside dist folder so you can run kscript straight away without 
needing to compile anything.

## Usage
```shell
# Run example fibonacci script
java -jar /dist/kscript.jar demo/script_fib.ks

# Run interactive mode
java -jar /dist/kscript.jar
```

## Example kscript program
```shell

// Fibonacci example
fun fib(n) {
  if (n <= 1) return n;
  return fib(n - 2) + fib(n - 1);
}
for (var i = 0; i < 30; i = i + 1) {
  print fib(i);
}

// Lambda example 1
fun loop(fn) {
  for (var i = 0; i < 10; i = i + 1) {
    print fn(i, 5);
  }
}
loop(|x,y| x * y); // This is lambda function

// Lambda example 2
fun loop(fn) {
  for (var i = 0; i < 10; i = i + 1) {
    print fn(i, 5);
  }
}
loop(|x,y| { // This is lambda function with block 
  x * y 
});

// Object oriented example

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

```
For more examples, please refer to demo subdirectory

## Plugins for extending native functions

It is possible to support additional native functions through the plugin architecture in KScript.
At the minimum, you need to provide implementation classes for:

- NativeFunctionPlugin: The code that returns a list of NativeFunctionFactory.
- NativeFunctionFactory: The code that will build the NativeFunction.
- NativeFunction: The code implementing the Callable interface.

The classes need to be packaged as jar file and must be distributed to nativeplugins folder.
Make sure the package name of the plugin is com.gkh.lang.kscript.nativefn.

Please refer to the com.gkh.lang.kscript.nativefn for examples. 


## Todos
- Range (0..10)
- Native Map type { "hello" : 10 }
- File support
- Import system
- Static methods 
- Immutable variable using val
- Optimization
- HTTP support (Nice to have)
- Socket support (Nice to have)
- Code generation to bytecodes (Nice to have)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Please make sure to update tests as appropriate.

## Credits
- Plugin is based on Suvodeep Pyne's idea at  https://github.com/suvodeep-pyne
- The code for the interpreter is mostly based on Bob Nystrom's book, 'Crafting interpreters'. I added my own flavor on top of it such as having lambda functions, lists, map, quantors and so on.

## License
[MIT](https://choosealicense.com/licenses/mit/)# kscript
