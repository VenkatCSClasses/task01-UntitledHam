package edu.ithaca.dturnbull.bank;

import java.math.BigDecimal;

public class BankAccount {

    private String email;
    private double balance;

    /**
     * 
     * @param email the email address for the account
     * @param startingBalance the starting balance (must be postive and have less than or equal to 2 decimal places)
     * @throws IllegalArgumentException if the email is invalid or if the starting balance is invalid (negative and/or 3 or more decimal places)
     */
    public BankAccount(String email, double startingBalance) throws IllegalArgumentException {
        if (!isEmailValid(email)){
            throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");

        }
        if (startingBalance != 0 && !isAmountValid(startingBalance)) {
            throw new IllegalArgumentException("Starting balance: " + startingBalance + " is invalid, cannot create account");
        }

        this.email = email;
        this.balance = startingBalance;
    }

    public double getBalance(){
        return balance;
    }

    public String getEmail(){
        return email;
    }

    /**
     * Deposits the given amount into this bankaccount
     * @param amount the amount of money to deposit into the account
     * @throws IllegalArgumentException if the amount is less than or equal to 0 and/or contains more than 2 decimal places.
     */
    public void deposit(double amount) throws IllegalArgumentException {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Amount: " + amount + " is invalid, cannot deposit.");
        }

        this.balance += amount;
    }

    /***
     * Transfers the given amount from this account to the provided other account
     * @param amount the amount to tranfer to the other account
     * @param otherAccount the account to transfer the money to
     * @throws IllegalArgumentException if the amount is less than or equal to 0 and/or contains more than 2 decimal places.
     * @throws InsufficientFundsException if there is not enough money in the account to transfer
     */
    public void transfer(double amount, BankAccount otherAccount) throws IllegalArgumentException, InsufficientFundsException {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Amount: " + amount + " is invalid, cannot transfer.");
        }

        if (balance - amount >= 0) {
            balance -= amount;
            otherAccount.deposit(amount);
        }
        else {
            throw new InsufficientFundsException("Not enough money in account.");
        }
        
    }


    /***
     * Returns true if amount is positive and has less than 2 decimal places and is a finite number, otherwise returns false. 
     * @param amount the amount of money to test
     * @return a boolean indicating if the amount is valid or not
     */
    public static boolean isAmountValid(double amount) {
        return Double.isFinite(amount) && amount > 0 && BigDecimal.valueOf(amount).scale() <= 2;
    }

    /***
     * Withdraws the given amount from the bank account.
     * @param amount the amount of money to withdraw
     * @throws InsufficientFundsException on withdrawing more than the bank account contains
     * @throws IllegalArgumentException on withdrawing a negative amount, or an amount with more than 2 decimal places
     */
    public void withdraw(double amount) throws InsufficientFundsException {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Invalid withdraw amount: " + amount);
        }

        if (amount <= balance) {
            balance -= amount;
        } else {
            throw new InsufficientFundsException("Not enough money");
        }
    }


    /**
     * Returns whether the email is valid or not based on the RFC 5322 spec
     * @param email the email address to test if valid
     * @return a boolean indicating if the email is valid or not
     */
    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) return false;

        int at = email.indexOf('@');
        if (at <= 0) return false;
        if (at != email.lastIndexOf('@')) return false;
        if (at == email.length() - 1) return false;

        String local = email.substring(0, at);
        String domain = email.substring(at + 1);

        char first = local.charAt(0);
        char last = local.charAt(local.length() - 1);

        if (first == '.' || first == '-' || first == '_') return false;
        if (last == '.' || last == '-' || last == '_') return false;

        boolean prevWasSpecial = false;
        for (int i = 0; i < local.length(); i++) {
            char c = local.charAt(i);

            if (!(Character.isLetterOrDigit(c) || c == '.' || c == '-' || c == '_')) return false;

            boolean isSpecial = (c == '.' || c == '-' || c == '_');
            if (isSpecial && prevWasSpecial) return false;
            prevWasSpecial = isSpecial;
        }

        if (!domain.contains(".")) return false;
        if (domain.startsWith(".") || domain.endsWith(".")) return false;
        if (domain.contains("..")) return false;

        String[] parts = domain.split("\\.");
        if (parts.length < 2) return false;

        for (String part : parts) {
            if (part.isEmpty()) return false;
            if (part.startsWith("-") || part.endsWith("-")) return false;

            for (int i = 0; i < part.length(); i++) {
                char c = part.charAt(i);
                if (!(Character.isLetterOrDigit(c) || c == '-')) return false;
            }
        }

        String tld = parts[parts.length - 1];
        if (tld.length() < 2) return false;
        for (int i = 0; i < tld.length(); i++) {
            if (!Character.isLetter(tld.charAt(i))) return false;
        }

        return true;
    }



}