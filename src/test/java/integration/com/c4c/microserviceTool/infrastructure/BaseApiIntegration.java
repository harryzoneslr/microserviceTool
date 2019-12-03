package integration.com.c4c.microserviceTool.infrastructure;

import com.c4c.microserviceTool.MicroServiceToolApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest(classes = {MicroServiceToolApplication.class})
public class BaseApiIntegration {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    protected ObjectMapper objectMapper;
    protected HttpHeaders commonHeaders;

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    @Before
    public void setupMockMvc() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();

        commonHeaders = new HttpHeaders();
        commonHeaders.set("X-Client-ID", "integration-tests");
        commonHeaders.set("X-Client-Version", "1.0");
        commonHeaders.set("X-Account-Name", "coresystems");
        commonHeaders.set("X-Company-Name", "coresystems");
        commonHeaders.set("X-User-Name", "i043125");
        commonHeaders.set("X-Account-ID", "1");
        commonHeaders.set("X-Company-ID", "1"); ;

        MockitoAnnotations.initMocks(this);
    }
}
