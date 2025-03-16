terraform {
  required_providers {
    // this is not currently supported with the stock intellij plugin :(
    # https://search.opentofu.org/provider/keycloak/keycloak/latest
    keycloak = {
      source  = "keycloak/keycloak"
      version = "5.1.1"
    }
  }
}

provider "keycloak" {
  client_id = "admin-cli"
  username  = "admin"
  password  = "admin"
  url       = "http://keycloak:8080"
}