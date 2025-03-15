# Country Code Validator for Keycloak Profile Attribute

This is an examplary keycloak plugin that provides a custom validator for a user-profile attribute. It checks if the given text is a valid ISO 3166-1 ALPHA-2 or -3 code. It shows how to handle error-messages, help-text and i18n.

## Install

* Build with "mvn package"
* Put JAR file into Keycloak Providers directory
* Run "kc.sh build"
* Start Keycloak (26.0+) and login as admin
* Go to `Realm Settings > User Profile > Create Attribute` and add a new attribte (e.g. Location).
  Click `Add validator` and select `country-code` from the drop-down. Configure what format should be accepted and click `Save`.
* Create or edit a User. Try to enter an invalid CountryCode like "TEST". After that enter a valid one like "AT" or "AUT".
  * Don't forget that depending on your settings during attribute creation, it is possible that normal users are not able to see or edit the field. You can configure this using the Permissions on the attribute.
