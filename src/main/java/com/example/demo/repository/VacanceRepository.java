package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Vacance;


public interface VacanceRepository extends JpaRepository<Vacance, Integer>{
	public List<Vacance> findByMedcinId(int id);
	public List<Vacance> findByDateFinGreaterThanEqual(LocalDate today);
}

