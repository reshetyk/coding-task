package com.booxware.test.account.service;

import com.booxware.test.account.domain.Account;
import com.booxware.test.account.exception.AccountServiceException;
import com.booxware.test.account.repository.PersistenceInterface;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Objects;

public class AccountService implements AccountServiceInterface {

    private final PersistenceInterface repository;
    private final MessageDigest messageDigest;

    public AccountService(PersistenceInterface repository, MessageDigest messageDigest) {
        this.repository = repository;
        this.messageDigest = messageDigest;
    }

    @Override
    public Account login(String username, String password) {
        try {
            Account a = findAccountBy(username);
            if (Objects.deepEquals(a.getEncryptedPassword(), encryptPassword(password))) {
                Account account = new Account(a.getId().get(), a.getUsername(), a.getEncryptedPassword(), a.getSalt(), a.getEmail(), new Date());
                repository.save(account);
                return account;
            }
        } catch (Exception ex) {
            throw new AccountServiceException("Error is occurred during login: " + ex.getMessage(), ex);
        }

        throw new AccountServiceException("Cannot login. Password is incorrect");
    }

    @Override
    public Account register(String username, String email, String password) {
        try {
            Account account = new Account(username, encryptPassword(password), email);

            if (repository.findByName(account.getUsername()).isPresent())
                throw new RuntimeException("User with name [" + username + "] already exists");

            return repository.save(account);

        } catch (Exception ex) {
            throw new AccountServiceException("Error is occurred during registration: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteAccount(String username) {
        try {
            repository.delete(findAccountBy(username));
        } catch (Exception ex) {
            throw new AccountServiceException("Error is occurred during account deleting: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean hasLoggedInSince(Date date, String username) {
        try {
            return Objects.equals(date, findAccountBy(username).getLastLogin());
        } catch (Exception ex) {
            throw new AccountServiceException("Error is occurred during checking has user logged in since ["
                    + date.toString() + "]:" + ex.getMessage(), ex);
        }
    }

    private Account findAccountBy(String username) {
        return repository.findByName(username).orElseThrow(() ->
                new AccountServiceException("Not found user with name [" + username + "]"));
    }

    private byte[] encryptPassword(String password) {
        return messageDigest.digest(password.getBytes());
    }
}
