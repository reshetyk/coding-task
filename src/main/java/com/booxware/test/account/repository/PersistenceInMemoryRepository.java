package com.booxware.test.account.repository;

import com.booxware.test.account.domain.Account;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Created by Reshetuyk on 14.12.2017.
 */
public class PersistenceInMemoryRepository implements PersistenceInterface {

    private static final long INITIAL_ID = 1L;

    private final ConcurrentHashMap<Long, Account> storage = new ConcurrentHashMap();

    @Override
    public synchronized void save(Account a) {
        long id = a.getId().orElseGet(() -> getLastId().incrementAndGet());
        Account account = new Account(id, a.getUsername(), a.getEncryptedPassword(), a.getSalt(), a.getEmail(), a.getLastLogin());
        storage.put(id, account);
    }

    @Override
    public synchronized Account findById(long id) {
        Account account = storage.get(id);
        if (isNull(account))
            throw new RuntimeException("not found account with id [" + id + "]");

        return account;
    }

    @Override
    public synchronized Account findByName(String name) {
        assertNotNull(name);

        List<Account> accounts = storage.values().stream()
                .filter(account -> name.equals(account.getUsername()))
                .collect(Collectors.toList());

        assertOnlyOne(accounts);

        return accounts.stream().findFirst().get();
    }

    @Override
    public synchronized void delete(Account account) {
        assertNotNull(account);

        if (account.getId().isPresent() && storage.containsKey(account.getId().get())) {
            storage.remove(account.getId());
        } else {
            storage.remove(find(account).getId());
        }
    }

    private synchronized Account find(Account account) {
        List<Account> accounts = storage.values().stream()
                .filter(a -> Objects.equals(account, a))
                .collect(Collectors.toList());

        assertOnlyOne(accounts);

        return accounts.stream().findFirst().get();
    }

    private synchronized AtomicLong getLastId() {
        return new AtomicLong(storage.keySet().stream().sorted().findFirst().orElseGet(() -> INITIAL_ID));
    }

    private static void assertNotNull(Object obj) {
        if (Objects.isNull(obj))
            throw new IllegalArgumentException("argument cannot be null");
    }

    private static void assertOnlyOne(List<Account> accounts) {
        if (accounts.isEmpty())
            throw new IllegalStateException("not found any accounts");
        if (accounts.size() > 1)
            throw new IllegalStateException("found more than one representation of [" + Account.class.toString() + "]");
    }
}
