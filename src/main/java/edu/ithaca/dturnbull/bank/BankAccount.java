package edu.ithaca.dturnbull.bank;

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

    /**
     * @post reduces the balance by amount if amount is non-negative and smaller than balance
     */
    public void withdraw (double amount) throws InsufficientFundsException{
        if (amount <= balance){
            balance -= amount;
        }
        else {
            throw new InsufficientFundsException("Not enough money");
        }
    }


    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) return false;

        int at = email.indexOf('@');
        if (at <= 0 || at != email.lastIndexOf('@') || at == email.length() - 1) return false;

        String local = email.substring(0, at);
        String domain = email.substring(at + 1);

        if (local.startsWith(".") || local.endsWith("-") || local.contains("..")) return false;

        for (int i = 0; i < local.length(); i++) {
            char c = local.charAt(i);
            if (!(Character.isLetterOrDigit(c) || c == '.' || c == '-' || c == '_')) return false;
        }

        if (!domain.contains(".") || domain.startsWith(".") || domain.endsWith(".") || domain.contains("..")) return false;

        String[] parts = domain.split("\\.");
        if (parts.length < 2) return false;

        for (String part : parts) {
            if (part.isEmpty() || part.startsWith("-") || part.endsWith("-")) return false;
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