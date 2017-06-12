package sk.miroc.whitebikes.utils;

import org.json.JSONException;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static sk.miroc.whitebikes.utils.networking.ReceivedCookiesInterceptor.*;

public class ReceivedCookiesInterceptor {
    @Test
    public void testCookieParsing() throws JSONException {
        assertThat(
                parseCookieWithValue("loguserid=771; expires=Mon, 26-Jun-2017 07:17:11 GMT; Max-Age=1209600"),
                is("loguserid=771"));

        assertThat(
                parseCookieWithValue("logsession=7620ce62e830a3170731f89626b1bfe13cbc86d2be876c3;"),
                is("logsession=7620ce62e830a3170731f89626b1bfe13cbc86d2be876c3"));
    }
}
