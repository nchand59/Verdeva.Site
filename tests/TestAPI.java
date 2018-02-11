import com.paybycar.site.API;
import org.junit.jupiter.api.Test;

public class TestAPI {
    @Test
    public void DetectionTest() throws Exception {
        API api = new API("https://paybycar-qa.nowintelligence.com", "9HZjgVV5/psugdewuoEfDeZLgrE7Pg9A8gJlngd/C5A2dJTVojz/Za60PYNgXd+p0ssuqfcGsjpQEAVRcPdi0hwKwTglqUUCa1Nde/6ClhOdD9Indn4Fw3O1z4QjYc1ROMAQ7uiSnBA0g9oic5vq9FFVTSeTTQchgDXoXzAXE0NpjHlffAKsaWZxtbH9D3l150+Dy98Gl4o9ZeE7nxh2u1R3PahAQtpABAKnB+SJirCfwRSdOtlERa1Da4E0OQnLpQ6rpyLaap9Xvto2+RDS0qdjHANZe382dGu3GVComjUBY7l6lfZjwG7wePUbcfNwG94RNaUmL5CibQv4NaWAFg==");
        api.post(1,"12341234");
    }

    @Test
    public void HearbeatTest() throws Exception {
        API api = new API("https://paybycar-qa.nowintelligence.com", "9HZjgVV5/psugdewuoEfDeZLgrE7Pg9A8gJlngd/C5A2dJTVojz/Za60PYNgXd+p0ssuqfcGsjpQEAVRcPdi0hwKwTglqUUCa1Nde/6ClhOdD9Indn4Fw3O1z4QjYc1ROMAQ7uiSnBA0g9oic5vq9FFVTSeTTQchgDXoXzAXE0NpjHlffAKsaWZxtbH9D3l150+Dy98Gl4o9ZeE7nxh2u1R3PahAQtpABAKnB+SJirCfwRSdOtlERa1Da4E0OQnLpQ6rpyLaap9Xvto2+RDS0qdjHANZe382dGu3GVComjUBY7l6lfZjwG7wePUbcfNwG94RNaUmL5CibQv4NaWAFg==");
        api.heartbeat(1, 1);
    }

    @Test
    public void HearbeatBadKeyTest() throws Exception {
        API api = new API("https://paybycar-qa.nowintelligence.com", "8HZjgVV5/psugdewuoEfDeZLgrE7Pg9A8gJlngd/C5A2dJTVojz/Za60PYNgXd+p0ssuqfcGsjpQEAVRcPdi0hwKwTglqUUCa1Nde/6ClhOdD9Indn4Fw3O1z4QjYc1ROMAQ7uiSnBA0g9oic5vq9FFVTSeTTQchgDXoXzAXE0NpjHlffAKsaWZxtbH9D3l150+Dy98Gl4o9ZeE7nxh2u1R3PahAQtpABAKnB+SJirCfwRSdOtlERa1Da4E0OQnLpQ6rpyLaap9Xvto2+RDS0qdjHANZe382dGu3GVComjUBY7l6lfZjwG7wePUbcfNwG94RNaUmL5CibQv4NaWAFg==");
        api.heartbeat(1, 1);
    }
}
