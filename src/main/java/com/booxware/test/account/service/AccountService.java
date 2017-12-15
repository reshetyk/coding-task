package com.booxware.test.account.service;

import com.booxware.test.account.domain.Account;
import com.booxware.test.account.exception.AccountServiceException;
import com.booxware.test.account.repository.PersistenceInterface;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Reshetuyk on 14.12.2017.
 */
public class AccountService implements AccountServiceInterface {

    private final PersistenceInterface repository;

    public AccountService(PersistenceInterface repository) {
        this.repository = repository;
    }

    @Override
    public Account login(String username, String password) {
        Account a = repository.findByName(username);
        if (Objects.equals(a.getEncryptedPassword(), encryptPassword(password))) {
            Account account = new Account(a.getId().get(), a.getUsername(), a.getEncryptedPassword(), a.getSalt(), a.getEmail(), new Date());
            repository.save(account);
            return account;
        }

        throw new AccountServiceException("Cannot login. Password is incorrect");
    }

    private byte[] encryptPassword(String password) {
        return password.getBytes(); //todo implement me
    }

    @Override
    public Account register(String username, String email, String password) {
        Account account = new Account(username, encryptPassword(password), email);
        repository.save(account);
        return repository.findByName(account.getUsername());
    }

    @Override
    public void deleteAccount(String username) {
        repository.delete(repository.findByName(username));
    }

    @Override
    public boolean hasLoggedInSince(Date date, String username) {
        return Objects.equals(date, repository.findByName(username).getLastLogin());
    }
}
