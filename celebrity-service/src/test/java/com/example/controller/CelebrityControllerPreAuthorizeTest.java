package com.example.controller;

import com.example.config.SpringProfile;
import com.example.config.TestOAuth2JwtConfig;
import com.example.config.oauth2mock.WithMockOAuth2Jwt;
import com.example.config.security.ResourceServerConfiguration;
import com.example.config.security.oauth2.AccessTokenConverterConfig;
import com.example.config.security.oauth2.TokenStoreConfig;
import com.example.entity.Celebrity;
import com.example.repository.CelebrityRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles(SpringProfile.PROFILE_TEST)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        TestOAuth2JwtConfig.class, TokenStoreConfig.class, AccessTokenConverterConfig.class, CelebrityController.class,
        ResourceServerConfiguration.class
})
public class CelebrityControllerPreAuthorizeTest {

    @MockBean
    private CelebrityRepository repository;

    private CelebrityController controller;

    @Autowired
    public void setController(CelebrityController controller) {
        this.controller = controller;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // todo: tokenStore bean created only on secured profile
    @Ignore
    @Test
    @WithMockOAuth2Jwt(roles = "ROLE_ADMIN")
    public void test() {
        Celebrity celebrity = controller.randomCelebrityWithTestProperty();
        Assert.assertNotNull(celebrity);
    }

    @TestConfiguration
    @EnableWebSecurity
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { // @formatter:off
            auth
                    .inMemoryAuthentication()
                    .withUser("user").password("pass").roles("USER")
                    .and()
                    .withUser("admin").password("admin").roles("ADMIN");
        } // @formatter:on
    }

}
