package io.github.alerithe.client.features.plugins;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PluginClassLoader extends URLClassLoader {
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final Map<String, Package> packages = new HashMap<>();

    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;

    public PluginClassLoader(File file, ClassLoader parent) throws IOException {
        super(new URL[]{file.toURI().toURL()}, parent);

        this.jar = new JarFile(file);
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (classes.containsKey(name)) return classes.get(name);

        Class<?> result = null;

        String path = name.replace('.', '/') + ".class";
        JarEntry entry = jar.getJarEntry(path);
        if (entry != null) {
            byte[] classData;
            try (ByteArrayOutputStream output = new ByteArrayOutputStream();
                 InputStream is = jar.getInputStream(entry)) {
                byte[] buffer = new byte[8192]; // 8K buffer
                int read;
                while ((read = is.read(buffer)) != -1) output.write(buffer, 0, read);

                classData = output.toByteArray();
            } catch (IOException e) {
                throw new ClassNotFoundException(name, e);
            }

            int dotIdx = name.lastIndexOf('.');
            if (dotIdx > -1) {
                String pkgName = name.substring(0, dotIdx);
                Package pkg = getPackage(pkgName);
                if (pkg == null) {
                    if (manifest != null) {
                        pkg = definePackage(pkgName, manifest, url);
                    } else {
                        pkg = definePackage(name, null, null, null, null, null, null, null);
                    }
                }

                packages.put(pkgName, pkg);
            }

            CodeSource codeSrc = new CodeSource(url, entry.getCodeSigners());
            result = defineClass(name, classData, 0, classData.length, codeSrc);
            if (result == null) result = super.findClass(name);

            classes.put(name, result);
        }

        return result;
    }

    @Override
    protected Package getPackage(String name) {
        return packages.containsKey(name) ? packages.get(name) : super.getPackage(name);
    }

    @Override
    public URL getResource(String name) {
        return super.findResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return super.findResources(name);
    }

    @Override
    public void close() throws IOException {
        super.close();

        jar.close();
    }
}
