Feature: Dashboard Validation
  @AI-22
  @regression
  Scenario: Verify dashboard is visible after launching URL
    Given user launches the application URL
    Then dashboard text should be visible on header