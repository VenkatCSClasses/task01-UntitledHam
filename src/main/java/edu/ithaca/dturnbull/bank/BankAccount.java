package edu.ithaca.dturnbull.bank;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class BankAccount {

    private String email;
    private double balance;

    /**
     * @throws IllegalArgumentException if email is invalid
     */
    public BankAccount(String email, double startingBalance){
        if (isEmailValid(email)){
            this.email = email;
            this.balance = startingBalance;
        }
        else {
            throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");
        }
    }

    public double getBalance(){
        return balance;
    }

    public String getEmail(){
        return email;
    }

    /***
     * Returns true if amount is positive and has less than 2 decimal places, otherwise returns false. 
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
     * @throws IllegalArgumentException on withdrawing a negative amount
     */
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount < 0) {
            throw new IllegalArgumentException("Withdraw amount cannot be negative");
        }

        if (amount <= balance) {
            balance -= amount;
        } else {
            throw new InsufficientFundsException("Not enough money");
        }
    }


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