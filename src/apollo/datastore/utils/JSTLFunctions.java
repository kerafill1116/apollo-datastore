package apollo.datastore.utils;

import apollo.datastore.TimeZone;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;

import java.util.ArrayList;

public class JSTLFunctions {

    public static String defaultLanguage() {
        return Locale.DEFAULT_LANGUAGE;
    }

    public static String langOptionsQueryString(String queryString) {
        // queryString is not preceded by a question mark (?)
        if(queryString != null) {
            int indexOfLang = queryString.toLowerCase().indexOf(Cookies.LANG.getName());
            if(indexOfLang != -1) {
                StringBuilder sbQueryString = new StringBuilder(queryString);
                if(indexOfLang > 0)
                    --indexOfLang;
                sbQueryString.delete(indexOfLang, indexOfLang + 8);
                queryString = sbQueryString.toString();
            }
            if(queryString.length() > 0)
                queryString = "&" + queryString;
        }
        else
            queryString = "";
        return queryString;
    }

    public static String[] supportedLanguagesArray() {
        return Locale.SUPPORTED_LANGUAGES.toArray(new String[0]);
    }

    public static String[] supportedLanguagesTextArray() {
        return Locale.SUPPORTED_LANGUAGES_TEXT.toArray(new String[0]);
    }

    public static ArrayList<TimeZone> timeZonesArray() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query(TimeZone.DatastoreProperties.KIND.getName());
        q.addProjection(new PropertyProjection(TimeZone.DatastoreProperties.TIME_ZONE_ID.getName(), String.class));
        PreparedQuery pq = datastore.prepare(q);
        ArrayList<TimeZone> timeZones = new ArrayList<TimeZone>();
        for(Entity result : pq.asIterable())
            timeZones.add(new TimeZone(result));
        return timeZones;
    }
}
