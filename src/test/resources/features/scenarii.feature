#see https://cucumber.io/docs/gherkin/reference/
Feature: Todo API

  Scenario Outline: Find by id should return correct entity
    Given table todo contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
    When call find by id with id="<id>"
    Then the http status is 200
    And the returned todo has properties title="<title>",description="<description>" and completed="<completed>"
    Examples:
      | id                                   | title   | description   | completed  |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | title 1 | description 1 | false      |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | title 2 | description 2 | false      |


  Scenario Outline: Find by id with an non existing id should return 404
    Given table todo contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
    When call find by id with id="<bad_id>"
    Then the http status is 404
    Examples:
      |bad_id                              |
      |27a281a6-0882-4460-9d95-9c28f5852db1|
      |28a281a6-0882-4460-9d95-9c28f5852db1|

  Scenario Template: Find all should return correct list
    Given table todo contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
    When call find all
    Then the http status is 200
    And the returned list has 2 elements
    And that list contains values: title="<title>", description="<description>" and completed="<completed>":
    Examples:
      | title   | description   | completed  |
      | title 1 | description 1 | false      |
      | title 2 | description 2 | false      |

  Scenario: Delete an existing todo should return 204
    Given table todo contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
   When call delete with id="17a281a6-0882-4460-9d95-9c28f5852db1"
   Then the http status is 204

  Scenario: Delete an non existing todo should return 404
    Given table todo contains data:
      |id                                  |title  |description  |completed   |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
    When call delete with id="27a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 404

  Scenario: Complete an existing todo should return 202
    Given table todo contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
    When call complete with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 202
    And the completed todo has property completed="true"

  Scenario: Complete an non existing todo should return 404
    Given table todo contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
    When call complete with id="27a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 404

  Scenario Outline: Add todo should return 201
    Given table todo contains data:
      |id                                  |title  |description  |completed  |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false      |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false      |
    And title = "<title>"
    And  description = "<description>"
    When call add todo
    Then the http status is 201
    And the created todo has properties title="<title>", description="<description>", completed="<completed>"
    Examples:
      |title    |description    |completed  |
      |title 11 |description 11 |false      |

  Scenario: Add todo with an existing title should return 409
    Given table todo contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false     |
    When title = "title 1"
    And  description = "description 1.1"
    When call add todo
    Then the http status is 409

  Scenario Outline: Update an existing todo should return 202
    Given table todo contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
      |18a281a6-0882-4460-9d95-9c28f5852db1|title 2|description 2|false     |
    And  title = "<title>"
    And  description = "<description>"
    When call update todo with id="<id>"
    Then the http status is 202
    And the updated todo has properties title="<title>", description="<description>", completed="<completed>"
    Examples:
      |id                                   |title     |description     |completed|
      |17a281a6-0882-4460-9d95-9c28f5852db1 |title 1.1 |description 1.1 | false   |

  Scenario: Update an non existing todo should return 404
    Given title = "title 1"
    And  description = "description 1"
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 404

  Scenario: Add todo with title exceeding 80 characters should return 400
    Given title contains 81 characters
    And description contains 255 characters
    When call add todo
    Then the http status is 400

  Scenario: Add todo with title less than 2 characters should return 400
    Given title contains 1 characters
    And description contains 255 characters
    When call add todo
    Then the http status is 400

  Scenario: Add todo with description exceeding 255 characters should return 400
    Given title contains 50 characters
    And description contains 256 characters
    When call add todo
    Then the http status is 400


  Scenario: Update todo with title exceeding 80 characters should return 400
    Given table todo contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
    And title contains 81 characters
    And description contains 255 characters
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400

  Scenario: Update todo with title less than 2 characters should return 400
    Given table todo contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
    And title contains 1 characters
    And description contains 255 characters
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400

  Scenario: Update todo with description exceeding 255 characters should return 400
    Given table todo contains data:
      |id                                  |title  |description  |completed |
      |17a281a6-0882-4460-9d95-9c28f5852db1|title 1|description 1|false     |
    And title contains 50 characters
    And description contains 256 characters
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400
