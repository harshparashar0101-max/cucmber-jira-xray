Feature: Cart Functionality
  
  @cart 
  Scenario: Add product to cart
    Given user is on products page
    When user adds first product to cart
    And user clicks view cart
    Then product should be visible in cart