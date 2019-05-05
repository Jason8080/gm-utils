package com.gm.spring;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * The type Spring instance provider.
 *
 * @author Jason
 */
public class SpringInstanceProvider {

	private ApplicationContext applicationContext;

    /**
     * 以一批spring配置文件的路径初始化spring实例提供者。
     *
     * @param locations spring配置文件的路径的集合。spring将从类路径开始获取这批资源文件。
     */
    public SpringInstanceProvider(String... locations) {
		applicationContext = new ClassPathXmlApplicationContext(locations);
	}

    /**
     * 从ApplicationContext生成SpringProvider
     *
     * @param applicationContext the application context
     */
    public SpringInstanceProvider(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

    /**
     * 根据一批Spring配置文件初始化spring实例提供者。
     *
     * @param annotatedClasses the annotated classes
     */
    public SpringInstanceProvider(Class<?>... annotatedClasses) {
		applicationContext = new AnnotationConfigApplicationContext(
				annotatedClasses);
	}

    /**
     * 返回指定类型的实例。
     *
     * @param <T>       the type parameter
     * @param beanClass 实例的类型
     * @return 指定类型的实例 。
     */
    @SuppressWarnings("unchecked")
	public <T> T getInstance(Class<T> beanClass) {
		String[] beanNames = applicationContext.getBeanNamesForType(beanClass);
		if (beanNames.length == 0) {
			return null;
		}
		return (T) applicationContext.getBean(beanNames[0]);
	}

    /**
     * Gets instance.
     *
     * @param <T>       the type parameter
     * @param beanClass the bean class
     * @param beanName  the bean name
     * @return the instance
     */
    public <T> T getInstance(Class<T> beanClass, String beanName) {
		return (T) applicationContext.getBean(beanName, beanClass);
	}

    /**
     * Gets by bean name.
     *
     * @param <T>      the type parameter
     * @param beanName the bean name
     * @return the by bean name
     */
    @SuppressWarnings("unchecked")
	public <T> T getByBeanName(String beanName) {
		return (T) applicationContext.getBean(beanName);
	}

    /**
     * Gets instance.
     *
     * @param <T>      the type parameter
     * @param beanName the bean name
     * @return the instance
     */
    @SuppressWarnings("unchecked")
	public <T> T getInstance(String beanName) {
		return (T) applicationContext.getBean(beanName);
	}

    /**
     * Gets interface count.
     *
     * @param <T>       the type parameter
     * @param beanClass the bean class
     * @return the interface count
     */
    public <T> int getInterfaceCount(Class<T> beanClass) {
		return applicationContext.getBeanNamesForType(beanClass).length;
	}

    /**
     * Gets interfaces.
     *
     * @param <T>       the type parameter
     * @param beanClass the bean class
     * @return the interfaces
     */
    public <T> Map<String, T> getInterfaces(Class<T> beanClass) {
		return applicationContext.getBeansOfType(beanClass);
	}

    /**
     * Gets application context.
     *
     * @return the application context
     */
    public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	
}
