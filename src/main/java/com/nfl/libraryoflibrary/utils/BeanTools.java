package com.nfl.libraryoflibrary.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuli.niu on 2017/3/21.
 */

public class BeanTools {
    /**
     * 从 from 中将属性复制到 to 中
     *
     * @param to   目标类
     * @param from 被复制的类
     */
    public static void copyProperties(Object to, Object from){
        List commonFields = new ArrayList(); // 用于保存 to 和 from 中共有的属性
        try {
            Class classTo = to.getClass();
            Class classFrom = from.getClass();
            Field[] fieldsTo = classTo.getDeclaredFields();
            Field[] fieldsFrom = classFrom.getDeclaredFields();

            // 如果其中
            if (null == fieldsTo || fieldsTo.length == 0 || null == fieldsFrom || fieldsFrom.length == 0) {
                return;
            }

            // 两个类属性比较剔除不相同的属性，只留下相同的属性
            for (int i = 0; i < fieldsFrom.length; i++) {
                for (int j = 0; j < fieldsTo.length; j++) {
                    if (fieldsTo[j].getName().equals(fieldsFrom[i].getName())) {
                        commonFields.add(fieldsTo[j]);
                        LogTool.i("BeanTools 共同属性：" + fieldsTo[j]) ;
                        break;
                    }
                }
            }
            if (null != commonFields && commonFields.size() > 0) {
                Field field;
                String fieldName;
                String str, getName, setName;
                Method getMethod = null;
                Method setMethod = null;
                for (int i = 0; i < commonFields.size(); i++) {
                    //获取属性名称
                    field = (Field) commonFields.get(i);
                    fieldName = field.getName();
                    // 属性名第一个字母大写
                    str = fieldName.substring(0, 1).toUpperCase();
                    // 拼凑getXXX和setXXX方法名
                    getName = "get" + str + fieldName.substring(1);
                    setName = "set" + str + fieldName.substring(1);
                    // 获取get、set方法
                    getMethod = classFrom.getMethod(getName, new Class[]{});
                    setMethod = classTo.getMethod(setName, new Class[]{field.getType()});
                    //获取属性值
                    Object o = getMethod.invoke(from , new Object[]{});
                    //将属性值放入另一个对象中对应的属性
                    if (null != o) {
                        setMethod.invoke(to , new Object[]{o});
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
        }
    }

}
