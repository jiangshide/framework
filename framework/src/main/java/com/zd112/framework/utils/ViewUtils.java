package com.zd112.framework.utils;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ViewUtils {

    public static void inject(Object object) {
        reflectFindView(object, null);
        reflectMethod(object, null);
    }

    public static void inject(Object object, View view) {
        reflectFindView(object, view);
        injectMethod(object, view);
    }

    private static void reflectFindView(Object object, View view) {
        Class<?> _class = object.getClass();
        Field[] fields = _class.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                reflectFindView(object, view, field, viewInject.value());
            }
        }
    }

    private static void reflectFindView(Object object, View view, Field field, int id) {
        if (id == -1) {
            return;
        }
        Class<?> _class = object.getClass();
        Class<?> findViewClass = (view == null ? object : view).getClass();
        try {
            Method method = (findViewClass == null ? _class : findViewClass).getMethod("findViewById", int.class);
            Object resView = method.invoke(view == null ? object : view, id);
            field.setAccessible(true);
            field.set(object, resView);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void injectMethod(Object object, View view) {
        reflectMethod(object, view);
    }

    private static void reflectMethod(Object object, View view) {
        Class<?> _class = object.getClass();
        Method[] methods = _class.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            OnClick onClick = method.getAnnotation(OnClick.class);
//            LogUtils.e("------------onClick:",onClick);
            if (onClick != null) {
                int[] values = onClick.value();
                int size = values.length;
                for (int index = 0; index < size; index++) {
                    int id = values[index];
                    reflectMethod(object, view, id, index, method);
                }
            }
        }
    }

    private static void reflectMethod(final Object object, View view, int id, int index, final Method method) {
        Class<?> _class = object.getClass();
        try {
            Method findViewMethod = (view == null ? _class : view.getClass()).getMethod("findViewById", int.class);
            final View resView = (View) findViewMethod.invoke(object, id);
            if (resView == null) {
                return;
            }
            resView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        method.invoke(object, resView);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ViewInject {
        int value() default -1;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OnClick {
        int[] value();
    }

}


