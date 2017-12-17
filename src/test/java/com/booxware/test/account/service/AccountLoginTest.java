package com.booxware.test.account.service;

import com.booxware.test.account.domain.Account;
import com.booxware.test.account.exception.AccountServiceException;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccountLoginTest extends AccountAbstractTest {

    @Test
    public void shouldUpdateLastLoginDateIfLoginIsSuccessful() throws Exception {
        Date dateBefore = new Date();
        accountService.register("alex", "email", "1234");
        Account account = accountService.login("alex", "1234");
        Date dateAfter = new Date();

        assertThat(account.getId().isPresent()).isTrue();
        assertThat(account.getLastLogin()).isBetween(dateBefore, dateAfter, true, true);
    }

    @Test
    public void shouldThrowExceptionIfPasswordIncorrect() throws Exception {
        accountService.register("user1", "user@gmail.com", "1234");
        accountService.register("user2", "user2@gmail.com", "12345");

        assertThatThrownBy(() -> accountService.login("user1", "1111"))
                .isInstanceOf(AccountServiceException.class)
                .hasMessageContaining("Cannot login. Password is incorrect");
    }

    @Test
    public void shouldThrowExceptionIfUserNotRegistered() throws Exception {
        accountService.register("user2", "user2@gmail.com", "12345");

        assertThatThrownBy(() -> accountService.login("user1", "1111"))
                .isInstanceOf(AccountServiceException.class)
                .hasMessageContaining("Not found user with name [user1]");
    }
}