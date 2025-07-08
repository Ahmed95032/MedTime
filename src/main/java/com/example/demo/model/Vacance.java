package com.example.demo.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Vacance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private LocalDate dateDebut;
	private LocalDate dateFin;
	
	@ManyToOne
	@JoinColumn(name="medcine_id")
	private Medcin medcin;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDate getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}
	
	public LocalDate getDateFin() {
		return dateFin;
	}
	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}
	public Medcin getMedcin() {
		return medcin;
	}
	public void setMedcin(Medcin medcin) {
		this.medcin = medcin;
	}
	@Override
	public String toString() {
		return "Vacance [id=" + id + ", dateDebut=" + dateDebut + ", datFin=" + dateFin + ", medcin=" + medcin
				+  "]";
	}
	
	
}
