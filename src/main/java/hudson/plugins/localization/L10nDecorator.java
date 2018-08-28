package hudson.plugins.localization;

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.PageDecorator;
import org.kohsuke.stapler.WebApp;
import org.kohsuke.stapler.jelly.JellyFacet;

/**
 * {@link PageDecorator} that adds the translation dialog.
 *
 * @author suren
 */
@Extension
public class L10nDecorator extends PageDecorator {
    private final ResourceBundleFactoryImpl bundleFactory = new ResourceBundleFactoryImpl();
    private final Hudson hudson;

    public L10nDecorator() {
        super(L10nDecorator.class);

        // hook into Stapler to activate contributed l10n
        hudson = Hudson.getInstance();
        WebApp.get(hudson.servletContext).getFacet(JellyFacet.class).resourceBundleFactory
                = bundleFactory;
    }
//
//    public List<Locales.Entry> listLocales() {
//        return Locales.LIST;
//    }

//    /**
//     * Activate the recording of the localized resources.
//     */
//    public void startRecording(StaplerRequest request) {
//        request.setAttribute(InternationalizedStringExpressionListener.class.getName(), new MsgRecorder());
//    }
//
//    public Collection<Msg> getRecording(StaplerRequest request) {
//        MsgRecorder recording = (MsgRecorder) request.getAttribute(InternationalizedStringExpressionListener.class.getName());
//        return recording != null ? recording.set : Collections.<Msg>emptySet();
//    }
//
//    public String encodeRecording(StaplerRequest request) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        // string -> gzip -> encrypt -> base64 -> string
//        PrintStream w = new PrintStream(new GZIPOutputStream(new CipherOutputStream(baos,getCipher(ENCRYPT_MODE))),
//            false, "UTF-8");
//        for (Msg e : getRecording(request)) {
//            w.println(e.resourceBundle.getBaseName());
//            w.println(e.key);
//        }
//        w.close();
//
//        return Base64.encode(baos.toByteArray());
//    }
//
//    /**
//     * Does the opposite of {@link #encodeRecording(StaplerRequest)}.
//     */
//    public List<Msg> decode(StaplerRequest request) throws IOException {
//        final GZIPInputStream gzipInpurStream = new GZIPInputStream(new CipherInputStream(
//                new ByteArrayInputStream(Base64.decode(request.getParameter("bundles"))),
//                getCipher(DECRYPT_MODE)));
//        final BufferedReader r;
//        try {
//            //TODO: Replace by StandardCharsets in JDK7
//            r = new BufferedReader(new InputStreamReader(gzipInpurStream, "UTF-8"));
//        } catch (UnsupportedEncodingException ex) {
//            throw new IOException("UTF-8 encoding is not supported .This should never happen" +
//                    ", because it's a part of Java standard starting from Java 5", ex);
//        }
//
//        List<Msg> l = new ArrayList<Msg>();
//        String s;
//        while((s=r.readLine())!=null) {
//            l.add(new Msg(bundleFactory.create(s),r.readLine()));
//        }
//        return l;
//    }

//    private Cipher getCipher(int mode) {
//        try {
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(mode, hudson.getSecretKeyAsAES128());
//            return cipher;
//        } catch (GeneralSecurityException e) {
//            throw new Error(e); // impossible
//        }
//    }
//
//    /**
//     * Looks at {@code Accept-Language} header manually and decide which locale
//     * this user is likely capable of translating.
//     */
//    public String getPrimaryTranslationLocale(StaplerRequest req) {
//        String lang = req.getHeader("Accept-Language");
//        if(lang==null)  return null;
//
//        for (String t : lang.split(",")) {
//            // ignore q=N
//            int idx = t.indexOf(';');
//            if(idx>=0)  t=t.substring(0,idx);
//
//            // HTTP uses en-US but Java uses en_US
//            t=t.replace('-','_').toLowerCase(Locale.ENGLISH);
//
//            // First, look for an exact matching, so that 'en_US' matches 'en_US' and not 'en_UK' nor 'en'
//            for (Entry e : Locales.LIST)
//                if(t.equals(e.lcode))
//                    return e.lcode;
//
//            // Eventually,  look for the generic locale, so that 'en_US' matches 'en'
//            for (Entry e : Locales.LIST)
//                if(t.startsWith(e.lcode))
//                    return e.lcode;
//        }
//
//        return null;
//    }
}
