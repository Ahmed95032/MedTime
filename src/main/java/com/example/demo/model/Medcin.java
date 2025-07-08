package com.example.demo.model;

import java.time.LocalTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Medcin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String nom;
	private String prenom;
	private String email;
	private String password;
	private String telephone; 
	private LocalTime dureeM; // Durée de la science en minutes
	private LocalTime heurDebut;
	private LocalTime heurFin;
	private Byte jourDeTravaille;
	private String adresseDeCabinet;
	private String Specialite;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public LocalTime getDureeM() {
		return dureeM;
	}
	public void setDureeM(LocalTime dureeM) {
		this.dureeM = dureeM;
	}
	public LocalTime getHeurDebut() {
		return heurDebut;
	}
	public void setHeurDebut(LocalTime heurDebut) {
		this.heurDebut = heurDebut;
	}
	public LocalTime getHeurFin() {
		return heurFin;
	}
	public void setHeurFin(LocalTime heurFin) {
		this.heurFin = heurFin;
	}
	public Byte getJourDeTravaille() {
		return jourDeTravaille;
	}
	public void setJourDeTravaille(Byte jourDeTravaille) {
		this.jourDeTravaille = jourDeTravaille;
	}
	
	public String getAdresseDeCabinet() {
		return adresseDeCabinet;
	}
	public void setAdresseDeCabinet(String adresseDeCabinet) {
		this.adresseDeCabinet = adresseDeCabinet;
	}
	public String getSpecialite() {
		return Specialite;
	}
	public void setSpecialite(String specialite) {
		Specialite = specialite;
	}
	@Override
	public String toString() {
		return "Medcin [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", password="
				+ password + ", telephone=" + telephone + ", dureeM=" + dureeM + ", heurDebut=" + heurDebut
				+ ", heurFin=" + heurFin + ", jourDeTravaille=" + jourDeTravaille + ", adresseDeCabinet="
				+ adresseDeCabinet + ", Specialite=" + Specialite + "]";
	}
	
	
	
	
}
