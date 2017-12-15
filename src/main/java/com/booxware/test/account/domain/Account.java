package com.booxware.test.account.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * The encryption can be very simple, we don't put much emphasis on the
 * encryption algorithm.
 */
public final class Account implements Serializable {

	private final Optional<Long> id;

	private final String username;

	private final byte[] encryptedPassword;

	private final String salt;

	private final String email;

	private final Date lastLogin;

    //TODO create builder
    public Account(Long id, String username, byte[] encryptedPassword, String salt, String email, Date lastLogin) {
        this.id = Optional.of(id);
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.salt = salt;
        this.email = email;
        this.lastLogin = lastLogin;
    }

    public Account(String username, byte[] encryptedPassword, String email) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.email = email;
        this.id = Optional.empty();
        this.salt = "";
        this.lastLogin = new Date();
    }

    public Optional<Long> getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getEncryptedPassword() {
        return encryptedPassword.clone();
    }

    public String getSalt() {
        return salt;
    }

    public String getEmail() {
        return email;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (email != null ? !email.equals(account.email) : account.email != null) return false;
        if (!Arrays.equals(encryptedPassword, account.encryptedPassword)) return false;
        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        if (lastLogin != null ? !lastLogin.equals(account.lastLogin) : account.lastLogin != null) return false;
        if (salt != null ? !salt.equals(account.salt) : account.salt != null) return false;
        if (username != null ? !username.equals(account.username) : account.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (encryptedPassword != null ? Arrays.hashCode(encryptedPassword) : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        return result;
    }


}
