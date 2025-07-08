package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.*;

public interface MedcinRepository extends JpaRepository<Medcin, Integer>{
	public Optional<Medcin> findByEmail(String email);
	
}
