package apollo.datastore;

import java.util.Arrays;
import java.util.List;

public class Locale {

    private Locale() {}

    public static final List<String> SUPPORTED_LANGUAGES = Arrays.asList("en", "ja");
    public static final List<String> SUPPORTED_LANGUAGES_TEXT = Arrays.asList("English", "日本語");
    public static final String DEFAULT_LANGUAGE = "en";
}
