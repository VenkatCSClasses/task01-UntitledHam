package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BankAccountTest {

    @Test
    void getBalanceTest() throws InsufficientFundsException {
        // Equivalence class of normal balances
        BankAccount positiveBalanceAccount = new BankAccount("a@b.com", 200);
        assertEquals(200, positiveBalanceAccount.getBalance(), 0.001);
        positiveBalanceAccount.withdraw(50);
        assertEquals(150, positiveBalanceAccount.getBalance(), 0.001); 

        // Equivalence class of zero balances
        BankAccount zeroBalanceAccount = new BankAccount("zero@b.com", 0);
        assertEquals(0, zeroBalanceAccount.getBalance(), 0.001); // zero starting balance
        BankAccount drainedAccount = new BankAccount("drain@b.com", 75);
        drainedAccount.withdraw(75); // withdraw everything
        assertEquals(0, drainedAccount.getBalance(), 0.001); // balance reaches zero

        // Equivalence class of max balances
        BankAccount maxBalanceAccount = new BankAccount("max@b.com", Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, maxBalanceAccount.getBalance(), 0.001); // ensure getter handles max double
    }

    @Test
    void withdrawTest() throws InsufficientFundsException{
        // Equivalence class of valid withdrawal amounts
        BankAccount validAccount = new BankAccount("a@b.com", 200);
        validAccount.withdraw(100); // standard withdrawal amount
        assertEquals(100, validAccount.getBalance(), 0.001);

        validAccount.withdraw(0); // withdrawing zero should be allowed and do nothing
        assertEquals(100, validAccount.getBalance(), 0.001);

        validAccount.withdraw(100); // withdrawing the entire remaining balance
        assertEquals(0, validAccount.getBalance(), 0.001);

        // Equivalence class of invalid withdrawals where the amount exceeds the balance
        BankAccount overdraftAccount = new BankAccount("a@b.com", 150);
        assertThrows(InsufficientFundsException.class, () -> overdraftAccount.withdraw(151));
        assertEquals(150, overdraftAccount.getBalance(), 0.001); // balance should remain unchanged

        // Equivalence class of invalid withdrawals where the amount is negative
        BankAccount negativeAmountAccount = new BankAccount("a@b.com", 75);
        assertThrows(IllegalArgumentException.class, () -> negativeAmountAccount.withdraw(-20));
        assertEquals(75, negativeAmountAccount.getBalance(), 0.001); // negative withdrawal should not alter balance
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


        // Allowed characters in prefix equivalence class:
        assertTrue(BankAccount.isEmailValid("abc@mail.com")); // Alpha characters only
        assertTrue(BankAccount.isEmailValid("abc123@mail.com")); // Alphanumeric characters only
        assertTrue(BankAccount.isEmailValid("abc-efg@mail.com")); // Correct usage of special character
        assertFalse(BankAccount.isEmailValid("-abc@mail.com")); // Special character at start (not allowed)
        assertFalse(BankAccount.isEmailValid("abc-@mail.com")); // Special character at end (not allowed)
        assertFalse(BankAccount.isEmailValid("abc--efg@mail.com")); // Two special characters in a row (not allowed)
        assertFalse(BankAccount.isEmailValid("abc#efg@mail.com")); // Use of illegal special character (not allowed)

        // Allowed Characters in domain equivalence class:
        assertTrue(BankAccount.isEmailValid("abc@mail24.com")); // Alphanumeric domain with alpha TLD
        assertTrue(BankAccount.isEmailValid("abc@email-service.com")); // Correct usage of dashes in domain
        assertFalse(BankAccount.isEmailValid("abc@-mail.com")); // Domain that starts with a dash (not allowed)
        assertFalse(BankAccount.isEmailValid("abc@mail-.com")); // Domain that ends with a dash (not allowed)
        assertFalse(BankAccount.isEmailValid("abc@mail#service.com")); // Domain with illegal special characters (not allowed)
        assertFalse(BankAccount.isEmailValid("abe@email")); // No TLD (not allowed)
        assertFalse(BankAccount.isEmailValid("abe@email.")); // Empty TLD (not allowed)
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
