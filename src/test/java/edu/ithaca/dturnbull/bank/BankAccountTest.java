package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BankAccountTest {

    @Test
    void getBalanceTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals(200, bankAccount.getBalance(), 0.001);
    }

    @Test
    void withdrawTest() throws InsufficientFundsException{
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.withdraw(100);

        assertEquals(100, bankAccount.getBalance(), 0.001);
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));
    }

    @Test
    void isEmailValidTest(){
        // Equivalence class of testing on overall valid email addresses that are expected/normal
        assertTrue(BankAccount.isEmailValid( "a@b.com")); // valid email address
        // Equivalence class of testing on local part that are valid
        assertTrue(BankAccount.isEmailValid("abc-d@mail.com")); // valid email, not border
        assertTrue(BankAccount.isEmailValid("abc.def@mail.com")); //  valid email where local part contains -, not border
        assertTrue(BankAccount.isEmailValid("abc.def@mail.com")); // same as above
        assertTrue(BankAccount.isEmailValid("abc_def@mail.com")); // valid email where local part contains _, not border
        // Equivalence class of testing on domain part that are valid
        assertTrue(BankAccount.isEmailValid("abc.def@mail.cc")); // valid email with a two letter domain, yes border case
        assertTrue(BankAccount.isEmailValid("abc.def@mail-archive.com")); // valid email with - in domain, not border
        assertTrue(BankAccount.isEmailValid("abc.def@mail.org")); // valid email with .org domain, not border
        assertTrue(BankAccount.isEmailValid("abc.def@mail.com")); // valid email with .com domain, not border

        // Equivalence class of testing on overall structure of where the domain, local, and TLD are placed, and the @/.
        assertFalse(BankAccount.isEmailValid("")); // empty string
        
        assertFalse(BankAccount.isEmailValid("test")); // invalid missing @ and domain, yes border
        assertFalse(BankAccount.isEmailValid("test@em@ail.com")); // invalid multiple @ symbols, yes border
        assertFalse(BankAccount.isEmailValid("test.email@com")); // invalid missing domain dot, yes border
        assertFalse(BankAccount.isEmailValid("@.")); // invalid missing local and domain name, yes border
        assertFalse(BankAccount.isEmailValid("@test.com")); // invalid missing local name, yes border
        assertFalse(BankAccount.isEmailValid("test@email.")); // invalid missing .com name, yes border
        assertFalse(BankAccount.isEmailValid("test@.com")); // invalid missing domain name, yes border

        // Equivalence class of testing on local part that are invalid
        assertFalse(BankAccount.isEmailValid("abc-@mail.com")); // local part ends with -, yes border
        assertFalse(BankAccount.isEmailValid("abc..def@mail.com")); // local part has consecutive dots, yes border
        assertFalse(BankAccount.isEmailValid(".abc@mail.com")); // local part starts with ., yes border
        assertFalse(BankAccount.isEmailValid("abc#def@mail.com")); // local part has invalid character #, yes border

        // Equivalence class of testing on domain part that are invalid
        assertFalse(BankAccount.isEmailValid("abc.def@mail.c")); // domain part has only one letter after dot, yes border
        assertFalse(BankAccount.isEmailValid("abc.def@mail#archive.com")); // domain part has invalid character  of #, yes border
        assertFalse(BankAccount.isEmailValid("abc.def@mail")); // domain part missing dot, yes border
        assertFalse(BankAccount.isEmailValid("abc.def@mail..com")); // domain part has consecutive dots, yes border
        
    }

    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);
        //check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("", 100));
    }

}