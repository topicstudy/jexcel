package com.wjh.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    /**
     * 将map转成实体类的对象
     * 例如将map{name:wjh,gender:男}转成User类的对象
     */
    public static <T> T mapToBean(Map<String, String> map, Class<T> clazz) throws Exception {
        if (map == null) throw new Exception("mapToBean时传入的map为空");
        if (clazz == null) throw new Exception("mapToBean时传入的clazz为空");

        T instance = clazz.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();// 所有属性
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            if (!map.containsKey(fieldName)) continue;
            String fieldValue = map.get(fieldName);
            String fieldType = field.getType().getTypeName();//java.lang.String
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, clazz);

            if (fieldType.equals(String.class.getName())
                    || fieldType.equals("string")) {
                propertyDescriptor.getWriteMethod().invoke(instance, fieldValue);
            } else if (fieldType.equals(Float.class.getName())
                    || fieldType.equals("float")) {
                propertyDescriptor.getWriteMethod().invoke(instance, Float.parseFloat(fieldValue));
            } else if (fieldType.equals(Double.class.getName())
                    || fieldType.equals("double")) {
                propertyDescriptor.getWriteMethod().invoke(instance, Double.parseDouble(fieldValue));
            } else if (fieldType.equals(Boolean.class.getName())
                    || fieldType.equals("boolean")) {
                propertyDescriptor.getWriteMethod().invoke(instance, Boolean.parseBoolean(fieldValue));
            } else if (fieldType.equals(Date.class.getName())) {
                propertyDescriptor.getWriteMethod().invoke(instance, DateUtil.stringToDate(fieldValue));
            }
        }
        return instance;
    }

    /**
     * 将实体类的对象转成Map<String,String>
     */
    public static <T> Map<String, String> beanToMap(T bean) throws Exception {
        if (bean == null) throw new Exception("参数bean不可为空");
        Map<String, String> map = new HashMap();
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();// 所有字段
        for (Field field : fields) {
            String fieldName = field.getName();
            String fieldType = field.getType().getTypeName();
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, clazz);
            Method readMethod = propertyDescriptor.getReadMethod();
            Object fieldValue = readMethod.invoke(bean);
            if (fieldType.equals(Date.class.getName())) {
                map.put(fieldName, DateUtil.dateToString((Date) fieldValue));
            } else {
                if (fieldValue == null) fieldValue = "";
                map.put(fieldName, String.valueOf(fieldValue));
            }
        }
        return map;
    }
}
