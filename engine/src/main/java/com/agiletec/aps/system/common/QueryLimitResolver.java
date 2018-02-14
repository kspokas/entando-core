package com.agiletec.aps.system.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.sql.DataSource;

public class QueryLimitResolver {

    public static String createLimitBlock(FieldSearchFilter filter, DataSource dataSource) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return createLimitBlock(filter.getOffset(), filter.getLimit(), dataSource);
    }

    public static String createLimitBlock(Integer offset, Integer limit, DataSource dataSource) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String limitBlock = null;
        String driverClassName = extractDriverClassName(dataSource);
        if (driverClassName.equalsIgnoreCase("org.apache.derby.jdbc.EmbeddedDriver")) {
            limitBlock = String.format("OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", new Object[]{offset, limit});
            
        } else  if (driverClassName.equalsIgnoreCase("TODO")) {
            throw new UnsupportedOperationException(driverClassName + " not implemented!");
        }  else {
            throw new UnsupportedOperationException(driverClassName + " not implemented!");
        }
        return limitBlock;
    }
    

    //JBOSS!!!? make it smarter?

    private static String extractDriverClassName(DataSource dataSource) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = dataSource.getClass().getDeclaredMethod("getDriverClassName");
        String driver = (String) method.invoke(dataSource);
        return driver;
    }

}
