/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.profiles;


import org.springframework.samples.petclinic.citas.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.users.User;
import org.springframework.samples.petclinic.users.UserReposiroty;
import org.springframework.samples.petclinic.users.User_reportRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
/**
 *
 * @author Jose Pablo
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ProfileController.class)
public class profilesControllerTest {
    private static final int TEST_OWNER_ID = 19;

    @Autowired
    private MockMvc mockMvc;    
    @MockBean
    private ProfileRepository profileRepository; 
//    @MockBean
//    private UserReposiroty ur; 
    @MockBean
    private OwnerRepository or; 
    @MockBean
    private UserReposiroty users; 
    @MockBean
    private User_reportRepository reportes; 
 
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private Profile profile;
    private User user;
    private Owner owner;
    @Before
    public void setup() {
        profile = new Profile();
        profile.setId_usuario(19);
        profile.setId_owner(14);
        profile.setFoto("1.png");
        user = new User();
        user.setNombre("Pablo");
        user.setId(14);
        owner = new Owner();
        owner.setId(19);
        owner.setFirstName("PabloOwner");

        given(this.profileRepository.findById(TEST_OWNER_ID)).willReturn(profile);       
        given(this.users.findById(19)).willReturn(user);       
        given(this.or.findById(14)).willReturn(owner);       
    } 
    
    @WithMockUser (username = "admin", roles={"2"})
    @Test
    public void testGetProfile() throws Exception {
        mockMvc.perform(get("/profiles/profile/{userId}", TEST_OWNER_ID))
          //  .andExpect(status().isOk())
            .andExpect(model().attributeExists("user"))
//          .andExpect(model().attribute("user", hasProperty("nombre", is("Pablo"))))
            .andExpect(model().attributeExists("profile"))
//          .andExpect(model().attribute("profile", hasProperty("foto", is("1.png"))))              
//            .andExpect(model().attributeExists("user"))
//            .andExpect(model().attributeExists("cita"))
            .andExpect(status().isOk());

    }
    
    //@WithMockUser (username = "admin", roles={"0"})
    @Test
    public void testPostPhoto() throws Exception {
        mockMvc.perform(post("/profiles/profile/{userId}", TEST_OWNER_ID)
            .param("id_usuario", "19")
            .param("id_owner", "14")
            .param("foto", "1.png")
        )
            .andExpect(status().is3xxRedirection());
    }
} 