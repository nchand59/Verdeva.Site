import com.paybycar.site.API;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestAPI {
    @Test
    public void TestAPI() throws IOException, JSONException {
        API api = new API("https://paybycar-qa.nowintelligence.com");
        api.post(1,"12341234");
    }
}
