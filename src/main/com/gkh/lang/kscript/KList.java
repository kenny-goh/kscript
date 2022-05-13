package com.gkh.lang.kscript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KList {
    private final List list = new ArrayList();

    public KList(List<Object> elements) {
        list.addAll(elements);
    }

    public void push(Object element) {
        list.add(element);
    }

    public KList extend(KList another) {
        List mergedList = new ArrayList();
        mergedList.addAll(list);
        mergedList.addAll(another.list);
        return new KList(mergedList);
    }

    public KList copy() {
        List copy = new ArrayList(this.list.size());
        copy.addAll(this.list);
        return new KList(copy);
    }

    @Override public String toString() {
        List<String> strings = (List<String>) list.stream().map(Utils::stringify).collect(Collectors.toList());
        return "[" + String.join(", ",strings) + "]";
    }

    public Object get(int index) {
        return list.get(index);
    }

    public void insert(int index, Object element) {
        list.add(index, element);
    }

    public void remove(int index) {
        list.remove(index);
    }

    public Integer size() {
        return this.list.size();
    }

    public void clear() {
        this.list.clear();
    }

    public Object pop(int index) {
        return this.list.remove(index);
    }

    public KList reverse() {
        List copy = new ArrayList(this.list.size());
        copy.addAll(this.list);
        Collections.reverse(copy);
        return new KList(copy);
    }


    public Callable getFunction(Token name) {
        if (name.lexeme.equals("size")) return new Callable() {
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return size();
            }
            @Override
            public int arity() {return 0;}
        };
        else if (name.lexeme.equals("clear")) return new Callable() {
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                clear();
                return null;
            }
            @Override
            public int arity() {return 0;}
        };
        else if (name.lexeme.equals("push")) return new Callable() {
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                push(arguments.get(0));
                return null;
            }
            @Override
            public int arity() {return 1;}
        };
        else if (name.lexeme.equals("insert")) return new Callable() {
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                Integer index = ((Double)arguments.get(0)).intValue();
                Object element = arguments.get(1);
                insert(index, element);
                return null;
            }
            @Override
            public int arity() {return 2;}
        };
        else if (name.lexeme.equals("remove")) return new Callable() {
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                Integer index = ((Double)arguments.get(0)).intValue();
                remove(index);
                return null;
            }
            @Override
            public int arity() {return 1;}
        };
        else if (name.lexeme.equals("pop")) return new Callable() {
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                Integer index = ((Double)arguments.get(0)).intValue();
                return pop(index);
            }
            @Override
            public int arity() {return 1;}
        };
        else if (name.lexeme.equals("copy")) return new Callable() {
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return copy();
            }
            @Override
            public int arity() {return 0;}
        };
        else if (name.lexeme.equals("reverse")) return new Callable() {
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return reverse();
            }
            @Override
            public int arity() {return 0;}
        };
        else if (name.lexeme.equals("map")) return new Callable() {
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                Lambda lambda = (Lambda) arguments.get(0);
                List<Object> copy = (List<Object>) list.stream().map(it->{
                    List lambdaArgument = new ArrayList();
                    lambdaArgument.add(it);
                    return lambda.call(interpreter, lambdaArgument);
                }).collect(Collectors.toList());
                return new KList(copy);
            }
            @Override
            public int arity() {return 1;}
        };
        return null;
    }
}
