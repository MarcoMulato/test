/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.profiles;

import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pablo
 */
public interface ProfileRepository extends Repository<Profile, Integer> {
	@Query("SELECT profiles FROM Profile profiles WHERE profiles.id_usuario =:id")
	@Transactional(readOnly = true)
	Profile findById(@Param("id") Integer id);
	
	@Query("SELECT profiles FROM Profile profiles")
	@Transactional (readOnly= true)
	Collection<Profile> findAll();
	
	void save(Profile profile);
	

	
}
