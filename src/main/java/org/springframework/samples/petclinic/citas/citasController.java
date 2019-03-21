/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.citas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author theco
 */
@Controller
class citasController{
    
    private final citaRepository citas;    
    private final CitasReportRepository citasReport;

    private static final String VIEWS_CITAS_CREATE_OR_UPDATE_FORM = "citas/citasAdd";
    private static final String VIEWS_CITAS_CREATE_OR_UPDATE_FORM2 = "citas/citasAddMaster";

    public citasController(citaRepository clinicService, CitasReportRepository citasReport) {
        this.citas = clinicService;
        this.citasReport = citasReport;
    }
    
    @GetMapping("/citas")
    public String showCitasList(Map<String, Object> model) {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for Object-Xml mapping
        Citas citas = new Citas();                
        citas.getCitasList().addAll(this.citas.findAll());
        model.put("citas", citas);
        return "citas/citasList";
    }
    
    @GetMapping("/citas/edit")
    public String showCitasListEdit(Map<String, Object> model) {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for Object-Xml mapping
        Citas citas = new Citas();                
        citas.getCitasList().addAll(this.citas.findAll());
        model.put("citas", citas);
        return "citas/citasListMaster";
    }
    
    @ModelAttribute("status")
    public List<String> populatSpecialty() {
        List<String> estatuses = new ArrayList<String>();
        estatuses.add("Sin Confirmar");
        estatuses.add("Confirmado");
        return estatuses;
    }
    
    @ModelAttribute("specialties")
    public Collection<Specialty> populatSpecialty2() {
        return this.citas.findSpecialityTypes();
    }
    
    @ModelAttribute("pets")
    public Collection<Pet> populatSpecialty3() {
        return this.citas.findSpecialityTypesPets();
    }
    
        
    @GetMapping("/citas/new")
    public String initCreationForm(Map<String, Object> model) {
        cita cita = new cita();        
        model.put("citas", cita);        
        return "citas/citasAdd";
    }
    
    @PostMapping("/citas/new")
    public String processCreationForm(@Valid cita cita, BindingResult result) {
        cita.setConfirmado("Sin Confirmar");
        CitasReport report = new CitasReport();
        
        if (result.hasErrors()) {
            System.out.println("Ocurrió un error");
            return VIEWS_CITAS_CREATE_OR_UPDATE_FORM;
        } else {
            report.setEspecialidad(cita.getEspecialidad());
            report.setNombre(cita.getNombre());
            report.setStatus(cita.getConfirmado());
            report.setAccion("Crear cita");
            report.setFecha(LocalDate.now().toString());
            this.citasReport.save(report);
            this.citas.save(cita);            
            return "redirect:/citas";
        }
    }
    
    @GetMapping("/citas/newMaster")
    public String initCreationForm2(Map<String, Object> model) {
        cita cita = new cita();        
        model.put("cita", cita);        
        return "citas/citasAddMaster";
    }
    
    @PostMapping("/citas/newMaster")
    public String processCreationFormMaster(@Valid cita cita, BindingResult result) {
        cita.setConfirmado("Sin Confirmar");
        CitasReport report = new CitasReport();

        if (result.hasErrors()) {
            System.out.println("Ocurrió un error");
            return VIEWS_CITAS_CREATE_OR_UPDATE_FORM2;
        } else {
            report.setEspecialidad(cita.getEspecialidad());
            report.setNombre(cita.getNombre());
            report.setStatus(cita.getConfirmado());
            report.setAccion("Crear cita");
            report.setFecha(LocalDate.now().toString());
            this.citasReport.save(report);
            this.citas.save(cita);            
            return "redirect:/citas/edit";
        }
    }
    
    
    @GetMapping("/citas/edit/{citaId}")
    public String initUpdateVetForm(@PathVariable("citaId") int citaId, Model model) {
        System.out.println("LISTO");
        cita cita = this.citas.findById(citaId);
        model.addAttribute(cita);
        return VIEWS_CITAS_CREATE_OR_UPDATE_FORM2;
    }
    
    @GetMapping("/citas/report")
    public String initCreateReportCitas(Map<String, Object> model){
        CitasReportList citasReport = new CitasReportList();                
        citasReport.getCitasReportList().addAll(this.citasReport.findAll());
        model.put("citas", citasReport);
        return "citas/citasReport";
    }
    
    @PostMapping("/citas/edit/{citaId}")
    public String processUpdateOwnerForm(@Valid cita cita, BindingResult result, @PathVariable("citaId") int citaId){
        CitasReport report = new CitasReport();

        if (result.hasErrors()) {
            return VIEWS_CITAS_CREATE_OR_UPDATE_FORM2;
        } else {
            report.setEspecialidad(cita.getEspecialidad());
            report.setNombre(cita.getNombre());
            report.setStatus(cita.getConfirmado());
            report.setAccion("Modifico Cita");
            report.setFecha(LocalDate.now().toString());
            this.citasReport.save(report);
            System.out.println("LISTO2");
            cita.setId(citaId);
            this.citas.save(cita);
            return "redirect:/citas/edit";
        }
    }
    
}
