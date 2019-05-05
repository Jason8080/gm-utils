package com.gm.utils.base;

import com.gm.help.base.Quick;
import com.gm.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The type Class utils.
 *
 * @author Jason
 */
public class ClassUtils implements Utils {

    private static final String jar = ".jar";


    /**
     * 获取对象泛型类型.
     *
     * @param o the o
     * @return the class
     */
    public static Class getGenericInterfaces(Object o) {

        Type type = o.getClass().getGenericInterfaces()[0];
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return null;
    }


    /**
     * 获取同一路径下所有子类或接口实现类
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the sub classes
     */
    public static <T> List<Class<T>> getSubClasses(Class<T> clazz) {

        List<Class<T>> classes = new ArrayList();
        for (Class<T> c : getClasses(clazz)) {
            if (clazz.isAssignableFrom(c) && !clazz.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * 获取class的实现类.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the bean classes
     */
    public static <T> List<Class<T>> getBeans(Class<T> clazz) {

        List<Class<T>> classes = new ArrayList();
        for (Class<T> c : getClasses(clazz)) {
            if (clazz.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers()) && !clazz.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * 取得当前类路径下的所有类
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return list list
     */
    public static <T> List<Class<T>> getClasses(Class<T> clazz) {

        String pk = clazz.getPackage().getName();
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (path.contains(jar)) {
            try {
                return getClasses(new JarFile(path), pk);
            } catch (IOException e) {
                return ExceptionUtils.cast(e);
            }
        }
        String pack = pk.replace('.', File.separatorChar);
        String absolute = path + (path.endsWith(File.separator) ? "" : File.separator) + pack;
        return getClasses(new File(absolute), pk);
    }

    /**
     * 迭代查找类
     *
     * @param dir
     * @param pk
     * @return
     */
    private static <T> List<Class<T>> getClasses(File dir, String pk) {

        List<Class<T>> classes = new ArrayList();
        pk = Bool.isNull(pk) ? pk : pk + ".";
        if (!dir.exists()) {
            return classes;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, pk + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                try {
                    Class<T> clazz = (Class<T>) Class.forName(pk + name.substring(0, name.length() - 6));
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    return ExceptionUtils.cast(e);
                }
            }
        }
        return classes;
    }

    /**
     * 迭代查找类
     *
     * @param jar
     * @param pk
     * @return
     */
    private static <T> List<Class<T>> getClasses(JarFile jar, String pk) {

        List<Class<T>> classes = new ArrayList();
        for (Enumeration<JarEntry> en = jar.entries(); en.hasMoreElements(); ) {
            JarEntry entry = en.nextElement();
            String name = entry.getName();
            name = name.replace('/', '.');
            name = name.replace(File.separatorChar, '.');
            if (name.contains(pk) && name.endsWith(".class")) {
                try {
                    Class<T> clazz = (Class<T>) Class.forName(name.substring(0, name.lastIndexOf(".class")));
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    return ExceptionUtils.cast(e);
                }
            }
        }
        return classes;
    }


    /**
     * 获取指定包下所有class.
     *
     * @param packages 多个包名
     * @return 所有class集合 classes
     */
    public static Set<Class<?>> getClasses(String... packages) {
        // Class存放处
        Set<Class<?>> classes = new HashSet();
        if (Bool.isNull(packages)) {
            classes.addAll(getClassesByPack(""));
        } else {
            for (String pack : packages) {
                classes.addAll(getClassesByPack(pack));
            }
        }
        return classes;
    }

    /**
     * 根据包名获取所有class集合
     *
     * @param packages
     * @return
     */
    private static Set<Class<?>> getClassesByPack(String packages) {

        Set<Class<?>> classes = new HashSet();
        Quick.run(x -> {
            // 定义一个枚举的集合 并进行循环来处理这个目录下的things
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(getDirname(packages));
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String path = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    classes.addAll(getClassesByPath(packages, path));
                } else if ("jar".equals(protocol)) {
                    classes.addAll(getClassesByJar(packages, ((JarURLConnection) url.openConnection()).getJarFile()));
                }
            }
        });
        return classes;
    }

    private static String getDirname(String packages) {

        return packages.replace('.', '/');
    }

    /**
     * 以Jar的形式来获取包下的所有Class
     *
     * @param packages 包名
     * @param jar      文件
     */
    private static Set<Class<?>> getClassesByJar(final String packages, JarFile jar) {

        Set<Class<?>> classes = new HashSet();
        // 如果是jar包文件
        String jarPackage = packages;
        // 从此jar包 得到一个枚举类
        Enumeration<JarEntry> entries = jar.entries();
        // 同样的进行循环迭代
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            // 如果是以/开头的
            if (name.charAt(0) == '/') {
                // 获取后面的字符串
                name = name.substring(1);
            }
            // 如果前半部分和定义的包名相同
            if (name.startsWith(getDirname(packages))) {
                int idx = name.lastIndexOf('/');
                // 如果以"/"结尾 是一个包
                if (idx != -1) {
                    // 获取包名 把"/"替换成"."
                    jarPackage = name.substring(0, idx).replace('/', '.');
                }
                // 如果可以迭代下去(是否递归) 并且是一个包
                if (idx != -1) {
                    // 如果是一个.class文件 而且不是目录
                    if (name.endsWith(".class") && !entry.isDirectory()) {
                        // 去掉后面的".class" 获取真正的类名
                        String className = name.substring(jarPackage.length() + 1, name.length() - 6);
                        try {
                            // 添加到classes
                            String prefix = Bool.isNull(jarPackage) ? "" : jarPackage + '.';
                            classes.add(Thread.currentThread().getContextClassLoader().loadClass(prefix + className));
                        } catch (Throwable throwable) {
                        }
                    }
                }
            }
        }
        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packages 包名
     * @param path     文件路径
     */
    private static Set<Class<?>> getClassesByPath(final String packages, String path) {

        Set<Class<?>> classes = new HashSet();
        // 获取此包的目录 建立一个File
        File dir = new File(path);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return classes;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] files = dir.listFiles((File file) -> {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件){可以在此控制是否递归}
            return (file.isDirectory() || (file.getName().endsWith(".class")));
        });
        // 循环所有文件
        for (File file : files) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                String prefix = Bool.isNull(packages) ? "" : packages + ".";
                classes.addAll(getClassesByPath(prefix + file.getName(), file.getAbsolutePath()));
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去 classes.add(Class.forName(packages + '.' + className)); 这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packages + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

}
