package integration.com.c4c.microserviceTool.infrastructure;

import com.c4c.microserviceTool.MicroServiceToolApplication;
import com.c4c.microserviceTool.dto.ProductDto;
import com.c4c.microserviceTool.service.ExcelParseService;
import com.c4c.microserviceTool.service.ExternalSystemAccessService;
import com.c4c.microserviceTool.service.utility.UtilityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MicroServiceToolApplication.class})
@AutoConfigureMockMvc
@WebAppConfiguration
public class ProductApiIntegrationTest  {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExternalSystemAccessService externalSystemAccessService;

    @Autowired
    private ExcelParseService excelParseService;

    @Autowired
    private WebApplicationContext context;

    private static final String HOST_SYS = "https://qt.coresystems.net/cloud-product-service";

    private static final String BASE_PATH = "/api/product/v1";

    private static final String BASE_PATH_CONFIG = "/api/product/v1/configuration";

    @Test
    public void setupTest() {
    }

//
//    @Before
//    public void setupMockMvc() throws Exception {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//    }

    @Test
    public void shouldGetProductByID() throws Exception {

        String excelFileUrl = "c:/Intel/ProductsData.xlsx";
        List<JSONObject> resultJsonList = excelParseService.parseExcelToJSONObject(excelFileUrl);
        List<String> dataContentList = new ArrayList<String>();
        if(!UtilityService.checkEmptyList(resultJsonList)){
            for(JSONObject jsonObject: resultJsonList) {
                if(jsonObject == null){
                    continue;
                }
                dataContentList.add(jsonObject.toString());
            }
            List<String> responseList = new ArrayList<>();
            for(String dataContent: dataContentList){
                int response = externalSystemAccessService.postData(dataContent, ExternalSystemAccessService.HOST_SB_CORESYS, ExternalSystemAccessService.ENDPOINT_PRODUCT);
                responseList.add("Code:" + response + "-" + dataContent);
            }
            if(responseList.size() > 0 ){
                for(String response:responseList){
                    String outContent = response.length() > 50? response.substring(0,50):response;
                    System.out.println(outContent);
                }
            }

        }
    }

}
