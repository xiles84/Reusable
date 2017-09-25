package com.tapette.reusables;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Random;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;


public class MockUtils {

	public static <T> CtClass prepareClass(Class<T> clazz, String strMethod , ArrayList<Class<?>> parameters , String newMethos) throws Exception {
		return prepareClass(clazz.getCanonicalName(), strMethod , parameters , newMethos );
	}

	public static <T> CtClass prepareClass(String clazz, String strMethod , ArrayList<Class<?>> parameters , String newMethos) throws Exception {
		Random rn = new Random();
		ClassPool pool = ClassPool.getDefault();
		CtClass parentClass = pool.get(clazz);
		CtClass childClass = pool.makeClass(
				parentClass.
				getName()+"Mocker_" + rn.nextInt()%1000,
				parentClass);
		childClass.setSuperclass(parentClass);
		childClass.stopPruning(true);
		childClass.defrost();
		CtMethod[] ms = parentClass.getDeclaredMethods();
		for (int i = 0; i < ms.length; i++) {
			CtMethod method = new CtMethod(ms[i].getReturnType(),
					ms[i].getName(),
					ms[i].getParameterTypes(),
					childClass);
			method.setBody(ms[i], null);
			childClass.addMethod(
					method);
		}
		return prepareClass(childClass, strMethod , parameters , newMethos );
	}

	//TODO not implemented yet
	public boolean hasMethod(CtClass cc , String strMethod , ArrayList<Class<?>> parameters , String newMethos) throws NotFoundException, CannotCompileException {
		StringBuilder signature = new StringBuilder();
		signature.append(strMethod).append("(");
		if(parameters != null) {
			for (int i = 0; i < parameters.size() - 1; i++)
				signature.append(parameters.get(i).getCanonicalName()).append(",");
			signature.append(parameters.get(parameters.size()-1).getCanonicalName());
		}
		signature.append(")");
		CtMethod m2 = null;
		CtMethod[] ms = cc.getSuperclass().getDeclaredMethods();
		for (int i = 0; i < ms.length; i++) {
			m2 = new CtMethod(ms[i].getReturnType(), ms[i].getName(), ms[i].getParameterTypes(), cc);
			if(signature.toString().equals(ms[i].getLongName().substring(ms[i].getLongName().lastIndexOf("."+strMethod)+1))) {
				m2.setBody(newMethos);
				cc.addMethod(m2);
			}else {
				cc.addMethod(m2);
			}
		}
		return false;
	}

	public static <T> CtClass prepareClass(CtClass cc , String strMethod, ArrayList<Class<?>> parameters , String newMethos) throws Exception {
		StringBuilder signature = new StringBuilder();
		signature.append(strMethod).append("(");
		if(parameters != null) {
			for (int i = 0; i < parameters.size() - 1; i++) {
				signature.append(parameters.get(i).getCanonicalName()).append(",");
			}
			signature.append(parameters.get(parameters.size()-1).getCanonicalName());
		}
		signature.append(")");
		CtMethod[] ms = cc.getDeclaredMethods();
		for (int i = 0; i < ms.length; i++)
			if(signature.toString().equals(ms[i].getLongName().substring(ms[i].getLongName().lastIndexOf("."+strMethod)+1)))
				ms[i].setBody(newMethos);
		return cc;
	}

	public static <T> Object initiateClass(CtClass cc, Class<?>[] clazzes, Object[] objects) throws Exception {
		cc.stopPruning(false);
		Class<?> gg = cc.toClass();
		Object qq = null;
		boolean verifier = false;
		try {
			if(clazzes != null) {
				for (int i = 0; i < gg.getConstructors().length; i++) {
					verifier = true;
					for (int j = 0; j <gg.getConstructors()[i].getParameters().length; j++) {
						if(gg.getConstructors()[i].getParameters().length ==  clazzes.length)
							if(!gg.getConstructors()[i].getParameters()[j].getType().getCanonicalName().equalsIgnoreCase(clazzes[j].getCanonicalName()))
								verifier = false;
							else
								verifier = false;
					}

					if(verifier)
						return  gg.
								getConstructors()[i].
								newInstance(objects);
				}
			}else
				return gg.
						newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
		return qq;
	}

	public static <T> T assignField(T obj, String strField, Object value) throws Exception {
		Field field = obj.getClass().getDeclaredField(strField);
		field.setAccessible(true);
		field.set(obj, value);
		return obj;
	}

	public static <T> void assignFinalField(Class<T> clazz, String strField, Object newValue) throws Exception {
		Field field = clazz.getDeclaredField(strField);
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.set(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, newValue);
	}

	public static <T> void invokeMethod(T obj, String strMethod) throws Exception {
		Method method = obj.getClass().getDeclaredMethod(strMethod);
		method.setAccessible(true);
		method.invoke(obj);
	}

	public static <T> Object getFieldValue(T obj, String strField) throws Exception {
		Field field = obj.getClass().getDeclaredField(strField);
		field.setAccessible(true);
		return field.get(obj);
	}

}
