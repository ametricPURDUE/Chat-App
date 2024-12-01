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
            boolean implementsInterface = Arrays.asList(superinterfaces).contains(UserTemplate.class);

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
        public void classDeclarationTestThree(){ //ChatDatabase declaration test
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = ChatClient.class;
            modifiers = clazz.getModifiers();
            superclass = clazz.getSuperclass();
            superinterfaces = clazz.getInterfaces();
            boolean implementsInterface = Arrays.asList(superinterfaces).contains(ClientInterface.class);

            Assert.assertTrue("Ensure that `ChatClient` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `ChatClient` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `ChatClient` extends `Object`!", Object.class, superclass);
            Assert.assertTrue("Ensure that `ChatClient` implements `ClientInterface`", implementsInterface);
        }
        @Test(timeout = 1000)
        public void classDeclarationTestFour(){ //ChatDatabase declaration test
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = MainChatServer.class;
            modifiers = clazz.getModifiers();
            superclass = clazz.getSuperclass();
            superinterfaces = clazz.getInterfaces();
            boolean implementsInterface = Arrays.asList(superinterfaces).contains(MainServerInterface.class);

            Assert.assertTrue("Ensure that `MainChatServer` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `MainChatServer` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `MainChatServer` extends `Object`!", Object.class, superclass);
            Assert.assertTrue("Ensure that `MainChatServer` implements `MainServerInterface`", implementsInterface);
        }
        @Test(timeout = 1000)
        public void classDeclarationTestFive(){ //ChatDatabase declaration test
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = ChatSubServer.class;
            modifiers = clazz.getModifiers();
            superclass = clazz.getSuperclass();
            superinterfaces = clazz.getInterfaces();
            boolean implementsInterface = Arrays.asList(superinterfaces).contains(SubServerInterface.class);

            Assert.assertTrue("Ensure that `ChatSubServer` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `ChatSubServer` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `ChatSubServer` extends `Object`!", Object.class, superclass);
            Assert.assertTrue("Ensure that `ChatSubServer` implements `SubServerInterface`", implementsInterface);
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
            User AJ = new User("AJ Metrick,ametric,18");
            User nolan = new User("Nolan Shultz,nshultz,17");
            User parshawn = new User("Parshawn Haynes,phaynes18,19");
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

            System.out.println(testDatabase.getUsers("ametric").toString());
            boolean actGetUsersOne= testDatabase.getUsers("ametric").equals(AJ);
            boolean actGetUsersTwo = testDatabase.getUsers("nolan") == null;
            boolean actWriteUsernames = testDatabase.writeUsernames();
            boolean actWritePasswords = ChatDatabase.writePasswords();
            boolean actWriteUsers = testDatabase.writeUsers();
            boolean actReadUsernames = testDatabase.readUsernames();
            boolean actReadPasswords = testDatabase.readPasswords();
            boolean actReadUsers = testDatabase.readUsers();
            boolean actUpdatePasswordOne = testDatabase.updatePassword("ametric", "test43");
            boolean actUpdatePasswordTwo = testDatabase.updatePassword("nolan", "test43");
            boolean actCreateUserOne = testDatabase.createUser("Edward Ju,eju18,18,password");
            boolean actCreateUserTwo = false;
            boolean actCreateUserThree = false;
            boolean actCreateUserFour = false;
            try {
                testDatabase.createUser("name,password,username,22");
            } catch (IncorrectInput e) {
                if (e.getMessage().equals("Please input a number for age")) {
                    actCreateUserTwo = true;
                }
            }
            try {
                testDatabase.createUser("username,name,-10,password");
            } catch (IncorrectInput e) {
                if (e.getMessage().equals("Age cannot be below 0")){
                    actCreateUserThree = true;
                }
            }
            try {
                testDatabase.createUser("BAD_DATA_STRING");
            } catch (IncorrectInput e) {
                if (e.getMessage().equals("Input string data formatted incorrectly")){
                    actCreateUserFour = true;
                }
            }
            boolean actRemoveUserOne = testDatabase.removeUser(new User("AJ Metrick,ametric,18"));
            boolean actRemoveUserTwo = testDatabase.removeUser(new User("James Bond,bondj,33"));
            boolean actModifyUserOne = testDatabase.modifyUser(new User("Nolan Shultz,nshultz,17"), "Pranav Sangani,18,test5");
            boolean actModifyUserTwo = testDatabase.modifyUser(new User("Nolan Shultz,nshultz,17"), "Pranav Sangani,18,test5");
            boolean actLoginOne = testDatabase.login("eju18", "password");
            boolean actLoginTwo = testDatabase.login("eju18", "pass");


            assertTrue("Ensure that getUsers() gets the correct User", actGetUsersOne);
            assertTrue("Ensure that getUsers() returns null when no user exists with inputted username", actGetUsersTwo);
            assertTrue("Ensure that writeUsernames() returns the correct value", actWriteUsernames);
            assertTrue("Ensure that writePasswords() returns the correct value", actWritePasswords);
            assertTrue("Ensure that writeUsers() returns the correct value", actWriteUsers);
            assertTrue("Ensure that readUsernames() returns the correct value", actReadUsernames);
            assertTrue("Ensure that readPasswords() returns the correct value", actReadPasswords);
            assertTrue("Ensure that readUsers() returns the correct value", actReadUsers);
            assertTrue("Ensure that updatePassword() correctly updates password", actUpdatePasswordOne);
            assertFalse("Ensure that updatePassword() does not update password for nonexistent user", actUpdatePasswordTwo);
            assertTrue("Ensure that createUser() correctly creates user with good input data", actCreateUserOne);
            assertTrue("Ensure that createUser() only accepts a number for age", actCreateUserTwo);
            assertTrue("Ensure that createUser() does not accept negative age", actCreateUserThree);
            assertTrue("Ensure that createUser() does not accept an incorrectly formated string", actCreateUserFour);
            assertTrue("Ensure that removeUser() correctly removes given User", actRemoveUserOne);
            assertFalse("Ensure that removeUser() doesn't remove a User that doesn't exist in the database", actRemoveUserTwo);
            assertTrue("Ensure that modifyUser() returns the correct value", actModifyUserOne);
            assertFalse("Ensure that modifyUser() doesn't modify a non existing user", actModifyUserTwo);
            assertTrue("Ensure that login() returns the correct value", actLoginOne);
            assertFalse("Ensure that login() doesn't allow login with an incorect password", actLoginTwo);
        }
        @Test(timeout = 1000)
        public void testUser() throws IncorrectInput{
            User nolan = new User("Nolan Shultz", "nolan_shultz", 18);
            User AJ = new User("AJ Metrick", "ajMetrick", 18);
            User parshawn = new User("Parshawn Haynes", "phaynes18", 19);
            User edward = new User("Edward Ju" ,"eju18" ,18);
            ArrayList<User> expectedBlocked = new ArrayList<>();
            ArrayList<User> expectedFriends = new ArrayList<>();
            boolean badData = false;
            try {
                User wrong = new User("PUrduePete,5000");
            } catch (IncorrectInput e) {
                badData = true;
            }
            assertTrue("Ensure that User(String data) constructor throws IncorrectInput when it is fed bad data", badData);
            boolean actUserEquals = AJ.equals(AJ);
            boolean actUserEqualsTwo = AJ.equals(nolan);
            Assert.assertEquals("Ensure that getName() returns the correct value", nolan.getName(), "Nolan Shultz");
            Assert.assertEquals("Ensure that getAge() returns the correct value", nolan.getAge(), 18);
            Assert.assertEquals("Ensure that getUsername() returns the correct value", nolan.getUsername(), "nolan_shultz");
            try {
                User ajTwo = new User("AJ Metrick,ajMetrick,18");
                Assert.assertEquals("Ensure that User(String data) is correctly assigning the name variable", ajTwo.getName(), "AJ Metrick");
                Assert.assertEquals("Ensure that User(String data) is correctly assigning the age variable", ajTwo.getAge(), 18);
                Assert.assertEquals("Ensure that User(String data) is correctly assigning the username variable", ajTwo.getUsername(), "ajMetrick");
            } catch (IncorrectInput e) {
                Assert.fail("Ensure that User is not throwing IncorrectInput when it shouldn't");
            }
            nolan.setName("Edward Ju");
            nolan.setAge(19);
            nolan.setUsername("eju18");
            Assert.assertEquals("Ensure that setName() returns the correct value" , nolan.getName(), "Edward Ju");
            Assert.assertEquals("Ensure that setAge() returns the correct value", nolan.getAge(), 19);
            Assert.assertEquals("Ensure that setUsername() returns the correct value", nolan.getUsername(), "eju18");
            expectedBlocked.add(parshawn);
            expectedBlocked.add(AJ);
            nolan.setBlocked(new ArrayList<User>());
            nolan.blockUser(parshawn);
            nolan.blockUser(AJ);
            boolean blockedCorrect = true;
            try {
                for (int i = 0; i < nolan.getBlocked().size(); i++) {
                    blockedCorrect = expectedBlocked.get(i).toString().equals(nolan.getBlocked().get(i).toString()) && blockedCorrect;
                }
            } catch (IndexOutOfBoundsException e) {
                Assert.fail("Ensure that getBlocked() returns the correct value");
            }
            Assert.assertEquals("Ensure that getBlocked returns the correct values", blockedCorrect, true);
            nolan.unblockUser(AJ);
            expectedBlocked.remove(AJ);
            try {
                for (int i = 0; i < nolan.getBlocked().size(); i++) {
                    blockedCorrect = expectedBlocked.get(i).toString().equals(nolan.getBlocked().get(i).toString()) && blockedCorrect;
                }
            } catch (IndexOutOfBoundsException e) {
                Assert.fail("Ensure that getBlocked() returns the correct value");
            }
            Assert.assertEquals("Ensure that unblockUser acutally unblocks correct user", blockedCorrect, true);
            expectedFriends.add(parshawn);
            expectedFriends.add(AJ);
            nolan.setFriends(new ArrayList<User>());
            nolan.addFriend(parshawn);
            nolan.addFriend(AJ);
            boolean friendsCorrect = true;
            try {
                for (int i = 0; i < nolan.getFriends().size(); i++) {
                    friendsCorrect = expectedFriends.get(i).toString().equals(nolan.getFriends().get(i).toString()) && friendsCorrect;
                }
            } catch (IndexOutOfBoundsException e) {
                Assert.fail("Ensure that getFriends() returns the correct value");
            }
            Assert.assertEquals("Ensure that getFriends() returns the correct values", blockedCorrect, true);
        }
    }
}

