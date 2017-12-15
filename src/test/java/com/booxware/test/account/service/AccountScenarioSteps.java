package com.booxware.test.account.service;

import com.booxware.test.account.repository.PersistenceInMemoryRepository;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Reshetuyk on 14.12.2017.
 */
public class AccountScenarioSteps {

    private String username;
    private String email;
    private String password;

    private AccountServiceInterface accountService;

    @Given("the user with username: $username and email: $email and password: $password")
    public void initUserDetails(@Named("$username") String username,
                                @Named("email") String email,
                                @Named("password") String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @When("the user is registered new account")
    public void register() {
        accountService = new AccountService(new PersistenceInMemoryRepository());
        accountService.register(username, email, password);
    }

    @Then("system returns true that user logged in since current date/time")
    public void checkMoney() {
        assertThat(accountService.hasLoggedInSince(new Date(), username), is(true));
    }
}
