package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.*;

public interface RendezVousRepository extends JpaRepository<RendezVous, Integer>{
	    public List<RendezVous> findByDate(LocalDate date);
	    public List<RendezVous> findByMedcinId(Integer id);
	    public RendezVous findByNCarteNationale(String nCarteNationale); 
	    public RendezVous findByDateAndTimeAndMedcinAndRole(LocalDate date, LocalTime time, Medcin medcin,String role);
	    public RendezVous findByTelephonePatient(String telephonePatient); 
	    public List<RendezVous> findByTelephonePatientAndNCarteNationaleAndRole(String telephonePatient, String nCarteNationale,String role); 
	    public List<RendezVous> findByRole(String role);
	
}
