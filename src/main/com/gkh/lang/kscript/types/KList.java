package com.gkh.lang.kscript.types;

import com.gkh.lang.kscript.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class KList {

    public static final String SUB_LIST = "subList";
    public static final String MAP = "map";
    public static final String REVERSE = "reverse";
    public static final String COPY = "copy";
    public static final String POP = "pop";
    public static final String REMOVE = "remove";
    public static final String INSERT = "insert";
    public static final String PUSH = "push";
    public static final String CLEAR = "clear";
    public static final String SIZE = "size";

    final List<Object> list = new ArrayList<>();
    private final Map<String, Callable> cachedFunctions = new HashMap<>();

    public KList(List<Object> elements) {
        list.addAll(elements);
    }

    public void push(Object element) {
        list.add(element);
    }

    public KList extend(KList another) {
        List<Object> mergedList = new ArrayList<>();
        mergedList.addAll(list);
        mergedList.addAll(another.list);
        return new KList(mergedList);
    }

    public KList copy() {
        List<Object> copy = new ArrayList<>(this.list.size());
        copy.addAll(this.list);
        return new KList(copy);
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

    public Double size() {
        return this.list.size() * 1.0;
    }

    public void clear() {
        this.list.clear();
    }

    public Object pop(int index) {
        return this.list.remove(index);
    }

    public KList reverse() {
        List<Object> copy = new ArrayList<>(this.list.size());
        copy.addAll(this.list);
        Collections.reverse(copy);
        return new KList(copy);
    }

    public KList subList(int fromIndex, int toIndex) {
        List<Object> copy = new ArrayList<>();
        List subList = this.list.subList(fromIndex, toIndex);
        copy.addAll(subList);
        return  new KList(copy);
    }

    public Callable getMethod(Token name) {
        Callable fn;
        switch (name.lexeme) {
            case SIZE:
                if (!this.cachedFunctions.containsKey(SIZE)) {
                    fn = new Callable() {
                        @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                            return size();
                        }

                        @Override public int arity() {
                            return 0;
                        }
                    };
                    this.cachedFunctions.put(SIZE, fn);
                }
                return this.cachedFunctions.get(SIZE);
            case CLEAR:
                if (!this.cachedFunctions.containsKey(CLEAR)) {
                    fn = new Callable() {
                        @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                            clear();
                            return null;
                        }
                        @Override public int arity() {
                            return 0;
                        }
                    };
                    this.cachedFunctions.put(CLEAR, fn);
                }
                return this.cachedFunctions.get(CLEAR);
            case PUSH:
                if (!this.cachedFunctions.containsKey(PUSH)) {
                    fn =  new Callable() {
                        @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                            push(arguments.get(0));
                            return null;
                        }

                        @Override public int arity() {
                            return 1;
                        }
                    };
                    this.cachedFunctions.put(PUSH, fn);
                }
                return this.cachedFunctions.get(PUSH);
            case INSERT:
                if (!this.cachedFunctions.containsKey(INSERT)) {
                    fn = new Callable() {
                        @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                            int index = ((Double) arguments.get(0)).intValue();
                            Object element = arguments.get(1);
                            insert(index, element);
                            return null;
                        }

                        @Override public int arity() {
                            return 2;
                        }
                    };
                    this.cachedFunctions.put(INSERT, fn);
                }
                return this.cachedFunctions.get(INSERT);
            case REMOVE:
                if (!this.cachedFunctions.containsKey(REMOVE)) {
                    fn = new Callable() {
                        @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                            int index = ((Double) arguments.get(0)).intValue();
                            remove(index);
                            return null;
                        }

                        @Override public int arity() {
                            return 1;
                        }
                    };
                    this.cachedFunctions.put(REMOVE, fn);
                }
                return this.cachedFunctions.get(REMOVE);
            case POP:
                if (!this.cachedFunctions.containsKey(POP)) {
                    fn = new Callable() {
                        @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                            int index = ((Double) arguments.get(0)).intValue();
                            return pop(index);
                        }

                        @Override public int arity() {
                            return 1;
                        }
                    };
                    this.cachedFunctions.put(POP, fn);
                }
                return this.cachedFunctions.get(POP);
            case COPY:
                if (!this.cachedFunctions.containsKey(COPY)) {
                    fn = new Callable() {
                        @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                            return copy();
                        }

                        @Override public int arity() {
                            return 0;
                        }
                    };
                    this.cachedFunctions.put(COPY, fn);
                }
                return this.cachedFunctions.get(COPY);
            case REVERSE:
                if (!this.cachedFunctions.containsKey(REVERSE)) {
                    fn = new Callable() {
                        @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                            return reverse();
                        }

                        @Override public int arity() {
                            return 0;
                        }
                    };
                    this.cachedFunctions.put(REVERSE, fn);
                }
                return this.cachedFunctions.get(REVERSE);
            case SUB_LIST:
                if (!this.cachedFunctions.containsKey(SUB_LIST)) {
                    fn = new Callable() {
                        @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                            int fromIndex = ((Double) arguments.get(0)).intValue();
                            int toIndex = ((Double) arguments.get(1)).intValue();
                            return subList(fromIndex, toIndex);
                        }

                        @Override public int arity() {
                            return 2;
                        }
                    };
                    this.cachedFunctions.put(SUB_LIST, fn);
                }
                return this.cachedFunctions.get(SUB_LIST);
            case MAP:
                if (!this.cachedFunctions.containsKey(MAP)) {
                    fn =  new Callable() {
                        @Override public Object call(Interpreter interpreter, List<Object> arguments) {
                            Lambda lambda = (Lambda) arguments.get(0);
                            List<Object> copy = list.stream().map(it -> {
                                List<Object> lambdaArgs = new ArrayList<>();
                                lambdaArgs.add(it);
                                return lambda.call(interpreter, lambdaArgs);
                            }).collect(Collectors.toList());
                            return new KList(copy);
                        }

                        @Override public int arity() {
                            return 1;
                        }
                    };
                    this.cachedFunctions.put(MAP, fn);
                }
                return this.cachedFunctions.get(MAP);
        }
        return null;
    }

    <R> R accept(Quantor<R> visitor) {
        return visitor.visitKList(this);
    }

    @Override public String toString() {
        List<String> strings = list.stream().map(Utils::stringify).collect(Collectors.toList());
        return "[" + String.join(", ",strings) + "]";
    }
}
