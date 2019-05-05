package com.gm.help.base;


import com.gm.ex.BusinessException;
import com.gm.help.Help;
import com.gm.kit.thread.Fixed;
import com.gm.utils.base.Bool;
import com.gm.utils.base.ExceptionUtils;
import com.gm.utils.base.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 沙箱帮助类
 * 速效代码帮助类.
 * <p>
 * 关于异常捕捉: 只有有返回值的方法会捕捉异常
 *
 * @author Jason
 */
public class Quick implements Help {

    public static final Object NULL = null;

    /**
     * 接口函数.
     *
     * @param <T> the type parameter
     */
    public interface Exec<T> {

        /**
         * 接口函数
         *
         * @param t the t
         * @return t t
         * @throws Throwable the throwable
         */
        T exec(T t) throws Throwable;
    }

    /**
     * 接口函数.
     *
     * @param <T> the type parameter
     */
    public interface Run<T> {

        /**
         * 接口函数
         *
         * @param t 此参数未使用
         * @throws Throwable the throwable
         */
        void run(T... t) throws Throwable;
    }

    /**
     * 接口函数.
     *
     * @param <T> the type parameter
     */
    public interface Echo<T> {

        /**
         * 接口函数
         *
         * @param t the t
         * @throws Throwable the throwable
         */
        void echo(T t) throws Throwable;
    }

    /**
     * 接口函数.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     */
    public interface Maps<K, V> {

        /**
         * 接口函数
         *
         * @param k the k
         * @param v the v
         * @throws Throwable the throwable
         */
        void echo(K k, V v) throws Throwable;
    }

    /**
     * 接口函数.
     */
    public interface Param {

        /**
         * 接口函数
         *
         * @param obj the obj
         * @throws Throwable the throwable
         */
        void param(Object... obj) throws Throwable;
    }

    /**
     * 接口函数.
     */
    public interface Startup {

        /**
         * 接口函数
         *
         * @param obj the obj
         * @throws Throwable the throwable
         */
        void startup(Object... obj) throws Throwable;
    }

    /**
     * 接口函数.
     *
     * @param <T> the type parameter
     */
    public interface Finale<T> {

        /**
         * 接口函数
         *
         * @param t the t
         * @throws Throwable the throwable
         */
        void finale(T t) throws Throwable;
    }

    /**
     * 新线程执行代码块.
     *
     * @param startup 执行代码块/方法/线程
     * @param finales the finales
     * @return 抛出异常返回false, 执行完毕返回true;
     */
    public static boolean startup(Startup startup, Finale... finales) {
        try {
            Fixed.get().execute(() -> {
                try {
                    startup.startup();
                } catch (Throwable throwable) {
                    Logger.trace(throwable);
                }
            });
            return true;
        } catch (Throwable e) {
            Logger.trace(e);
            return false;
        } finally {
            for (Finale finale : finales) {
                new Thread(() -> {
                    try {
                        finale.finale(NULL);
                    } catch (Throwable throwable) {

                    }
                }).start();
            }
        }
    }

    /**
     * 正真的递归.
     *
     * @param <T>     the type parameter
     * @param obj     the obj
     * @param exec    执行代码块/方法/线程
     * @param finales the finales
     */
    public static <T> void loop(Object obj, Exec<T> exec, Finale... finales) {
        async(obj, exec, finales);
    }

    /**
     * 正真的递归(线程池异步).
     *
     * @param <T>     the type parameter
     * @param obj     the obj
     * @param exec    the exec
     * @param finales the finales
     */
    public static synchronized <T> void async(Object obj, Exec<T> exec, Finale... finales) {
        Quick.startup(x -> {
            if (!Bool.isNull(obj)) {
                try {
                    T t = exec.exec((T) obj);
                    if (t instanceof Collection) {
                        for (T o : (Collection<T>) t) {
                            async(o, exec);
                        }
                    } else if (t instanceof Object[]) {
                        for (T o : (T[]) t) {
                            async(o, exec);
                        }
                    } else if (t != NULL) {
                        async(t, exec);
                    }
                } catch (Throwable throwable) {
                    ExceptionUtils.process(throwable);
                } finally {
                    for (Finale finale : finales) {
                        try {
                            finale.finale(NULL);
                        } catch (Throwable throwable) {
                            ExceptionUtils.process(throwable);
                        }
                    }
                }
            }
        });
    }

    /**
     * 死循环.
     *
     * @param <T>     the type parameter
     * @param echo    执行代码块/方法/线程
     * @param finales the finales
     */
    public static <T> void echo(Echo<T> echo, Finale... finales) {
        try {
            while (true) {
                echo.echo(null);
            }
        } catch (BusinessException be) {
        } catch (Throwable throwable) {
            ExceptionUtils.process(throwable);
        } finally {
            for (Finale finale : finales) {
                try {
                    finale.finale(NULL);
                } catch (Throwable throwable) {
                    ExceptionUtils.process(throwable);
                }
            }
        }
    }


    /**
     * 遍历map.
     *
     * @param <K>     the type parameter
     * @param <V>     the type parameter
     * @param map     the map
     * @param maps    the maps
     * @param finales the finales
     */
    public static <K, V> void echo(Map<K, V> map, Maps<K, V> maps, Finale... finales) {
        try {
            Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<K, V> next = it.next();
                maps.echo(next.getKey(), next.getValue());
            }
        } catch (BusinessException be) {
        } catch (Throwable throwable) {
            ExceptionUtils.process(throwable);
        } finally {
            for (Finale finale : finales) {
                try {
                    finale.finale(NULL);
                } catch (Throwable throwable) {
                    ExceptionUtils.process(throwable);
                }
            }
        }
    }


    /**
     * 遍历集合.
     *
     * @param <T>     the type parameter
     * @param cs      the cs
     * @param echo    the echo
     * @param finales the finales
     */
    public static <T> void echo(Collection<T> cs, Echo<T> echo, Finale... finales) {
        try {
            Iterator<T> it = cs.iterator();
            while (it.hasNext()) {
                echo.echo(it.next());
            }
        } catch (BusinessException be) {
        } catch (Throwable throwable) {
            ExceptionUtils.process(throwable);
        } finally {
            for (Finale finale : finales) {
                try {
                    finale.finale(NULL);
                } catch (Throwable throwable) {
                    ExceptionUtils.process(throwable);
                }
            }
        }
    }

    /**
     * 沙箱执行代码.
     *
     * @param <T>     the type parameter
     * @param exec    执行代码块/方法/线程
     * @param finales the finales
     * @return 是否执行完成 object
     */
    public static <T> T exec(Exec<T> exec, Finale... finales) {

        try {
            return exec.exec(null);
        } catch (Throwable t) {
            Logger.trace(t);
            return null;
        } finally {
            for (Finale finale : finales) {
                try {
                    finale.finale(NULL);
                } catch (Throwable throwable) {

                }
            }
        }
    }

    /**
     * 沙箱执行代码(线程安全).
     *
     * @param <T>     the type parameter
     * @param exec    执行代码块/方法/线程
     * @param finales the finales
     * @return 是否执行完成 object
     */
    public static synchronized <T> T sync(Exec<T> exec, Finale... finales) {

        return exec(exec, finales);
    }


    /**
     * 沙箱执行代码（失败后备胎）.
     * <p>
     * 与does不同的是，最后结果以备胎为准
     *
     * @param <T>   the type parameter
     * @param exec  执行代码块/方法/线程
     * @param execs 沙箱失败以此替换(备胎)
     * @return 是否执行完成 object
     */
    public static <T> T exes(Exec<T> exec, Exec... execs) {

        try {
            return exec.exec(null);
        } catch (Throwable t) {
            for (Exec<T> exec1 : execs) {
                try {
                    return exec1.exec(null);
                } catch (Throwable throwable) {

                }
            }
            return null;
        }
    }


    /**
     * 沙箱执行代码（失败后备胎）.
     * <p>
     * 执行出错直接抛出异常
     *
     * @param <T>   the type parameter
     * @param exec  the exec
     * @param execs the execs
     * @return the t
     */
    public static <T> T exx(Exec<T> exec, Exec... execs) {

        try {
            return exec.exec(null);
        } catch (Throwable t) {
            for (Exec<T> exec1 : execs) {
                try {
                    return exec1.exec(null);
                } catch (Throwable throwable) {
                    ExceptionUtils.process();
                }
            }
            return ExceptionUtils.process();
        }
    }

    /**
     * 沙箱执行代码.
     *
     * @param run     执行代码块/方法/线程
     * @param finales the finales
     * @return 有异常返回false, 正常运行返回true
     */
    public static boolean run(Run run, Finale... finales) {

        try {
            run.run();
            return true;
        } catch (Throwable t) {
            Logger.trace(t);
            return false;
        } finally {
            for (Finale finale : finales) {
                try {
                    finale.finale(NULL);
                } catch (Throwable throwable) {

                }
            }
        }
    }

    /**
     * 沙箱执行代码（成功后执行finales）.
     * 并且finales执行出错将抛出异常以保证事务同步.
     *
     * @param run     执行代码块/方法/线程
     * @param finales 成功后执行的代码
     * @return 有异常返回false, 正常运行返回true(不包括finales结果)
     */
    public static boolean does(Run run, Finale... finales) {

        if (run(run)) {
            for (Finale finale : finales) {
                try {
                    finale.finale(NULL);
                } catch (Throwable throwable) {
                    ExceptionUtils.process("沙箱代码运行成功但结尾代码运行出错");
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 沙箱执行代码（失败后执行finales）.
     *
     * @param run     执行代码块/方法/线程
     * @param finales 失败后执行的代码
     * @return 有异常返回false, 所有代码正常运行返回true
     */
    public static boolean doess(Run run, Finale... finales) {

        if (!run(run)) {
            for (Finale finale : finales) {
                try {
                    finale.finale(NULL);
                } catch (Throwable throwable) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
