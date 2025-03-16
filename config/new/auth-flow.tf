resource "keycloak_authentication_flow" "flow" {
  realm_id = var.keycloak_realm_id
  alias    = "browser custom"
}

resource "keycloak_authentication_execution" "cookie" {
  realm_id          = var.keycloak_realm_id
  parent_flow_alias = keycloak_authentication_flow.flow.alias
  authenticator     = "auth-cookie"
  requirement       = "ALTERNATIVE"

  priority = 10
}

resource "keycloak_authentication_execution" "idp" {
  realm_id          = var.keycloak_realm_id
  parent_flow_alias = keycloak_authentication_flow.flow.alias
  authenticator     = "identity-provider-redirector"
  requirement       = "ALTERNATIVE"

  priority = 20
}

resource "keycloak_authentication_subflow" "magic_link" {
  realm_id = var.keycloak_realm_id
  parent_flow_alias = keycloak_authentication_flow.flow.alias
  alias = "magic link forms"
  requirement = "ALTERNATIVE"

  priority = 60
}

resource "keycloak_authentication_execution" "magic_link_form" {
  realm_id          = var.keycloak_realm_id
  parent_flow_alias = keycloak_authentication_subflow.magic_link.alias
  authenticator     = "ext-magic-form"
  requirement = "REQUIRED"

  priority = 10
}

resource "keycloak_authentication_subflow" "username_password" {
  realm_id = var.keycloak_realm_id
  parent_flow_alias = keycloak_authentication_flow.flow.alias
  alias = "browser forms"
  requirement = "ALTERNATIVE"

  priority = 40
}

resource "keycloak_authentication_execution" "username_password_form" {
  realm_id          = var.keycloak_realm_id
  parent_flow_alias = keycloak_authentication_subflow.username_password.alias
  authenticator     = "auth-username-password-form"
  requirement = "REQUIRED"

  priority = 41
}

resource "keycloak_authentication_subflow" "username_password_otp" {
  realm_id = var.keycloak_realm_id
  parent_flow_alias = keycloak_authentication_subflow.username_password.alias
  alias = "otp forms"
  requirement = "CONDITIONAL"

  priority = 50
}

resource "keycloak_authentication_execution" "condition_otp_configured" {
  realm_id = var.keycloak_realm_id
  parent_flow_alias = keycloak_authentication_subflow.username_password_otp.alias
  authenticator     = "conditional-user-configured"
  requirement = "REQUIRED"

  priority = 51
}

resource "keycloak_authentication_execution" "otp_form" {
  realm_id = var.keycloak_realm_id
  parent_flow_alias = keycloak_authentication_subflow.username_password_otp.alias
  authenticator     = "auth-otp-form"
  requirement = "REQUIRED"

  priority = 52
}

resource "keycloak_authentication_bindings" "bindings" {
  realm_id = var.keycloak_realm_id
  browser_flow = keycloak_authentication_flow.flow.alias
}