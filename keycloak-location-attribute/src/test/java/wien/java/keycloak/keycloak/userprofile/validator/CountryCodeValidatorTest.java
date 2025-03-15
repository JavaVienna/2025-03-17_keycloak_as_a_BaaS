package wien.java.keycloak.keycloak.userprofile.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.keycloak.models.KeycloakSession;
import org.keycloak.validate.ValidationContext;
import org.keycloak.validate.ValidatorConfig;
import org.mockito.Mock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CountryCodeValidatorTest {

    @Mock
    private KeycloakSession session;

    @CsvSource({
            "AT, true",
            "XX, false",
            "AUT, true",
            "XXX, false",
            "A, false",
            "AAAAAAAA, false"
    })
    @ParameterizedTest
    void doValidate_Both(String value, Boolean isValid) {
        var context = new ValidationContext(session);
        var config = new ValidatorConfig(Map.of(
                CountryCodeValidator.KEY_ALPHA2, Boolean.TRUE,
                CountryCodeValidator.KEY_ALPHA3, Boolean.TRUE
        ));
        CountryCodeValidator.INSTANCE.doValidate(value, "hint1", context, config);
        assertEquals(context.isValid(), isValid);
    }

    @CsvSource({
            "AT, true",
            "XX, false",
            "AUT, false",
            "XXX, false",
            "A, false",
            "AAAAAAAA, false"
    })
    @ParameterizedTest
    void doValidate_Alpha2(String value, Boolean isValid) {
        var context = new ValidationContext(session);
        var config = new ValidatorConfig(Map.of(
                CountryCodeValidator.KEY_ALPHA2, Boolean.TRUE,
                CountryCodeValidator.KEY_ALPHA3, Boolean.FALSE
        ));
        CountryCodeValidator.INSTANCE.doValidate(value, "hint1", context, config);
        assertEquals(context.isValid(), isValid);
    }

    @CsvSource({
            "AT, false",
            "XX, false",
            "AUT, true",
            "XXX, false",
            "A, false",
            "AAAAAAAA, false"
    })
    @ParameterizedTest
    void doValidate_Alpha3(String value, Boolean isValid) {
        var context = new ValidationContext(session);
        var config = new ValidatorConfig(Map.of(
                CountryCodeValidator.KEY_ALPHA2, Boolean.FALSE,
                CountryCodeValidator.KEY_ALPHA3, Boolean.TRUE
        ));
        CountryCodeValidator.INSTANCE.doValidate(value, "hint1", context, config);
        assertEquals(context.isValid(), isValid);
    }

    @CsvSource({
            "True, True, True",
            "True, False, True",
            "False, True, True",
            "False, False, False",
    })
    @ParameterizedTest
    void validateConfig(Boolean alpha2Enabled, Boolean alpha3Enabled, Boolean isValid) {
        var config = new ValidatorConfig(Map.of(
                CountryCodeValidator.KEY_ALPHA2, alpha2Enabled,
                CountryCodeValidator.KEY_ALPHA3, alpha3Enabled
        ));
        var configValidationResult = CountryCodeValidator.INSTANCE.validateConfig(session, config);
        assertEquals(configValidationResult.isValid(), isValid);
    }

}