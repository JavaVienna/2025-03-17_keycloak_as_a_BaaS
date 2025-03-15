FROM quay.io/keycloak/keycloak:26.1.4

COPY ./target/libs/*.jar /opt/keycloak/providers/

RUN /opt/keycloak/bin/kc.sh build
