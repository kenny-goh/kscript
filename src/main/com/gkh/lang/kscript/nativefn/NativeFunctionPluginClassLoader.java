package com.gkh.lang.kscript.nativefn;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

/**
 * Loads a plugin leveraging a {@link URLClassLoader}. However, it restricts the plugin from
 * using the system classloader thereby trimming access to all system classes.
 * Only the classes in SHARED_PACKAGES are visible to the plugin.
 */
public class NativeFunctionPluginClassLoader extends URLClassLoader {

    public static final List<String> SHARED_PACKAGES = Arrays.asList(
            "com.gkh.lang.kscript.nativefunction"
    );

    private final ClassLoader parentClassLoader;

    public NativeFunctionPluginClassLoader(URL[] urls, ClassLoader parentClassLoader) {
        super(urls, null);
        this.parentClassLoader = parentClassLoader;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass == null) {
            final boolean isSharedClass = SHARED_PACKAGES.stream().anyMatch(name::startsWith);
            if (isSharedClass) {
                loadedClass = parentClassLoader.loadClass(name);
            } else {
                loadedClass = super.loadClass(name, resolve);
            }
        }

        if (resolve) {
            resolveClass(loadedClass);
        }
        return loadedClass;
    }
}