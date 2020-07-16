Feature: Receive accumulate statistics, process it and generate daily statistics

  Scenario: Receive accumulate covid19 statistic and generate daily statistics
    Given a list of daily statistics with accumulate data
    When the stream processes the message
    And the new message is sent to the destination topic
    Then a consumer receives the message correctly
    And the daily statistics were calculated correctly

  Scenario: Receive accumulate covid19 statistic for different countries
    Given a list of daily statistics with accumulate data different countries
    When the stream processes the message
    And the new message is sent to the destination topic
    Then a consumer receives the message correctly
    And the daily statistics were calculated correctly