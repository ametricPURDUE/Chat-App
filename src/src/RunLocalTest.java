import junit.framework.TestCase;
import org.junit.Test;
import org.junit.After;
import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Before;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.Timeout;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class RunLocalTest {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("All Test Cases Work");
        } else {
            for (Failure failure: result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase {
        private final PrintStream originalOutput = System.out;
        private final InputStream originalSysin = System.in;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayInputStream testIn;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayOutputStream testOut;

        @Before
        public void outputStart() {
            testOut =  new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));
        }

        @After
        public void restoreInputAndOutput() {
            System.setIn(originalSysin);
            System.setOut(originalOutput);
        }

        private String getOutput() {
            return testOut.toString();
        }

        @SuppressWarnings("SameParameterValue")
        private void receiveInput(String str) {
            testIn = new ByteArrayInputStream(str.getBytes());
            System.setIn(testIn);
        }

        @Test(timeout = 1000)
        public void classDeclarationTestOne() { //User declaration test
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = User.class;
            modifiers = clazz.getModifiers();
            superclass = clazz.getSuperclass();
            superinterfaces = clazz.getInterfaces();
            boolean implementsInterface = Arrays.asList(superinterfaces).contains(userTemplate.class);

            Assert.assertTrue("Ensure that `User` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `User` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `User` extends `Object`!", Object.class, superclass);
            Assert.assertTrue("Ensure that `User` implements `userTemplate`!", implementsInterface);
        }

        @Test(timeout = 1000)
        public void classDeclarationTestTwo(){ //ChatDatabase declaration test
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = ChatDatabase.class;
            modifiers = clazz.getModifiers();
            superclass = clazz.getSuperclass();
            superinterfaces = clazz.getInterfaces();
            boolean implementsInterface = Arrays.asList(superinterfaces).contains(ChatDatabaseInterface.class);

            Assert.assertTrue("Ensure that `ChatDatabase` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `ChatDatabase` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `ChatDatabase` extends `Object`!", Object.class, superclass);
            Assert.assertTrue("Ensure that `ChatDatabase` implements `ChatDatabaseInterface`", implementsInterface);
        }

        @Test(timeout = 1000)
        public void testUserThrowsIncorrectInput() {
            try {
                new User("Invalid Input");
            } catch (Exception e) {
                Assert.assertEquals("Ensure that `User` has a constructor that throws IncorrectInput!",
                        IncorrectInput.class, e.getClass());
            }
        }

        @Test(timeout = 1000)
        public void testChatDatabase() throws IncorrectInput{
            ArrayList<User> expectedUsers = new ArrayList<User>();
            ArrayList<String> expectedUsernames = new ArrayList<String>();
            ArrayList<String> expectedPasswords = new ArrayList<String>();

            expectedUsers.add(new User("AJ Metrick,ametric,18"));
            expectedUsers.add(new User("Parshawn Haynes,phaynes18,19"));
            expectedUsers.add(new User("Nolan Shultz,nshultz,17"));

            expectedUsernames.add("ametric");
            expectedUsernames.add("phaynes18");
            expectedUsernames.add("nshultz");

            expectedPasswords.add("Test1");
            expectedPasswords.add("Test2");
            expectedPasswords.add("Test3");

            ChatDatabase testDatabase = new ChatDatabase(expectedUsers, expectedUsernames, expectedPasswords);

            boolean actWriteUsernames = testDatabase.writeUsernames();
            boolean actWritePasswords = ChatDatabase.writePasswords();
            boolean actReadUsernames = testDatabase.readUsernames();
            boolean actReadPasswords = testDatabase.readPasswords();
            boolean actUpdatePassword = ChatDatabase.updatePassword("ametric", "test43");
            boolean actCreateUser = testDatabase.createUser("Edward Ju,eju18,18");
            boolean actRemoveUser = testDatabase.removeUser(new User("AJ Metrick,ametric,18"));
            boolean actModifyUser = testDatabase.modifyUser(new User("Nolan Shultz,nshultz,17"), "Pranav Sangani,18,psangani,test5");

            assertTrue("Ensure that writeUsernames() returns the correct value", actWriteUsernames);
            assertTrue("Ensure that writePasswords() returns the correct value", actWritePasswords);
            assertTrue("Ensure that readUsernames() returns the correct value", actReadUsernames);
            assertTrue("Ensure that readPasswords() returns the correct value", actReadPasswords);
            assertTrue("Ensure that updatePassword() returns the correct value", actUpdatePassword);
            assertTrue("Ensure that createUser() returns the correct value", actCreateUser);
            assertTrue("Ensure that removeUser() returns the correct value", actRemoveUser);
            assertTrue("Ensure that modifyUser() returns the correct value", actModifyUser);
        }
    }
}

