Feature: Signup Functionality

  Background:
    Given user is on signup page
  
  @AI-22
  @regression
  Scenario: Open signup page
    Then new user signup section should be visible
    
   @regression
  Scenario: Signup with already registered email
    When user enters existing name and email
    And user clicks signup button
    Then existing email error message should be displayed