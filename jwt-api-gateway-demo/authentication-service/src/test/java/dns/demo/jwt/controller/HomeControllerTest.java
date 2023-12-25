package dns.demo.jwt.controller;

import dns.demo.jwt.config.SecurityConfig;
import dns.demo.jwt.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.WWW_AUTHENTICATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({HomeController.class, AuthController.class})
@Import({SecurityConfig.class, TokenService.class})
class HomeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {
        this.mvc.perform(get("/")
                        .header("Authorization", "Bearer " + getJwtToken()))
                .andExpect(content().string("Hello dnunez!!"));
    }

    @Test
    void preAuthorizedEndpointSuccess() throws Exception {
        this.mvc.perform(get("/secured-read")
                        .header("Authorization", "Bearer " + getJwtToken()))
                .andExpect(content().string("This is a secured endpoint with read authority!!"));
    }

    @Test
    void preAuthorizedEndpointFailsForMissingAuthority() throws Exception {
        this.mvc.perform(get("/secured-write")
                        .header("Authorization", "Bearer " + getJwtToken()))
                .andExpect(status().isForbidden())
                .andExpect(header().string(WWW_AUTHENTICATE, containsString("Bearer error=\"insufficient_scope\"")));
    }

    private String getJwtToken() throws Exception {
        MvcResult result = this.mvc.perform(post("/token")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content("{ \"username\": \"dnunez\", \"password\": \"pass\" }"))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    @Test
    @WithMockUser
    public void rootWithMockUserStatusIsOK() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}