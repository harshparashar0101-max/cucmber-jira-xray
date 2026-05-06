Feature: Login Functionality

  Background:
    Given user is on login page
  
  @AI-18
  @smoke
  Scenario: Login with valid credentials
    When user enters valid email and password
    And clicks on login button
    Then user should be logged in successfully

  @smoke
  Scenario: Login with invalid credentials
    When user enters invalid email and password
    And clicks on login button
    Then error message should be displayed

