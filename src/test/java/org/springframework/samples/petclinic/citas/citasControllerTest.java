/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.citas;


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
@WebMvcTest(citasController.class)
public class citasControllerTest {
    private static final int TEST_OWNER_ID = 3;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private citaRepository citas;    
    @MockBean
    private CitasReportRepository citasReport;
  
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private cita cita;
    
    @Before
    public void setup() {
        cita = new cita();
        cita.setNombre("Juancarlas");
        cita.setEspecialidad("surgery");
        cita.setMascota("Lucky");
        cita.setFecha("2019-12-22");
        cita.setConfirmado("Confirmado");   
        cita.setId(TEST_OWNER_ID);
        given(this.citas.findById(TEST_OWNER_ID)).willReturn(cita);       
    } 
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testShowCitasList() throws Exception {
        mockMvc.perform(get("/citas"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("citas"))
            .andExpect(view().name("citas/citasList"));
    }
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testShowCitasListEdit() throws Exception {
        mockMvc.perform(get("/citas/edit"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("citas"))
            .andExpect(view().name("citas/citasListMaster"));
    }
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/citas/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("citas"))
            .andExpect(view().name("citas/citasAdd"));
    }
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testProcessCreationForm() throws Exception{
        mockMvc.perform(post("/citas/new")
            .param("nombre", "Juancarlas")
            .param("fecha", "2019-12-22")
            .param("mascota", "Lucky")
            .param("especialidad", "surgery")
            .param("confirmado", "Confirmado")
        )
            .andExpect(status().is3xxRedirection());
    }
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void itHasErrorTestProcessCreationForm() throws Exception{
        mockMvc.perform(post("/citas/new")
            .param("nombre", "")
            .param("fecha", "")
            .param("mascota", "")
            .param("especialidad","" )
            .param("confirmado", "")
        )
            .andExpect(status().is3xxRedirection());
            //.andExpect(model().attributeHasErrors("status"))
            //.andExpect(model().attributeHasFieldErrors("status"))
            //.andExpect(view().name("citas/citasAdd"));
    }
    @WithMockUser (username = "admin", roles={"1s"})
    @Test
    public void initCreationForm2() throws Exception {
        mockMvc.perform(get("/citas/newMaster"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("cita"))
            .andExpect(view().name("citas/citasAddMaster"));
    }
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void processCreationFormMaster() throws Exception{
        mockMvc.perform(post("/citas/newMaster")
            .param("nombre", "Juancarlas")
            .param("fecha", "2019-12-22")
            .param("mascota", "Lucky")
            .param("especialidad", "surgery")
            .param("confirmado", "Confirmado")
        )
            .andExpect(status().is3xxRedirection());
    }
    
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testInitUpdateVetForm() throws Exception {
        mockMvc.perform(get("/citas/edit/{citaId}",TEST_OWNER_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("cita"))
            .andExpect(view().name("citas/citasAddMaster"));
    }
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testInitCreateReportCitas() throws Exception {
        mockMvc.perform(get("/citas/report"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("citas"))
            .andExpect(view().name("citas/citasReport"));
    }
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testProcessUpdateOwnerForm() throws Exception {
        mockMvc.perform(post("/citas/edit/{citaId}", TEST_OWNER_ID)
            .param("Nombre", "Juancarlas")
            .param("Fecha", "2019-12-22")
            .param("Estatus", "Sin confirmar")
            .param("Especialidad", "dentistry")
            .param("Accion", "Crear cita")
        )
            .andExpect(status().is3xxRedirection());
    }
    /*@WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/medicament/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicament"))
            .andExpect(view().name("medicaments/createOrUpdateMedicamentForm"));
    }
    
    @WithMockUser (username = "admin", roles={"1"})
    @Test
    public void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(
            post("/medicament/new")
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
    public void testProcessCreationFormHasErrors() throws Exception {
        mockMvc.perform(post("/medicament/new")
            .param("nombre", "Petximo")
            .param("ingredientes", "12")            
            .param("presentacion", "Pastillas")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("medicament"))
            .andExpect(model().attributeHasFieldErrors("medicament", "ingredientes"))
            .andExpect(view().name("medicaments/createOrUpdateMedicamentForm"));
    }*/
    
}

