package Spring.FX.validaciones;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String EMAIL_REGEX =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
                    "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean validarEmail(String email) throws IllegalArgumentException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío o ser nulo");
        }

        if (!PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Formato de email inválido: " + email);
        }

        return true;
    }
}