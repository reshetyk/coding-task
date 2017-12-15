package com.booxware.test.account.repository;

import com.booxware.test.account.domain.Account;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class PersistenceInMemoryRepository implements PersistenceInterface {

    private static final long INITIAL_ID = 0L;

    private final ConcurrentHashMap<Long, Account> storage = new ConcurrentHashMap<>();

    @Override
    public synchronized Account save(Account a) {
        long id = a.getId().orElseGet(() -> getLastId().incrementAndGet());
        Account account = new Account(id, a.getUsername(), a.getEncryptedPassword(), a.getSalt(), a.getEmail(), a.getLastLogin());
        storage.put(id, account);
        return account;
    }

    @Override
    public synchronized Optional<Account> findById(long id) {
        Account account = storage.get(id);
        if (isNull(account))
            return Optional.empty();

        return Optional.of(account);
    }

    @Override
    public synchronized Optional<Account> findByName(String name) {
        assertNotNull(name);

        List<Account> accounts = storage.values().stream()
                .filter(account -> name.equals(account.getUsername()))
                .collect(Collectors.toList());

        assertThatNotMoreThanOne(accounts);

        return accounts.stream().findFirst();
    }

    @Override
    public synchronized void delete(Account account) {
        assertNotNull(account);

        if (account.getId().isPresent() && storage.containsKey(account.getId().get())) {
            storage.remove(account.getId().get());
        } else {
            storage.remove(find(account).getId().get());
        }
    }

    private synchronized Account find(Account account) {
        List<Account> accounts = storage.values().stream()
                .filter(a -> Objects.equals(account, a))
                .collect(Collectors.toList());

        assertThatNotMoreThanOne(accounts);

        return accounts.stream().findFirst().get();
    }

    synchronized AtomicLong getLastId() {
        return new AtomicLong(storage.keySet().stream()
                .sorted((o1, o2) -> new Long(o2 - o1).intValue())
                .findFirst()
                .orElseGet(() -> INITIAL_ID));
    }

    Integer countAll () {
        return storage.size();
    }

    private static void assertNotNull(Object obj) {
        if (Objects.isNull(obj))
            throw new IllegalArgumentException("argument cannot be null");
    }

    private static void assertThatNotMoreThanOne(List<Account> accounts) {
        if (accounts.size() > 1)
            throw new IllegalStateException("found more than one representation of [" + Account.class.toString() + "]");
    }
}
