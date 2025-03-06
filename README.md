# Java Vienna Meetup Group - Using Keycloak to secure a Quarkus WebApp

## Setup

### Prerequisites

* podman or docker installed (podman desktop recommended)
* java 21+

### Steps
* podman cp caddy:/data/caddy/pki/authorities/local/root.crt ./
* as admin: keytool -importcert -keystore "<JAVA_HOME>\lib\security\cacerts" -storepass changeit -noprompt -trustcacerts -alias caddy-ca -file "root.crt"