package wien.java.keycloak.keycloak.userprofile.validator;

import com.google.auto.service.AutoService;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ConfiguredProvider;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.validate.*;
import org.keycloak.validate.validators.ValidatorConfigValidator;

import java.util.*;

@AutoService(ValidatorFactory.class)
public class CountryCodeValidator extends AbstractStringValidator implements ConfiguredProvider {
    public static final String ID = "country-code";

    public static final String KEY_ALPHA2 = "cc-type-alpha2";
    public static final String KEY_ALPHA3 = "cc-type-alpha3";

    public static final String MESSAGE_CC_INVALID = "error-invalid-countrycode";

    public static final CountryCodeValidator INSTANCE = new CountryCodeValidator();

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {
        ProviderConfigProperty property1;
        property1 = new ProviderConfigProperty();
        property1.setName(KEY_ALPHA2);
        property1.setLabel("ALPHA-2");
        property1.setHelpText("Allow ISO 3166-1 ALPHA-2 Codes (e.g. AT, DE, US, UK)"); //sadly no i18n support here
        property1.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        configProperties.add(property1);

        ProviderConfigProperty property2;
        property2 = new ProviderConfigProperty();
        property2.setName(KEY_ALPHA3);
        property2.setLabel("ALPHA-3");
        property2.setHelpText("Allow ISO 3166-1 ALPHA-3 Codes (e.g. AUT, DEU, USA, GBR)");
        property2.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        configProperties.add(property2);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void doValidate(String value, String inputHint, ValidationContext context, ValidatorConfig config) {
        var countryCodesSets = new ArrayList<Set<String>>();
        if(config.getBooleanOrDefault(KEY_ALPHA2, false)) {
            countryCodesSets.add(Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2));
        }
        if(config.getBooleanOrDefault(KEY_ALPHA3, false)) {
            countryCodesSets.add(Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3));
        }
        if (countryCodesSets.stream().noneMatch(ccSet -> ccSet.contains(value))) {
            context.addError(new ValidationError(ID, inputHint, MESSAGE_CC_INVALID, value));
        }
    }

    @Override
    public ValidationResult validateConfig(KeycloakSession session, ValidatorConfig config) {
        Set<ValidationError> errors = new LinkedHashSet<>();
        if(config == null || !config.containsKey(KEY_ALPHA2)) {
            errors.add(new ValidationError(ID, KEY_ALPHA2, ValidatorConfigValidator.MESSAGE_CONFIG_MISSING_VALUE));
        }
        if(config == null || !config.containsKey(KEY_ALPHA3)) {
            errors.add(new ValidationError(ID, KEY_ALPHA3, ValidatorConfigValidator.MESSAGE_CONFIG_MISSING_VALUE));
        }
        if(config != null && config.containsKey(KEY_ALPHA2) && config.containsKey(KEY_ALPHA3) && !config.getBoolean(KEY_ALPHA2) && !config.getBoolean(KEY_ALPHA3)) {
            errors.add(new ValidationError(ID, KEY_ALPHA2, ValidatorConfigValidator.MESSAGE_CONFIG_INVALID_BOOLEAN_VALUE, "at least one option has to be set"));
            errors.add(new ValidationError(ID, KEY_ALPHA3, ValidatorConfigValidator.MESSAGE_CONFIG_INVALID_BOOLEAN_VALUE, "at least one option has to be set"));
        }
        return new ValidationResult(errors);
    }

    @Override
    public String getHelpText() {
        return "Country Code validator";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }
}
