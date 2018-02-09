import com.paybycar.site.API;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class TestAPI {
    @Test
    public void TestAPI() throws IOException, JSONException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        API api = new API("https://paybycar-qa.nowintelligence.com");
        api.post(1,"12341234");
    }
}
