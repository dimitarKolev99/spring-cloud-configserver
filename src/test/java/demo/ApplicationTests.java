package demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    @Value("${local.server.port}")
    private int port = 0;

    @Autowired
    private RestTestClient restTestClient;

    @Test
    public void configurationAvailable() {
        var res = restTestClient.get()
                .uri("http://localhost:" + port + "/app/cloud")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Map.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(res);
        assertTrue(res.containsKey("propertySources"));

        var propertySources = (List<?>) res.get("propertySources");
        assertFalse(propertySources.isEmpty());
    }

    @Test
    public void envGetAvailable() {
        restTestClient.get()
                .uri("http://localhost:" + port + "/admin/env")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

}
