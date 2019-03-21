/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.citas;

import java.util.Collection;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author theco
 */
public interface citaRepository extends Repository<cita, Integer>{
        /**
     * Retrieve all <code>Vet</code>s from the data store.
     *
     * @return a <code>Collection</code> of <code>Vet</code>s
     */
    @Transactional(readOnly = true)   
    Collection<cita> findAll() throws DataAccessException;
    
    @Query("SELECT specialties FROM Specialty specialties ORDER BY specialties.name")
    @Transactional(readOnly = true)
    List<Specialty> findSpecialityTypes();
    
    @Query("SELECT pets FROM Pet pets")
    @Transactional(readOnly = true)
    List<Pet> findSpecialityTypesPets();
    
    @Query("SELECT cita FROM cita cita WHERE cita.id =:id")
    @Transactional(readOnly = true)
    cita findById(@Param("id") Integer id);
    
    void save(cita cita);

    
}
