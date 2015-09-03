package apollo.datastore.tests;

import static org.junit.Assert.*;

import apollo.datastore.TimeZone;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.*;

public class TimeZoneTest {

    private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void setUp() {
        helper.setUp();
    }

    @AfterClass
    public static void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testConstructor() {
        String timeZoneId = "america_chicago";
        long offset = -21600;
        String localeId = "America/Chicago";
        TimeZone timeZone = new TimeZone(timeZoneId, offset, localeId);
        Key key = timeZone.getKey();
        assertEquals(key.getKind(), TimeZone.DatastoreProperties.KIND.getName());
        assertEquals(key.getName(), timeZoneId);
        assertEquals(timeZone.getTimeZoneId(), timeZoneId);
        assertEquals(timeZone.getOffset(), offset);
        assertEquals(timeZone.getLocaleId(), localeId);
    }

    @Test
    public void testConstructorFromEntity() {
        String timeZoneId = "america_chicago";
        long offset = -21600;
        String localeId = "America/Chicago";
        TimeZone timeZoneDummy = new TimeZone(timeZoneId, offset, localeId);
        TimeZone timeZone = new TimeZone(timeZoneDummy.getEntity());
        Key key = timeZone.getKey();
        assertEquals(key.getKind(), TimeZone.DatastoreProperties.KIND.getName());
        assertEquals(key.getName(), timeZoneId);
        assertEquals(timeZone.getTimeZoneId(), timeZoneId);
        assertEquals(timeZone.getOffset(), offset);
        assertEquals(timeZone.getLocaleId(), localeId);
    }
}
