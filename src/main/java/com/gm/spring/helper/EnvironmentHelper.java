package com.gm.spring.helper;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.gm.spring.InstanceFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;


/**
 * The type Environment helper.
 *
 * @author Jason
 */
public class EnvironmentHelper {

	private static Environment environment;

    /**
     * Get property string.
     *
     * @param key the key
     * @return the string
     */
    public static String getProperty(String key){
		init();
		return environment == null ? null : environment.getProperty(key);
	}

    /**
     * Init.
     */
    public static void init() {
		if(environment == null){
			synchronized (EnvironmentHelper.class) {
				environment = InstanceFactory.getInstance(Environment.class);
			}
		}
	}

    /**
     * Contains property boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public static boolean containsProperty(String key){
		init();
		return environment == null ? false : environment.containsProperty(key);
	}


    /**
     * Get all properties map.
     *
     * @param prefix the prefix
     * @return the map
     */
    public static Map<String, Object> getAllProperties(String prefix){
		init();
		if(environment == null) {
            return null;
        }
		MutablePropertySources propertySources = ((ConfigurableEnvironment)environment).getPropertySources();
		
		Map<String, Object> properties = new LinkedHashMap<String, Object>();
		for (PropertySource<?> source : propertySources) {
			if(source.getName().startsWith("servlet") || source.getName().startsWith("system")){
				continue;
			}
			if (source instanceof EnumerablePropertySource) {
				for (String name : ((EnumerablePropertySource<?>) source) .getPropertyNames()) {
					boolean match = StringUtils.isEmpty(prefix);
					if(!match){
						match = name.startsWith(prefix);
					}
					if(match){						
						Object value = source.getProperty(name);
						if(value != null){
							properties.put(name, value);
						}
					}
				}
			}
		}
		return Collections.unmodifiableMap(properties);
	}
}
