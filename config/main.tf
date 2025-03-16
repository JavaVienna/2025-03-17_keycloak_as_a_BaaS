terraform {
  required_providers {
    # https://registry.terraform.io/providers/mrparkers/keycloak/latest
    keycloak = {
      source  = "mrparkers/keycloak"
      version = "4.4.0"
    }
  }
}

provider "keycloak" {
  client_id = "admin-cli"
  username  = "admin"
  password  = "admin"
  url       = "http://keycloak:8080"
}

resource "keycloak_realm" "realm" {
  realm        = "javavienna"
  registration_allowed           = true
  registration_email_as_username = true
  login_with_email_allowed = true
  verify_email                   = true
  reset_password_allowed = true

  # we intentionally set this to a very log value to see what happens on access_token timeout
  access_token_lifespan = "1m0s"

  # this is a mocked smtp-server. See README for the url
  smtp_server {
    from     = "noreply@example.com"
    host     = "mail"
    port     = "1025"
    ssl      = false
    starttls = false
  }
}

resource "keycloak_realm_events" "realm" {
  realm_id = keycloak_realm.realm.id
  events_listeners = [
    "jboss-logging", # default logging event-handler
    "webhook-http", # webhook plugin (see README)
    "email" # magic-link plugin (see README)
  ]
}

# we just create a group for admins
resource "keycloak_group" "admins" {
  realm_id = keycloak_realm.realm.id
  name     = "admins"
}

# we create a client for the quarkus REST-API example with CONFIDENTIAL access_type because this is a trusted client.
resource "keycloak_openid_client" "quarkus_example" {
  realm_id  = keycloak_realm.realm.id
  client_id = "quarkus-example"
  name      = "Quarkus Example"
  access_type         = "CONFIDENTIAL"
  standard_flow_enabled = true
  direct_access_grants_enabled = true
  valid_redirect_uris = [
    "https://app.127-0-0-1.nip.io/*",
    "http://localhost:8080/*" # to support dev-mode swagger-ui
  ]
  web_origins   = [
    "https://app.127-0-0-1.nip.io",
    "+" # to support dev-mode swagger-ui, avoid in production!
  ]
  client_secret = "zAnPi52RkG9j4xlwAOjUq8aqMne8DU7x"
}

# we want to create an client-role to allow users to get admin access to the api
resource "keycloak_role" "quarkus_admin" {
  name     = "admin"
  realm_id = keycloak_realm.realm.id
  client_id = keycloak_openid_client.quarkus_example.id
}

# we also want a client for the SPA example, but now with PUBLIC access_type to allow SPAs to get tokens
resource "keycloak_openid_client" "spa_example" {
  realm_id  = keycloak_realm.realm.id
  client_id = "spa-example"
  name      = "SPA Example App"
  access_type         = "PUBLIC"
  standard_flow_enabled = true
  valid_redirect_uris = ["https://spa.127-0-0-1.nip.io/*"]
  web_origins   = ["https://spa.127-0-0-1.nip.io"]
}

# lets create a realm admin user
resource "keycloak_user" "admin" {
  realm_id   = keycloak_realm.realm.id
  username   = "jv@example.com"
  enabled    = true

  email      = "jv@example.com"
  first_name = "JavaVienna"
  last_name  = "Admin"

  email_verified = true

  initial_password {
    value     = "admin"
    temporary = false
  }
}

# we have to get existing (default) clients and roles using "data sources", we do not create those
data "keycloak_openid_client" "realm_management" {
  realm_id  = keycloak_realm.realm.id
  client_id = "realm-management"
}

data "keycloak_role" "realm_admin" {
  realm_id = keycloak_realm.realm.id
  client_id = data.keycloak_openid_client.realm_management.id
  name     = "realm-admin"
}

# now we configure roles on the admin group
resource "keycloak_group_roles" "admins_group_roles" {
  group_id = keycloak_group.admins.id
  realm_id = keycloak_realm.realm.id
  role_ids = [
    data.keycloak_role.realm_admin.id, # for admin-access to the realm
    keycloak_role.quarkus_admin.id # for admin-access to the quarkus backend
  ]
}

# and we add the admin-user as group member
resource "keycloak_group_memberships" "admins_members" {
  members = [
    keycloak_user.admin.username
  ]
  group_id = keycloak_group.admins.id
  realm_id = keycloak_realm.realm.id
}

# we also want a unprivileged user to test things
resource "keycloak_user" "user" {
  realm_id   = keycloak_realm.realm.id
  username   = "user@example.com"
  enabled    = true

  email      = "user@example.com"
  first_name = "JavaVienna"
  last_name  = "User"

  email_verified = true

  initial_password {
    value     = "user"
    temporary = false
  }
}

# we can also configure some custom profile files for users
resource "keycloak_realm_user_profile" "profile" {
  realm_id = keycloak_realm.realm.id

  // for this version of the provider we have to define all default attributes - sadly
  // -----
  attribute {
    name         = "username"
    display_name = "$${username}"

    validator {
      name = "length"
      config = {
        min = "3"
        max = "255"
      }
    }
    validator { name = "username-prohibited-characters" }
    validator { name = "up-username-not-idn-homograph" }

    permissions {
      view = ["admin", "user"]
      edit = ["admin", "user"]
    }
  }

  attribute {
    name         = "email"
    display_name = "$${email}"

    required_for_roles = ["user"]

    validator { name = "email" }
    validator {
      name = "length"
      config = { max = "255" }
    }

    permissions {
      view = ["admin", "user"]
      edit = ["admin", "user"]
    }
  }

  attribute {
    name         = "firstName"
    display_name = "$${firstName}"

    required_for_roles = ["user"]

    validator {
      name = "length"
      config = { max = "255" }
    }
    validator { name = "person-name-prohibited-characters" }

    permissions {
      view = ["admin", "user"]
      edit = ["admin", "user"]
    }
  }

  attribute {
    name         = "lastName"
    display_name = "$${lastName}"

    required_for_roles = ["user"]

    validator {
      name = "length"
      config = { max = "255" }
    }
    validator { name = "person-name-prohibited-characters" }

    permissions {
      view = ["admin", "user"]
      edit = ["admin", "user"]
    }
  }
  // -----

  group {
    name               = "user-details"
    display_header     = "Details"
    display_description = ""
  }

  attribute {
    name         = "yearsOfJavaExperience"
    display_name = "Years Of Java Experience"
    group        = "user-details"

    permissions {
      view = ["admin", "user"]
      edit = ["admin", "user"]
    }

    validator {
      name = "double"
      config = {"min":"0","max":"99"}
    }
  }

  attribute {
    name         = "location"
    display_name = "Main Location"
    group        = "user-details"

    permissions {
      view = ["admin", "user"]
      edit = ["admin", "user"]
    }

    validator {
      name = "country-code"
      config = {"cc-type-alpha2":"true","cc-type-alpha3":"true"}
    }
  }
}

module "module_using_keycloak_new" {
  source = "./new"
  keycloak_realm_id = keycloak_realm.realm.id
}
