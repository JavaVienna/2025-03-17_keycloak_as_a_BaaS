# Java Vienna Meetup Group - Using Keycloak to secure a Quarkus WebApp

## Setup

### Prerequisites

* podman or docker installed (podman desktop recommended)
* java 21+

### Steps

* first run maven to build all dependencies, install the jars into the local maven-repo and builds all containers locally.
  * run `mvn clean install` inside the root project
* after caddy is online you have to get the temporary local ca to be able to connect to the applications without tls errors.
  * run `podman cp caddy:/data/caddy/pki/authorities/local/root.crt ./`
  * as admin: keytool -importcert -keystore "<JAVA_HOME>\lib\security\cacerts" -storepass changeit -noprompt -trustcacerts -alias caddy-ca -file "root.crt"
* now you should be able to reach keycloak, the jaeger-ui and the example-apps using nip.io domains (examples will not work until you complete the setup).
  * keycloak: https://auth.127-0-0-1.nip.io/
  * tracing: https://tracing.127-0-0-1.nip.io/
  * quarkus REST api example: https://app.127-0-0-1.nip.io/
  * SPA example: https://spa.127-0-0-1.nip.io/
* Login to keycloak with username "admin" and password "admin"
* Now we have to configure keycloak. There is a working example-realm provided


## Additional things to do

* Now you can have a look at the HOWTOs or play around with the setup yourself.
  * [HowTo: Export a realm](./how-to/export-realm.md)