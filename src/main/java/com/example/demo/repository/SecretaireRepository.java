package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.*;

public interface SecretaireRepository extends JpaRepository<Secretaire, Integer>{
	public Optional<Secretaire> findByEmail(String email);
}
