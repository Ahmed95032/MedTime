package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class RendezVous {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JsonFormat(pattern="dd-MM-yyyy")
	private LocalDate date;
	@JsonFormat(pattern="HH:mm")
	private LocalTime time;
	private String role;
	private LocalTime dureeS; // Durée de la science en minutes
	private String nomPatient;
	private String prenomPatient;
	private String telephonePatient;
	private String nCarteNationale;
	
	@ManyToOne
	@JoinColumn(name="medcine_id")
	private Medcin medcin;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalTime getDureeS() {
		return dureeS;
	}

	public void setDureeS(LocalTime dureeS) {
		this.dureeS = dureeS;
	}

	public Medcin getMedcin() {
		return medcin;
	}

	public void setMedcin(Medcin medcin) {
		this.medcin = medcin;
	}
	

	public String getNomPatient() {
		return nomPatient;
	}

	public void setNomPatient(String nomPatient) {
		this.nomPatient = nomPatient;
	}

	public String getPrenomPatient() {
		return prenomPatient;
	}

	public void setPrenomPatient(String prenomPatient) {
		this.prenomPatient = prenomPatient;
	}

	public String getTelephonePatient() {
		return telephonePatient;
	}

	public void setTelephonePatient(String telephonePatient) {
		this.telephonePatient = telephonePatient;
	}

	public String getnCarteNationale() {
		return nCarteNationale;
	}

	public void setnCarteNationale(String nCarteNationale) {
		this.nCarteNationale = nCarteNationale;
	}

	@Override
	public String toString() {
		return "RendezVous [id=" + id + ", date=" + date + ", time=" + time + ", role=" + role + ", dureeS=" + dureeS
				+ ", nomPatient=" + nomPatient + ", prenomPatient=" + prenomPatient + ", telephonePatient="
				+ telephonePatient + ", nCarteNationale=" + nCarteNationale + ", medcin=" + medcin + "]";
	}

	
	
	
}
