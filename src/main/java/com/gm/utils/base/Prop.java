package com.gm.utils.base;

import com.gm.help.base.Quick;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 配置文件工具类.
 * properties
 *
 * @author Jason
 */
public class Prop {

    public static final String suffix = ".properties";

    private static final String jar = ".jar";

    private static Map<File, Properties> props = new HashMap();


    /**
     * Get properties.
     *
     * @param file the file
     * @return the properties
     */
    public static Properties get(File file) {
        Prop.load();
        Iterator<Map.Entry<File, Properties>> it = props.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<File, Properties> next = it.next();
            if (Bool.isEqual(file, next.getKey())) {
                return next.getValue();
            }
        }
        return null;
    }

    /**
     * Get bool.
     *
     * @param key the key
     * @return the bool
     */
    public static boolean is(String key) {
        Prop.load();
        Iterator<Map.Entry<File, Properties>> it = props.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<File, Properties> next = it.next();
            Object obj = Quick.exec(x -> next.getValue().get(key));
            if (obj != null && Boolean.parseBoolean(obj.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    public static Object get(String key) {
        Prop.load();
        Iterator<Map.Entry<File, Properties>> it = props.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<File, Properties> next = it.next();
            Object obj = Quick.exec(x -> next.getValue().get(key));
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Gets property.
     *
     * @param key the key
     * @return the property
     */
    public static String getProperty(String key) {
        Prop.load();
        Iterator<Map.Entry<File, Properties>> it = props.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<File, Properties> next = it.next();
            String obj = Quick.exec(x -> next.getValue().getProperty(key));
            if (!Bool.isNull(obj)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Gets int.
     *
     * @param key the key
     * @return the int
     */
    public static Integer getInt(String key) {
        return Quick.exec(x -> Integer.valueOf(getProperty(key)));
    }

    /**
     * Gets long.
     *
     * @param key the key
     * @return the long
     */
    public static Long getLong(String key) {
        return Quick.exec(x -> Long.valueOf(getProperty(key)));
    }

    /**
     * Put.
     *
     * @param file  the file
     * @param key   the key
     * @param value the value
     */
    public static void put(File file, String key, Object value) {
        if (props.containsKey(file)) {
            Properties properties = props.get(file);
            if (Bool.isNull(properties)) {
                properties = new Properties();
            }
            properties.setProperty(key, value.toString());
        }
    }

    /**
     * Put file.
     *
     * @param key   the key
     * @param value the value
     * @return the file
     */
    public static File put(String key, Object value) {
        final Iterator<Map.Entry<File, Properties>> it = props.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<File, Properties> next = it.next();
            Properties properties = next.getValue();
            if (!Bool.isNull(properties)) {
                properties.setProperty(key, value.toString());
                return next.getKey();
            }
        }
        return ExceptionUtils.cast(String.format("配置{%s}不存在", key));
    }


    /**
     * Store.
     *
     * @param key   the key
     * @param value the value
     */
    public static void store(String key, Object value) {
        store(put(key, value));
    }

    /**
     * Store.
     *
     * @param file the file
     */
    public static void store(File file) {
        if (props.containsKey(file)) {
            Quick.run(x -> {
                FileOutputStream out = new FileOutputStream(file);
                props.get(file).store(out, "");
                out.flush();
                out.close();
            });
        }
    }

    /**
     * Load.
     */
    public static void load() {
        if (props.isEmpty()) {
            String path = Prop.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            if (path.endsWith(jar)) {
                JarFile file = Quick.exec(x -> new JarFile(path));
                for (Enumeration<JarEntry> en = file.entries(); en.hasMoreElements(); ) {
                    JarEntry entry = en.nextElement();
                    String name = entry.getName();
                    if (name.endsWith(suffix)) {
                        load(file, entry, new File(path + File.separator + name));
                    }
                }
            } else {
                File file = new File(path);
                if (file.exists() && file.isDirectory()) {
                    Quick.loop(file, (File r) -> {
                        File[] files = r.listFiles();
                        if (!Bool.isNull(files)) {
                            for (File f : files) {
                                if (f.isFile() && f.exists() && f.getName().endsWith(suffix)) {
                                    load(f);
                                }
                            }
                        }
                        return null;
                    });
                }
            }
        }
    }


    /**
     * 加载jar中的prop.
     *
     * @param jarFile the jar file
     * @param entry   the entry
     * @param file    the file
     * @return the properties
     */
    public static Properties load(JarFile jarFile, JarEntry entry, File file) {
        return Quick.exec(x -> {
            InputStream in = jarFile.getInputStream(entry);
            Properties properties = properties(in);
            props.put(file, properties);
            in.close();
            return properties;
        });
    }

    /**
     * Load prop.
     *
     * @param file the file
     * @return the properties
     */
    public static Properties load(File file) {
        if (file.exists() && file.isFile()) {
            Properties properties = new Properties();
            Quick.run(x -> {
                FileInputStream in = new FileInputStream(file);
                properties.load(in);
                in.close();
            });
            props.put(file, properties);
            return properties;
        }
        return null;
    }

    /**
     * 根据数据流加载配置文件
     *
     * @param in the in
     * @return properties properties
     */
    public static Properties properties(InputStream in) {
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(in, "UTF-8"));
        } catch (IOException e) {
            Logger.error("加载配置出错 ", e);
        }
        return prop;
    }

    /**
     * 根据类相对路径加载配置文件
     *
     * @param filename the filename
     * @return properties properties
     */
    public static Properties properties(String filename) {
        InputStream in = Prop.class.getResourceAsStream(filename);
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(in, "UTF-8"));
        } catch (IOException e) {
            Logger.error("加载配置出错 ", e);
        }
        return prop;
    }


    /**
     * 获取long配置最小值是least.
     *
     * @param key   the key
     * @param least the least
     * @return the least
     */
    public static long getLeast(String key, long least) {
        Long v = getLong(key);
        if (v != null && v > least) {
            return v;
        }
        return least;
    }

    /**
     * 获取int配置, 最小是least.
     *
     * @param key   the key
     * @param least the least
     * @return the least
     */
    public static int getLeast(String key, int least) {
        Integer v = getInt(key);
        if (v != null && v > least) {
            return v;
        }
        return least;
    }
}
