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
    void isAmountValidTest() {
        // Positve amounts equivalence class:
        assertTrue(BankAccount.isAmountValid(100)); // Positive whole number amount (border)
        assertTrue(BankAccount.isAmountValid(50.5)); // Positive 1 decimal place amount
        assertTrue(BankAccount.isAmountValid(50.55)); // Positive 2 decimal places amount (border)
        assertFalse(BankAccount.isAmountValid(50.505)); // Positive 3 decimal places amount (not allowed)

        // Negative amounts equivalence class:
        assertFalse(BankAccount.isAmountValid(-100)); // Negative whole number amount (not allowed) (border)
        assertFalse(BankAccount.isAmountValid(-10.1)); // Negative 2 decimal places amount (not allowed) (border)
        assertFalse(BankAccount.isAmountValid(-10.11)); // Negative 2 decimal places amount (not allowed) (border)
        assertFalse(BankAccount.isAmountValid(-10.111)); // Negative 3 decimal places amount (not allowed)

        // Zero and near-zero amounts equivalence class:
        assertFalse(BankAccount.isAmountValid(0)); // Zero is not positive (border)
        assertTrue(BankAccount.isAmountValid(0.01)); // Smallest typical cent amount (border)

        // Large but finite positive amount equivalence class:
        assertTrue(BankAccount.isAmountValid(999999999.99)); // High magnitude with 2 decimals

        // Boundry amounts equivalence class:
        assertTrue(BankAccount.isAmountValid(Double.MAX_VALUE)); // The largest positve value
        assertFalse(BankAccount.isAmountValid(Double.MIN_VALUE)); // The smallest possible decimal (not allowed)
        assertFalse(BankAccount.isAmountValid(Double.NEGATIVE_INFINITY)); // The minimum negative value (not allowed)

        // Non-finite and undefined values equivalence class:
        assertFalse(BankAccount.isAmountValid(Double.POSITIVE_INFINITY)); // Infinite positive value (not allowed)
        assertFalse(BankAccount.isAmountValid(Double.NaN)); // NAN should be rejected

    }

    @Test
    void constructorTest() {
        // Whole number amount equivalence class:
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);


        // Decimal amounts equivalence class:
        bankAccount = new BankAccount("a@b.com", 200.1); // 1 Decimal Places
        assertEquals(200.1, bankAccount.getBalance(), 0.001); 
        bankAccount = new BankAccount("a@b.com", 200.11); // 2 Decimals Places
        assertEquals(200.11, bankAccount.getBalance(), 0.001);
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("a@b.com", 200.111)); // 3 Decimal Places (not allowed)

        assertThrows(IllegalArgumentException.class, () -> new BankAccount("", 100)); // Empty email should error

        // Negative amounts equivalence class:
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("a@b.com", -100)); // Negative whole number (not allowed)
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("a@b.com", -100.5)); // Negative 1 decimal place (not allowed)
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("a@b.com", -100.55)); // Negative 2 decimal place (not allowed)
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("a@b.com", -100.555)); // Negative 3 decimal place (not allowed)
    }

}