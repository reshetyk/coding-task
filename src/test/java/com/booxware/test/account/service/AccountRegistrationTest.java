package com.booxware.test.account.service;

import com.booxware.test.account.domain.Account;
import com.booxware.test.account.exception.AccountServiceException;
import com.booxware.test.account.repository.PersistenceInMemoryRepository;
import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class AccountRegistrationTest {

    private AccountServiceInterface accountService;
    private PersistenceInMemoryRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new PersistenceInMemoryRepository();
        accountService = new AccountService(repository, MessageDigest.getInstance("MD5"));
    }

    @Test
    public void successfulRegistration() throws Exception {
        Account account = accountService.register("alex", "email", "1234");

        assertThat(account.getId().isPresent()).isTrue();
        Optional<Account> savedAccount = repository.findById(account.getId().get());

        assertThat(savedAccount.isPresent()).isTrue();
        assertThat(savedAccount.get()).isEqualTo(account);
    }

    @Test
    public void shouldThrowExceptionWhenUserAlreadyExists() throws Exception {
        accountService.register("user1", "user@gmail.com", "1234");
        accountService.register("user2", "user2@gmail.com", "1234");

        assertThatThrownBy(() -> accountService.register("user1", "user@gmail.com", "1234"))
                .isInstanceOf(AccountServiceException.class)
                .hasMessageContaining("User with name [user1] already exists");
    }
}