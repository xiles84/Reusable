package com.tapette.reusables;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import javassist.CtClass;

public class MockUtilsTest {

	public class TestClass {

		String str = null;
		int integer = 0;
		boolean globalFalseBol = false;
		boolean globalTrueBol = true;

		public TestClass() {}

		public TestClass(String str) {
			this.str = str;
		}

		public TestClass(int integer) {
			this.integer = integer;
		}

		public TestClass(String str , int integer) {
			this.str = str;
			this.integer = integer;
		}

		public boolean publicMethod() {
			return privateMethod() && protectedMethod() && justToFill();
		}

		private boolean privateMethod() {
			return false;
		}
		
		private boolean privateMethod(boolean var) {
			return var;
		}

		protected boolean protectedMethod() {
			return false;
		}

		public boolean justToFill() {
			return true;
		}

	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void myCoolTest() throws Exception {
		CtClass aa = MockUtils.prepareClass("com.tapette.reusables.MockUtilsTest$TestClass", "privateMethod" , null , "{ return true; }");
		MockUtils.prepareClass(aa, "protectedMethod" , null , "{ return true; }");
		MockUtilsTest.TestClass bb = (MockUtilsTest.TestClass)MockUtils.initiateClass(aa , new Class<?>[] {MockUtilsTest.class} , new Object[] { new MockUtilsTest()});
		if(!bb.publicMethod())
			fail("myCoolTest failed");
	}
	
	@Test
	public void myCoolTest2() throws Exception {
		CtClass aa = MockUtils.prepareClass("com.tapette.reusables.MockUtilsTest$TestClass", "privateMethod" , null , "{ return true; }");
		MockUtils.prepareClass(aa, "protectedMethod" , null , "{ return true; }");
		MockUtilsTest.TestClass bb = (MockUtilsTest.TestClass)MockUtils.initiateClass(aa , new Class<?>[] { MockUtilsTest.class , String.class } , new Object[] { new MockUtilsTest() , "" });
		if(!bb.publicMethod())
			fail("myCoolTest failed");
	}
	
	@Test
	public void myCoolTest3() throws Exception {
		CtClass aa = MockUtils.prepareClass("com.tapette.reusables.MockUtilsTest$TestClass", "publicMethod" , null , "{ return true; }");
		MockUtilsTest.TestClass bb = (MockUtilsTest.TestClass)MockUtils.initiateClass(aa , new Class<?>[] { MockUtilsTest.class } , new Object[] { new MockUtilsTest() });
		if(!bb.publicMethod())
			fail("myCoolTest failed");
	}
	
	@Test
	public void myCoolTest4() throws Exception {
		CtClass aa = MockUtils.prepareClass("com.tapette.reusables.MockUtilsTest$TestClass", "publicMethod" , null , "{ return privateMethod(true); }");
		MockUtilsTest.TestClass bb = (MockUtilsTest.TestClass)MockUtils.initiateClass(aa , new Class<?>[] { MockUtilsTest.class } , new Object[] { new MockUtilsTest() });
		if(!bb.publicMethod())
			fail("myCoolTest failed");
	}
	
	@Test
	public void myCoolTest5() throws Exception {
		CtClass aa1 = MockUtils.prepareClass("com.tapette.reusables.MockUtilsTest$TestClass", "publicMethod" , null , "{ return globalFalseBol; }");
		MockUtilsTest.TestClass bb1 = (MockUtilsTest.TestClass)MockUtils.initiateClass(aa1 , new Class<?>[] { MockUtilsTest.class } , new Object[] { new MockUtilsTest() });
		if(bb1.publicMethod())
			fail("myCoolTest failed");
		CtClass aa2 = MockUtils.prepareClass("com.tapette.reusables.MockUtilsTest$TestClass", "publicMethod" , null , "{ return globalTrueBol; }");
		MockUtilsTest.TestClass bb2 = (MockUtilsTest.TestClass)MockUtils.initiateClass(aa2 , new Class<?>[] { MockUtilsTest.class } , new Object[] { new MockUtilsTest() });
		if(!bb2.publicMethod())
			fail("myCoolTest failed");
	}
	
	@Test
	public void myCoolTest6() throws Exception {
		ArrayList<Class<?>> param =  new ArrayList<>();
		param.add(boolean.class);
		CtClass aa = MockUtils.prepareClass("com.tapette.reusables.MockUtilsTest$TestClass", "publicMethod" , null , "{ return privateMethod(false); }");
		MockUtils.prepareClass(aa, "privateMethod" , param , "{ return $1 ? false : true ; }");
		MockUtilsTest.TestClass bb = (MockUtilsTest.TestClass)MockUtils.initiateClass(aa , new Class<?>[] { MockUtilsTest.class } , new Object[] { new MockUtilsTest() });
		if(!bb.publicMethod())
			fail("myCoolTest failed");
	}
	
	@Test
	public void myCoolTest7() throws Exception {
		ArrayList<Class<?>> param =  new ArrayList<>();
		param.add(boolean.class);
		CtClass aa = MockUtils.prepareClass("com.tapette.reusables.MockUtilsTest$TestClass", "publicMethod" , null , "{ return privateMethod(false); }");
		MockUtils.prepareClass(aa, "privateMethod" , param , "{ return !$1; }");
		MockUtilsTest.TestClass bb = (MockUtilsTest.TestClass)MockUtils.initiateClass(aa , new Class<?>[] { MockUtilsTest.class } , new Object[] { new MockUtilsTest() });
		if(!bb.publicMethod())
			fail("myCoolTest failed");
	}

}
