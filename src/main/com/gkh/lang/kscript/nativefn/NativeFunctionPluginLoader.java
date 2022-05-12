package com.gkh.lang.kscript.nativefn;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

public class NativeFunctionPluginLoader {
    private final Map<String, NativeFunctionFactory> pluginsFactoryMap = new HashMap<>();
    private final AtomicBoolean loading = new AtomicBoolean();

    public void loadDefaultPlugins() {
        installPlugin(new DefaultNativePlugin());
    }

    private void installPlugin(NativeFunctionPlugin plugin) {
        System.out.println("Installing " + plugin.getNativeFunctionFactories().size() + " native function plugin" + (plugin.getNativeFunctionFactories().size() > 1 ? "s" : "")  +" from [" + plugin.getClass().getName() + "]");
        for (NativeFunctionFactory each : plugin.getNativeFunctionFactories()) {
            if (pluginsFactoryMap.containsKey(each.getName())) {
                throw new RuntimeException("Native plugin with " + each.getName() + " already exists.");
            }
            pluginsFactoryMap.put(each.getName(), each);
        }
    }

    public Map<String, NativeFunctionFactory> getPluginsFactoryMap() {
        return pluginsFactoryMap;
    }

    public void loadExternalPlugins(final File pluginsDir) {
        if (!pluginsDir.exists() || !pluginsDir.isDirectory()) {
            System.err.println("Skipping Plugin Loading. Plugin dir not found: " + pluginsDir);
            return;
        }
        System.out.println("Loading external native function plugins from " + pluginsDir);

        if (loading.compareAndSet(false, true)) {
            final File[] files = requireNonNull(pluginsDir.listFiles());
            for (File pluginDir : files) {
                if (pluginDir.isDirectory()) {
                    loadPlugin(pluginDir);
                }
            }
        }
    }

    private void loadPlugin(final File pluginDir) {
        final URLClassLoader pluginClassLoader = createPluginClassLoader(pluginDir);
        final ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(pluginClassLoader);
            for (NativeFunctionPlugin plugin : ServiceLoader.load(NativeFunctionPlugin.class, pluginClassLoader)) {
                installPlugin(plugin);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }


    private URLClassLoader createPluginClassLoader(File dir) {
        final URL[] urls = Arrays.stream(Optional.of(dir.listFiles()).orElse(new File[]{}))
                .sorted()
                .map(File::toURI)
                .map(this::toUrl)
                .toArray(URL[]::new);

        return new NativeFunctionPluginClassLoader(urls, getClass().getClassLoader());
    }

    private URL toUrl(final URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
