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
    public void shouldIncrementLastId() throws Exception {
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
        Account a1 = new Account("user1", "111".getBytes(), "email1");
        assertThat(a1.getId().isPresent()).isFalse();
        a1 = repository.save(a1);
        assertThat(a1.getId().isPresent()).isTrue();
        assertThat(repository.countAll()).isEqualTo(1);

        Account a2 = new Account("user2", "111".getBytes(), "email2");
        assertThat(a2.getId().isPresent()).isFalse();
        a2 = repository.save(a2);
        assertThat(a2.getId().isPresent()).isTrue();
        assertThat(repository.countAll()).isEqualTo(2);
    }

    @Test
    public void shouldUpdateExistedAccountIfIdNotEmpty() throws Exception {
        Account a1 = new Account("user1", "111".getBytes(), "email1");
        assertThat(a1.getId().isPresent()).isFalse();
        a1 = repository.save(a1);
        assertThat(a1.getId().isPresent()).isTrue();
        assertThat(repository.countAll()).isEqualTo(1);

        Account a2 = new Account(1L, "user", "222".getBytes(), "salt", "email1", new Date());
        assertThat(a2.getId().isPresent()).isTrue();
        a2 = repository.save(a2);
        assertThat(a2.getId().isPresent()).isTrue();
        assertThat(repository.countAll()).isEqualTo(1);
    }


}