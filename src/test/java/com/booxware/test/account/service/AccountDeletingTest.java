package com.booxware.test.account.service;

import com.booxware.test.account.domain.Account;
import com.booxware.test.account.exception.AccountServiceException;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class AccountDeletingTest extends AccountAbstractTest {

    @Test
    public void shouldSuccessfulDeleteAccount() throws Exception {
        Account account = accountService.register("alex", "email", "1234");

        assertThat(account.getId().isPresent()).isTrue();
        accountService.deleteAccount("alex");
        Optional<Account> savedAccount = repository.findByName("alex");

        assertThat(savedAccount.isPresent()).isFalse();
    }


    @Test
    public void shouldThrowExceptionIfAccountNotFound() throws Exception {
        accountService.register("user1", "user@gmail.com", "1234");
        accountService.register("user2", "user2@gmail.com", "1234");

        assertThatThrownBy(() -> accountService.deleteAccount("user3"))
                .isInstanceOf(AccountServiceException.class)
                .hasMessageContaining("Not found user with name [user3]");
    }
}