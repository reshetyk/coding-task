Narrative: Registration of new account
    As a user
    I want to register a new account
    In order to be registered in the system

Scenario: Successful account registration
Given the user with username: alex and email: alex@gmail.com and password: 12345
When the user is registered new account
Then system returns true that user logged in since current date/time

