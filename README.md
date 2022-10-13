This is a project which identifies the perfect date for a seminar based on multiple criteria in multiple locations across the globe.

Project details:
There is a company which has global clients and want to conduct a 2-day seminar in all locations. The company need an ideal date for organising the event based on
the client availability, earliest start date and country wise.

The project has an API which provides information about clients in multiple locations
and details of the dates on which they are available.

Conditions involved:

1. Each county has a single 2-day event.
2. If clients are available on multiple dates, choose the earliest.
3. If clients can't be present on consecutive dates, eliminate them.

Steps involved in this project to solve it.
1. Call an API to get the list of client data.
2. Check for consecutive date availability of each individual client.
3. Filter data according to country and identify the earliest date possible.
4. Filter clients who are available on the earliest date and form a list.
5. Format into a JSON and send the data using POST API.

This project contains:
 * Java 11 as programming language
 * Gradle as build tool

Dependencies:
 * SpringBoot
 * Lombok
 * Jackson
