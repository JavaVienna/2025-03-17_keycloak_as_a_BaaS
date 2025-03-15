# Java Vienna Keycloak Theme Example

This shows some minimal possible changes to Keycloak Themes and how to apply them.

## Install

* Run "mvn package"
* Put the resulting JAR file in the providers folder of Keycloak (/opt/keycloak/providers)
  * This can be done manually, using a dockerfile (like in this project) or by mounting the file into the right place.
* Run "kc.sh build" to rebuild the classloader, enabling the "plugin" (= theme)
* Login to Keycloak as admin and set the new theme at `Realm Settings > Themes`

## How to know what to change?

The build-in themes are a good starting-point to see what is in the base-theme and how to potentially configure 
things without starting from scratch: https://github.com/keycloak/keycloak/tree/main/themes/src/main/resources.

Besides that, there are a lot of example-themes on Github (https://github.com/topics/keycloak-theme), 
just lookout for a working build-system (maven/gradle, jar build). Earlier themes might rely on old Wildfly/JBoss 
formats (EAP, WAR) or added files directly to the themes folder. Those techniques are not going to work 
with recent Keycloak versions (19.0+).

If you want to fully customize Keycloak maybe have a look at https://github.com/keycloakify/keycloakify.
