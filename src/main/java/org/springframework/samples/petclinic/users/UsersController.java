/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.users;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Owner_report;
import org.springframework.samples.petclinic.owner.Owner_report_repo;
import org.springframework.samples.petclinic.profiles.Profile;
import org.springframework.samples.petclinic.profiles.ProfileRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author pablo
 */
@Controller
public class UsersController {
	private final UserReposiroty users;
	private final User_reportRepository reportes;
	private Owner ownerMomentaneo;
	private final UserRepository ur;
	private Owner_report ownerRMomentaneo;
	private static final String VIEWS_USERS_CREATE_OR_UPDATE_FORM = "users/newUser";
	private final OwnerRepository owners;
	private final Owner_report_repo or;
        private final ProfileRepository pr;
    
	
	public UsersController(UserReposiroty clinicService, User_reportRepository ur, 
		OwnerRepository owners, Owner_report_repo or, UserRepository usr, ProfileRepository pr){
		this.users = clinicService;
		this.reportes = ur;
		this.owners = owners;
		this.or =or;
		this.ur = usr;
                this.pr =pr;
	}
	   
	@GetMapping("/users")
	public String getUsers(Map<String, Object> model) {
                Users_List ul = new Users_List();
		ul.getUserList().addAll(this.users.findAll());
                //Collection<User> results = this.users.findAll();
                
		model.put("users", ul);
		return "users/userList";	
	}
	@GetMapping("/users/report")
	public String getUsersReport(Map<String, Object> model) {
		//System.out.println("Im in");
		//Users users = new Users();
		Users_Reports usr = new Users_Reports();
		//users.getUserList().addAll( this.users.findAll());
		usr.getUserList().addAll(this.reportes.findAll());
                //Collection<User_report> results = this.reportes.findAll();
		model.put("users", usr);
                 
		
		//System.out.println("R: " + results.get(0).getCorreo());
		return "users/userReport";	
	}
	
	@GetMapping("/users/edit/{userId}")
	public String initUpdateVetForm(@PathVariable("userId") int userId, Model model) {
	    User user = this.users.findById(userId);
	    System.out.println("User: " + user.getId());
	    model.addAttribute(user);
	    return VIEWS_USERS_CREATE_OR_UPDATE_FORM;
	}
	@PostMapping("/users/edit/{userId}")
	public String processUpdateOwnerForm(@Valid User user, BindingResult result, @PathVariable("userId") int userId) {
		User usuario;
		//System.out.println("ROL: " + user.getRol());
		usuario = this.ur.findByNombre(user.getNombre());
		System.out.println("ROL: " + usuario.getRol());
	    if (result.hasErrors()) {
		return "users/userList";
	    } else {
		user.setId(userId);
		Date fecha = new Date();	
		User_report ur = new User_report();
		ur.setNombre_usuario(user.getNombre());
		ur.setAccion("Fue Editado");
		ur.setFecha(fecha.toString());
                user.setRol(usuario.getRol());
		String temp=hashPassword(user.getPassword());
                user.setPassword(temp);
		this.users.save(user);
		
		System.out.println("USEEEER" + user.getRol());
		if(user.getRol() == 1){
			ur.setRol("Empleado");
			this.reportes.save(ur);
			return "redirect:/users";
			
		}else{
			ur.setRol("Owner");
			this.reportes.save(ur);
			return "redirect:/profiles/profile/" + user.getId();
		}
		
	    }
	}
	
	@GetMapping("/users/delete/{userId}")
	public String deleteVet(User user, BindingResult result, Map<String, Object> model,@PathVariable("userId") int userId) {
		Date fecha = new Date();	
		User_report ur = new User_report();
		User u = new User();
		u = users.findById(userId);
		System.out.println("User Eliminado" + u.getNombre());
		ur.setNombre_usuario(u.getNombre());
		ur.setAccion("Fue eliminado");
		ur.setFecha(fecha.toString());
		this.users.delete(this.users.findById(userId));
		this.reportes.save(ur);
		Collection<User> results = this.users.findByName("");
		model.put("selections", result);
		if(user.getRol() == 1){
			ur.setRol("Empleado");
			this.reportes.save(ur);
			
		}else{
			ur.setRol("Owner");
			this.reportes.save(ur);
		}
		
		return "redirect:/users";       
	}
	
	 @GetMapping("/users/new")
	public String addUsers(Map<String, Object> model) {
		User user = new User();
                
		model.put("user",user);
		//List<User> results = (List<User>) this.users.findAll();
		//System.out.println("R: " + results);
		return VIEWS_USERS_CREATE_OR_UPDATE_FORM;
	}
	 @GetMapping("/usersOwners/new")
	public String addUsersOwners(Map<String, Object> model) {
		User user = new User();
                
		model.put("user",user);
		//List<User> results = (List<User>) this.users.findAll();
		//System.out.println("R: " + results);
		return "users/newUserOwner"; 
	}
	@PostMapping("/usersOwners/new")
	public String postUserOwners(@Valid User user, BindingResult result){
		user.setNombre(this.ownerMomentaneo.getFirstName() + " " +this.ownerMomentaneo.getLastName());
		//user.setNombre("Plebo");
                Profile profile = new Profile();
                User usuario;
                Owner propietario;
		CodigoPostal codigoPostal = new CodigoPostal();
		UserWebService webService = new UserWebService();
		Date fecha = new Date();	
		User_report ur = new User_report();
		webService.setWebService(user.getCp());
		codigoPostal = webService.getCodigoPostal();
		user.setActivo("0");
		if(result.hasErrors()) {
			return "users/newUserOwner";
		} else {
			System.out.println("User: " +user.getMunicipio() + " WS: " +
				codigoPostal.getMunicipio());
			if(user.getMunicipio().equals(codigoPostal.getMunicipio())){
				System.out.println("Entré");
				ur.setNombre_usuario(user.getNombre());
				ur.setAccion("Fue creado");
				ur.setRol("Owner");
				ur.setFecha(fecha.toString());
                                String temp=hashPassword(user.getPassword());
                                user.setPassword(temp);
                                user.setRol( (byte) 2);
				//this.userMomentaneo = user;
				//this.userRMomentaneo = ur;
				this.users.save(user);
                                usuario = this.ur.findByNombre(user.getNombre());
				profile.setId_usuario(usuario.getId());
				this.reportes.save(ur);
				this.owners.save(this.ownerMomentaneo);
                                //this.owners.save(this.ownerMomentaneo);
				propietario = this.owners.findByNombre(this.ownerMomentaneo.getFirstName());
				profile.setId_owner(propietario.getId());
				profile.setFoto("1.png");
                                System.out.println("PROFILE:::::: " + profile);
				this.pr.save(profile);
				//this.or.save(this.ownerRMomentaneo);
                                this.or.save(this.ownerRMomentaneo);
				return "redirect:/login";
			}else{
				result.rejectValue("cp", "errorCP");
				return "/usersOwners/new";
			}
		}
	}
	@GetMapping("/users/OwnersUsersnew")
    public String initCreationForm(Map<String, Object> model) {
        Owner owner = new Owner();
        model.put("owner", owner);
        return "owners/newOwnerUser";
    }
    
        @PostMapping("/users/OwnersUsersnew")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
	    
        Date fecha = new Date();	
	Owner_report ors = new Owner_report();
        if (result.hasErrors()) {
		return "owners/newOwnerUser";
        } else {
		System.out.println("IM in");
		ors.setOwner_name(owner.getFirstName());
		ors.setAccion("Creado");
		ors.setFecha(fecha.toString());
		this.ownerMomentaneo = owner;
		this.ownerRMomentaneo = ors;
		return "redirect:/usersOwners/new";
        }
    }
	 @PostMapping("/users/new")
	public String proccessCreationForm(@Valid User user, BindingResult result){
		CodigoPostal codigoPostal = new CodigoPostal();
		UserWebService webService = new UserWebService();
		Date fecha = new Date();	
		User_report ur = new User_report();
		webService.setWebService(user.getCp());
		codigoPostal = webService.getCodigoPostal();

		if(result.hasErrors()) {
			return VIEWS_USERS_CREATE_OR_UPDATE_FORM;
		} else {
			System.out.println("User: " +user.getMunicipio() + " WS: " +
				codigoPostal.getMunicipio());
			if(user.getMunicipio().equals(codigoPostal.getMunicipio())){
				ur.setNombre_usuario(user.getNombre());
				ur.setAccion("Fue creado");
				ur.setFecha(fecha.toString());
				ur.setRol("Empleado");
				
                                String temp=hashPassword(user.getPassword());
                                user.setPassword(temp);
                                user.setRol( (byte) 1);
				this.users.save(user);
				this.reportes.save(ur);
				return "redirect:/users";
			}else{
				result.rejectValue("cp", "errorCP");
				return VIEWS_USERS_CREATE_OR_UPDATE_FORM;
			}
		}
    }

	
	public static String hashPassword(String password_plaintext) {
                PasswordEncoder encode = new BCryptPasswordEncoder();
                String newPassword = encode.encode(password_plaintext);
                return newPassword;
		//String salt = BCrypt.gensalt(10);
		//String hashed_password = BCrypt.hashpw(password_plaintext, salt);

		//return(hashed_password);
	}
}
