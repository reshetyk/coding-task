package com.booxware.test.account.repository;

import com.booxware.test.account.domain.Account;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class PersistenceInMemoryRepositoryTest {
    private PersistenceInMemoryRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new PersistenceInMemoryRepository();
    }

    @Test
    public void shouldGenerateUniqueId() throws Exception {
        repository.save(new Account("user1", "111".getBytes(), "email1"));
        assertThat(repository.getLastId().get()).isEqualTo(1);
        assertThat(repository.getLastId().get()).isEqualTo(1);

        repository.save(new Account("user2", "222".getBytes(), "email2"));
        assertThat(repository.getLastId().get()).isEqualTo(2);
        assertThat(repository.getLastId().get()).isEqualTo(2);

        repository.save(new Account("user3", "333".getBytes(), "email3"));
        assertThat(repository.getLastId().get()).isEqualTo(3);
        assertThat(repository.getLastId().get()).isEqualTo(3);
    }

    @Test
    public void shouldAddNewIfAccountIdEmpty() throws Exception {
        Account account1 = new Account("user1", "111".getBytes(), "email1");
        assertThat(account1.getId().isPresent()).isFalse();
        account1 = repository.save(account1);
        assertThat(account1.getId().isPresent()).isTrue();
        assertThat(repository.countAll()).isEqualTo(1);

        Account account2 = new Account("user2", "111".getBytes(), "email2");
        assertThat(account2.getId().isPresent()).isFalse();
        account2 = repository.save(account2);
        assertThat(account2.getId().isPresent()).isTrue();
        assertThat(repository.countAll()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateExistedAccountIfIdNotEmpty() throws Exception {
        Account accountWithoutId = new Account("user1", "111".getBytes(), "email1");
        assertThat(accountWithoutId.getId().isPresent()).isFalse();
        accountWithoutId = repository.save(accountWithoutId);
        assertThat(accountWithoutId.getId().isPresent()).isTrue();
        assertThat(repository.countAll()).isEqualTo(1);

        Account accountWithId = new Account(1L, "user", "222".getBytes(), "salt", "email1", new Date());
        assertThat(accountWithId.getId().isPresent()).isTrue();
        accountWithId = repository.save(accountWithId);
        assertThat(accountWithId.getId().isPresent()).isTrue();
        assertThat(repository.countAll()).isEqualTo(1);
    }


}