/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.profiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.users.CodigoPostal;
import org.springframework.samples.petclinic.users.User;
import org.springframework.samples.petclinic.users.UserReposiroty;
import org.springframework.samples.petclinic.users.UserWebService;
import org.springframework.samples.petclinic.users.User_report;
import org.springframework.samples.petclinic.users.User_reportRepository;
import static org.springframework.samples.petclinic.users.UsersController.hashPassword;
import org.springframework.samples.petclinic.users.Users_List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author pablo
 */
@Controller
public class ProfileController {
	private static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/resources/images/profiles/";
	ProfileRepository profileRepostory;
	Profile profileTemporal;
	UserReposiroty ur;
	OwnerRepository or;
	private final UserReposiroty users;
	private final User_reportRepository reportes;
	
	public ProfileController(ProfileRepository profileRepostory, UserReposiroty ur, OwnerRepository or, UserReposiroty users
	, User_reportRepository reportes){
		this.profileRepostory = profileRepostory;
		this.ur = ur;
		this.or = or;
		this.users = users;
		this.reportes = reportes;
	}
	
	
	@GetMapping("/profiles/profile/{userId}")
	public String getProfile(@PathVariable("userId") int userId, Model model) {
		//System.out.println("HI");
                Profile profile = profileRepostory.findById(userId);
		this.profileTemporal = profile;
		System.out.println("Profile: " + profile);
		User user = this.ur.findById(profile.getId_usuario());
		Owner owner = this.or.findById(profile.getId_owner());
		System.out.println("User: " + user);
		System.out.println("Owner: " + owner);
		
		model.addAttribute(user);
		//model.addAttribute(owner);
		model.addAttribute(profile);
		
		return "profiles/ownerProfile";	
            //model.addAttribute("msg", "Se logró copiar " + fileNames.toString());
        }
	
	@PostMapping("/profiles/profile/{userId}")
	public String postPhoto(@RequestParam("files") MultipartFile[]files, @PathVariable("userId") int userId){
		String nombre = null;
		StringBuilder fileNames = new StringBuilder();
		for(MultipartFile file: files){
			System.out.println("ARCHIVO: " + file.getOriginalFilename());
			Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
			nombre=file.getOriginalFilename();
			fileNames.append(file.getOriginalFilename());
			try{
			    Files.write(fileNameAndPath, file.getBytes());
			}catch(IOException e){
			    e.printStackTrace();
			}
			this.profileTemporal.setFoto(nombre);
		}
		System.out.println("PROFILE: " + this.profileTemporal);
		this.profileRepostory.save(this.profileTemporal);
		return "redirect:/profiles/profile/" + this.profileTemporal.getId_usuario();	
	} 
	
}
	
	

