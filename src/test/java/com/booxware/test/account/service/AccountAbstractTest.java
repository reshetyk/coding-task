package com.booxware.test.account.service;

import com.booxware.test.account.repository.PersistenceInMemoryRepository;
import org.junit.Before;

import java.security.MessageDigest;

public abstract class AccountAbstractTest {
    protected AccountServiceInterface accountService;
    protected PersistenceInMemoryRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new PersistenceInMemoryRepository();
        accountService = new AccountService(repository, MessageDigest.getInstance("MD5"));
    }
}
