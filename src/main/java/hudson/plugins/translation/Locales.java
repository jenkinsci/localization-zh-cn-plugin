package hudson.plugins.translation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.kohsuke.stapler.Stapler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Kohsuke Kawaguchi
 */
public class Locales {
    @SuppressFBWarnings(value = "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", 
            justification = "We expose it as API")
    public static final class Entry {
        /**
         * Human readable display name of this locale, like "Japanese"
         */
        public final String name;
        /**
         * Locale identifier like "ja" or "en_US".
         */
        public final String code;
        /**
         * The same as {@link #code}, but in lower case.
         */
        public final String lcode;
        public final Locale locale;

        public Entry(String name, String code) {
            this.name = name;
            this.code = code;
            this.lcode = code.toLowerCase(Locale.ENGLISH);

            String[] tokens = code.split("_");
            if(tokens.length==1) {
                this.locale = new Locale(tokens[0]);
            } else {
                this.locale = new Locale(tokens[0],tokens[1]);
            }

        }

        public boolean matchesRequestLocale() {
            return Stapler.getCurrentRequest().getLocale().toString().equals(code);
        }
    }

    private static List<Entry> convert(String... args) {
        List<Entry> r = new ArrayList<Entry>();
        for (int i=0; i<args.length; i+=2)
            r.add(new Entry(args[i],args[i+1]));
        return r;
    }

    public static final List<Entry> LIST = convert(
        "Afrikaans","af",
        "Albanian","sq",
        "Arabic","ar",
        "Basque","eu",
        "Belarusian","be",
        "Bengali (India)","bn_IN",
        "Bulgarian","bg",
        "Catalan","ca",
        "Chinese (Simplified)","zh_CN",
        "Chinese (Traditional)","zh_TW",
        "Czech","cs",
        "Danish","da",
        "Dutch"	,"nl",
        "English (British)","en_GB",
        "Esperanto","eo",
        "Estonian","et",
        "Faroese","fo",
        "Finnish","fi",
        "French","fr",
        "Frisian","fy_NL",
        "Galician","gl",
        "Georgian","ka",
        "German","de",
        "Greek","el",
        "Gujarati","gu_IN",
        "Hebrew","he",
        "Hindi","hi_IN",
        "Hungarian","hu",
        "Icelandic","is",
        "Indonesian","id",
        "Irish","ga_IE",
        "Italian","it",
        "Japanese","ja",
        "Kannada","kn",
        "Korean","ko",
        "Kurdish","ku",
        "Latvian","lv",
        "Lithuanian","lt",
        "Macedonian","mk",
        "Marathi","mr",
        "Mongolian","mn",
        "Norwegian (Bokmål)","nb_NO",
        "Norwegian (Nynorsk)","nn_NO",
        "Occitan (Lengadocian)","oc",
        "Polish","pl",
        "Portuguese (Brazilian)","pt_BR",
        "Portuguese (Portugal)","pt_PT",
        "Punjabi","pa_IN",
        "Romanian","ro",
        "Russian","ru",
        "Serbian","sr",
        "Sinhala","si",
        "Slovak","sk",
        "Slovenian","sl",
        "Spanish","es",
        "Spanish (Argentina)","es_AR",
        "Swedish","sv_SE",
        "Tamil","ta",
        "Telugu","te",
        "Thai","th",
        "Turkish","tr",
        "Ukrainian","uk");
}
