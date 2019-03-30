package org.springframework.samples.petclinic.system;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for {@link CrashController}
 *
 * @author Colin But
 */
@RunWith(SpringRunner.class)
// Waiting https://github.com/spring-projects/spring-boot/issues/5574
@WebMvcTest(LoginController.class)
public class LoginControllerTests {

    @Autowired
    private MockMvc mockMvc;

    
    //@WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            //.andExpect(model().attributeExists("errorMessge"))
            .andExpect(view().name("/login"));
    }
    //@WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testLogoutPage() throws Exception {
        mockMvc.perform(get("/logout"))
            .andExpect(status().is3xxRedirection());
            //.andExpect(model().attributeExists("errorMessge"))
            //.andExpect(view().name("/login"));
    }
}
