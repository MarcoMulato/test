/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.medicament;


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
import org.springframework.samples.petclinic.users.userReportsRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
/**
 *
 * @author Jose Pablo
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MedicamentController.class)
public class MedicamentControllerTest {
    private static final int TEST_OWNER_ID = 4;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentRepository medicaments;

    @MockBean
    private Medicament_reportRepository medicamentsReportRepository;
   
    @MockBean
    private userReportsRepository userReportsRepository;
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private Medicament med;
    
    @Before
    public void setup() {
        med = new Medicament();
        med.setNombre("Paracetamol");
        med.setPresentacion("Pastillas");
        med.setIngredientes("Medicina");
        med.setId(4);
        given(this.medicaments.findById(TEST_OWNER_ID)).willReturn(med);       
    } 
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/medicament/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicament"))
            .andExpect(view().name("medicaments/createOrUpdateMedicamentForm"));
    }
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testProcessCreationFormSuccess() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file", new byte[1]);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/medicament/new", 1).file(file)
            .param("nombre", "Parace")
            .param("ingredientes", "huevitos")
            .param("presentacion", "Pastillas")
            .param("descripcion", "nada")
            .param("inventario", "12")
            .param("precio", "32")            
            .param("imagen", "hola.jpg")        
        )
            .andExpect(status().is3xxRedirection());
    }
    @WithMockUser (username = "admin", roles={"1"})    
    @Test
    public void testProcessCreationFormHasErrors() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file", new byte[1]);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/medicament/new", 1).file(file)
            .param("nombre", "Petximo")
            //.param("ingredientes", "huevitos")
            .param("presentacion", "Pastillas")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("medicament"))
            .andExpect(model().attributeHasFieldErrors("medicament", "ingredientes"))
            .andExpect(view().name("medicaments/createOrUpdateMedicamentForm"));
    }

    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testInitFindForm() throws Exception {
        mockMvc.perform(get("/medicament/find"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicament"))
            .andExpect(view().name("medicaments/findMedicaments"));
    }
    @WithMockUser (username = "admin", roles={"1"})    
    @Test
    public void testProcessFindFormSuccess() throws Exception {
        given(this.medicaments.findByName("")).willReturn(Lists.newArrayList(med, new Medicament()));
        mockMvc.perform(get("/medicaments"))
            .andExpect(status().isOk())
            .andExpect(view().name("medicaments/medicamentsList"));
    }
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testInitUpdateMedicamentForm() throws Exception {
        mockMvc.perform(get("/medicament/edit/{medId}", TEST_OWNER_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicament"))
            .andExpect(model().attribute("medicament", hasProperty("nombre", is("Paracetamol"))))
            .andExpect(model().attribute("medicament", hasProperty("ingredientes", is("Medicina"))))
            .andExpect(model().attribute("medicament", hasProperty("presentacion", is("Pastillas"))))
            .andExpect(view().name("medicaments/createOrUpdateMedicamentForm"));

    }
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testProcessUpdateMedicamentFormSuccess() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file", new byte[1]);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/medicament/edit/{medId}", 1).file(file)
            .param("nombre", "Petzin")
            .param("ingredientes", "Huevito :3")
            .param("presentacion", "en bolita")      
        )
//            .andExpect(status().is3xxRedirection());
            .andExpect(view().name("medicaments/createOrUpdateMedicamentForm"));


    }

    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testDeleteMedicament() throws Exception {
        mockMvc.perform(get("/medicament/delete/{medId}", TEST_OWNER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/medicaments"));
    }
}

