package com.github.since1986.demo.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FragmentUtils {

    @Nullable
    public static Fragment reflectionNewInstance(@NonNull String fragmentClassName, Object... args) {
        Class<?>[] parameterTypes;
        if (args == null) {
            parameterTypes = new Class[0];
        } else {
            parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
        }
        try {
            Method method = Class.forName(fragmentClassName).getMethod("newInstance", parameterTypes);
            return (Fragment) method.invoke(null, args);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Fragment addByTag(FragmentActivity fragmentActivity, int containerResourceId, String tag, Bundle arguments) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = reflectionNewInstance(tag);
        fragment.setArguments(arguments);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerResourceId, fragment, tag);
        fragmentTransaction.commit();
        return fragment;
    }

    public static void showByTag(FragmentActivity fragmentActivity, String tag) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag); //通过Tab上的tag查找Fragment实例
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    public static void hideByTag(FragmentActivity fragmentActivity, String tag) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
    }

    public static void hideByTags(FragmentActivity fragmentActivity, String... tags) {
        for (String tag : tags) {
            hideByTag(fragmentActivity, tag);
        }
    }

    public static void showBeforeAddByTag(FragmentActivity fragmentActivity, int containerResourceId, String tag, Bundle arguments) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag); //通过Tab上的tag查找Fragment实例
        if (fragment == null) { //没找到则通过tag反射创建实例(tag写的是Fragment的限定名，用于反射查找)
            addByTag(fragmentActivity, containerResourceId, tag, arguments);
        } else {
            showByTag(fragmentActivity, tag);
        }
    }

    public static void showBeforeAddByTagAndHideOthers(FragmentActivity fragmentActivity, int containerResourceId, String tag, Bundle arguments, String... otherTags) {
        hideByTags(fragmentActivity, otherTags);
        showBeforeAddByTag(fragmentActivity, containerResourceId, tag, arguments);
    }
}
