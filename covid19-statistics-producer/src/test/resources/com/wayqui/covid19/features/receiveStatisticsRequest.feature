Feature: Send message to kafka scenario

  Scenario: Receiving request and sending to kafka
    Given a statistic for a certain country and day
    When the data is sent to the service
    Then the service returns the HTTP status 'OK'
    And a consumer receives the message correctly