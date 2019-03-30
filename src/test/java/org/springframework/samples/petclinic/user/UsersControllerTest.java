/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.user;


import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Owner_report_repo;
import org.springframework.samples.petclinic.profiles.ProfileRepository;
import org.springframework.samples.petclinic.users.User;
import org.springframework.samples.petclinic.users.UserReposiroty;
import org.springframework.samples.petclinic.users.User_reportRepository;
import org.springframework.samples.petclinic.users.UsersController;
import org.springframework.samples.petclinic.users.Users_Reports;
import org.springframework.samples.petclinic.users.userReportsRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
/**
 *
 * @author Jose Pablo
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
public class UsersControllerTest {
    private static final int TEST_USER_ID = 21;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserReposiroty users;

    @MockBean
    private User_reportRepository usersReportRepository;
   
    @MockBean
    private userReportsRepository loginReportsRepository;
    @MockBean
    private OwnerRepository owners;
    @MockBean
    private Owner_report_repo or;
    @MockBean
    private ProfileRepository pr;
    
    
    private User user;
    private Users_Reports userReport;
    
    @Before
    public void setup() {
        userReport = new Users_Reports();
        user = new User();
        user.setNombre("Plebo");
        user.setActivo("1");
        user.setCorreo("fabricio");
        user.setCp("29240");
        user.setMunicipio("San Cristóbal de las Casas");
        user.setPassword("$2a$10$EgNcl3AbYtujJ.xc9jW9yenctOistL/V9Xkow36eIsUoYYuAR5bEG");
        user.setRol((byte) 1);
        user.setId(TEST_USER_ID);
        given(this.users.findById(TEST_USER_ID)).willReturn(user);             
        given(this.users.findByNombre("Pablito")).willReturn(user);       
  
    } 
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testCreationUserGET() throws Exception {
        mockMvc.perform(get("/users/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("user"))
            .andExpect(view().name("users/newUser"));
    }
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testCreacionUserPOST() throws Exception{
             mockMvc.perform(post("/users/new")
            .param("nombre", "Joe")
            .param("activo", "1")
            .param("correo", "fabricio")
            .param("cp", "292140")
            .param("municipio", "San Cristóbal de las Casas")
            .param("password", "$2a$10$EgNcl3AbYtujJ.xc9jW9yenctOistL/V9Xkow36eIsUoYYuAR5bEG")
            .param("rol", "1")
        
        )
            .andExpect(view().name("users/newUser"));//is3xxRedirection());
    }
    
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void ListUsers() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(view().name("users/userList"));
    }
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
     public void testCreationUserReportGET() throws Exception {
        mockMvc.perform(get("/users/report"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("users"))
            .andExpect(view().name("users/userReport"));
    }
     
     @WithMockUser (username = "admin", roles={"1"})
    @Test
     public void testEditUserGET() throws Exception{
          mockMvc.perform(get("/users/edit/{userId}", TEST_USER_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attribute("user", hasProperty("nombre", is("Plebo"))))
            .andExpect(model().attribute("user", hasProperty("activo", is("1"))))
            .andExpect(model().attribute("user", hasProperty("correo", is("fabricio"))))
            .andExpect(model().attribute("user", hasProperty("cp", is("29240"))))
            .andExpect(model().attribute("user", hasProperty("municipio", is("San Cristóbal de las Casas"))))
            .andExpect(model().attribute("user", hasProperty("password", is("$2a$10$EgNcl3AbYtujJ.xc9jW9yenctOistL/V9Xkow36eIsUoYYuAR5bEG")))) 
            //.andExpect(model().attribute("user", hasProperty("rol", is("1"))))      
            .andExpect(view().name("users/newUser"));
     }
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
     public void testEditUserPOST() throws Exception{
         mockMvc.perform(post("/users/edit/{userId}", TEST_USER_ID)
         .param("nombre", "Pablito")
         .param("activo", "0")
         .param("correo", "Pablito")        
         .param("cp", "29240")
         .param("municipio", "San Cristobal de las Casas")
         .param("password", "$2a$10$EgNcl3AbYtujJ.xc9jW9yenctOistL/V9Xkow36eIsUoYYuAR5bEG")
         .param("rol", "1")
         ).andExpect(status().is3xxRedirection());
     }
 
    
 

     
    @WithMockUser (username = "admin", roles={"1"})
    @Test
     public void testCreationUserOwnerGET() throws Exception {
        mockMvc.perform(get("/usersOwners/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("user"))
            .andExpect(view().name("users/newUserOwner"));
    } 
     
    public void testCreacionUserOwnerPOST() throws Exception{
             mockMvc.perform(post("/usersOwners/new")
            .param("nombre", "Joe")
            .param("activo", "1")
            .param("correo", "fabricio")
            .param("cp", "292140")
            .param("municipio", "San Cristóbal de las Casas")
            .param("password", "$2a$10$EgNcl3AbYtujJ.xc9jW9yenctOistL/V9Xkow36eIsUoYYuAR5bEG")
            .param("rol", "1")
            .param("firstName", "Joe")
            .param("lastName", "Bloggs")
            .param("address", "123 Caramel Street")
            .param("city", "London")
            .param("telephone", "01616291589")
            .param("latitud", "90")
            .param("longintud", "0")
        )
            .andExpect(view().name("/usersOwners/new"));//is3xxRedirection());
    } 
    
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
     public void testCreationOwnerUserGET() throws Exception {
        mockMvc.perform(get("/users/OwnersUsersnew"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("owner"))
            .andExpect(view().name("owners/newOwnerUser"));
    } 
     
     @Test
     public void testCreacionOwnerUserPOST() throws Exception{
             mockMvc.perform(post("/users/OwnersUsersnew")
            
            .param("firstName", "Joe")
            .param("lastName", "Bloggs")
            .param("address", "123 Caramel Street")
            .param("city", "London")
            .param("telephone", "01616291589")
            .param("latitud", "90")
            .param("longintud", "0")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/usersOwners/new"));//is3xxRedirection());
    }
     
}