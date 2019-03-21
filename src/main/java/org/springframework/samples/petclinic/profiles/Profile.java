/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.profiles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.springframework.samples.petclinic.model.BaseEntity;

/**
 *
 * @author pablo
 */
@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity{
	@Column(name = "id_usuario")
	
	private int id_usuario;
	
	@Column(name = "id_owner")
	
	private int id_owner;
	
	@Column(name = "foto")
	@NotEmpty
	private String foto;


	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public int getId_owner() {
		return id_owner;
	}

	public void setId_owner(int id_owner) {
		this.id_owner = id_owner;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	@Override
	public String toString() {
		return "Profile{" + "id_usuario=" + id_usuario + ", id_owner=" + id_owner + ", foto=" + foto + '}';
	}
}
