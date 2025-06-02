#see https://cucumber.io/docs/gherkin/reference/
Feature:  BDD Scenarios of Todo API

#  Background:
#    Given table todo contains data:
#      | id                                   | title        | description        | completed | date_debut          | date_fin            |
#      | 17a281a6-0882-4460-9d95-9c28f5852db1 | Rendre notes | Rendre notes DIC 1 | false     | 2025-08-19T12:00:00 | 2025-08-19T12:15:00 |
#      | 18a81a6-0882-4460-9d95-9c28f5852db1  | Presentation | Presentation DIC 1 | false     | 2025-08-12T15:00:00 | 2030-09-02T19:00:00 |
#
#  Scenario Outline: Add todo should return 201
#    Given table todo contains data:
#      | id                                   | title   | description   | completed | date_debut          | date_fin            |
#      | 17a281a6-0882-4460-9d95-9c28f5852db1 | title 1 | description 1 | false     | 2030-09-02T12:00:00 | 2030-09-02T12:15:00 |
#      | 18a281a6-0882-4460-9d95-9c28f5852db1 | title 2 | description 2 | false     | 2030-09-02T12:00:00 | 2030-09-02T12:15:00 |
#    And title = "<title>"
#    And description = "<description>"
#    And dateDebut = "<dateDebut>"
#    And dateFin = "<dateFin>"
#    When call add todo
#    Then the http status is 201
#    And the created todo has properties title="<title>", description="<description>", completed="<completed>"
#    Examples:
#      | title    | description    | completed | date_debut          | date_fin            |
#      | title 11 | description 11 | false     | 2030-09-02T12:00:00 | 2030-09-02T12:15:00 |
#
#  Scenario: Add todo with title exceeding 80 characters should return 400
#    Given title contains 81 characters
#    And description contains 255 characters
#    When call add todo
#    Then the http status is 400
#
#  Scenario: Add todo with title less than 2 characters should return 400
#    Given title contains 1 characters
#    And description contains 255 characters
#    When call add todo
#    Then the http status is 400
#
#  Scenario: Add todo with description exceeding 255 characters should return 400
#    Given title contains 50 characters
#    And description contains 256 characters
#    When call add todo
#    Then the http status is 400
#
