package com.booxware.test.account.repository;

import com.booxware.test.account.domain.Account;

import java.util.Optional;

/**
 * Persistence can be very simple, for example an in memory hash map.
 * 
 */
public interface PersistenceInterface {

	public Account save(Account a);

	public Optional<Account> findById(long id);

	public Optional<Account> findByName(String name);

	public void delete(Account account);

}
