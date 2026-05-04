Feature: Login functionality

  @AI-18
  @smoke
  Scenario: Verify user login with valid credentials
    Given user is on login page
    When user enters valid username and password
    Then user should be logged in successfully