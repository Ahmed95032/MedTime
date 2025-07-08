package com.example.demo.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login") 
public class LoginController {
	@Autowired private SecretaireRepository repoSec;  
	@Autowired private MedcinRepository repoMed;
	@Autowired private RendezVousRepository repoRend;
	
	@GetMapping("/Formlogin")
	 public String Formlogin() {
		 return "logIn";
	 }
	 
	//affichage les rendez pour une role donner 
	@ModelAttribute("allRendezVousPret")
	public List<RendezVous> getAlRendezVousPret() {
		List<RendezVous> liste=repoRend.findByRole("prêt");
		liste.sort(Comparator.comparing(RendezVous::getDate).thenComparing(RendezVous::getTime));
		  return liste;
	}	
	
	@ModelAttribute("allRendezVousTerminer")
	public List<RendezVous> getAllRendezVousTerminer() {
		List<RendezVous> liste=repoRend.findByRole("terminer");
		liste.sort(Comparator.comparing(RendezVous::getDate).thenComparing(RendezVous::getTime));
		  return liste;
	}
	@ModelAttribute("allRendezVousAnnuler")
	public List<RendezVous> getAllRendezVousAnnuler() {
		List<RendezVous> liste=repoRend.findByRole("annuler");
		liste.sort(Comparator.comparing(RendezVous::getDate).thenComparing(RendezVous::getTime));
		  return liste;
	}
	@ModelAttribute("allRendezVousAbsent")
	public List<RendezVous> getAllRendezVousAbsent() {
		List<RendezVous> liste=repoRend.findByRole("absent");
		liste.sort(Comparator.comparing(RendezVous::getDate).thenComparing(RendezVous::getTime));
		  return liste;
	}
	
	 //Verfification de login pour Medcin , Secretaire ou Cabinet 
	@PostMapping("/VerifierConnexion")
	public String VerifierConnexion(@RequestParam String email, @RequestParam String password, Model model,HttpSession session) { 
	     // Verfification de Medcin
	     Optional<Medcin> medcinOpt = repoMed.findByEmail(email);
	     if (medcinOpt.isPresent()) {
	         Medcin medcin = medcinOpt.get();
	         if (password.equals(medcin.getPassword())) {
	        	 session.setAttribute("user", medcin);
	             session.setAttribute("role", "medcin");
	             session.setAttribute("Nom", medcin.getNom());
	             return "redirect:/login/acceuil";
	         } else {
	             model.addAttribute("loginError", "Mot de passe incorrect (Medcin)");
	             return "logIn";
	         }
	     }

	     // Verfification de  Secretaire
	     Optional<Secretaire> secretaireOpt = repoSec.findByEmail(email);
	     if (secretaireOpt.isPresent()) {
	         Secretaire secretaire = secretaireOpt.get();
	         if (password.equals(secretaire.getPassword())) {
	        	 session.setAttribute("user", secretaire);
	             session.setAttribute("role", "secretaire");
	             session.setAttribute("Nom", secretaire.getNom());
	             return "redirect:/login/acceuil";
	         } else {
	             model.addAttribute("loginError", "Mot de passe incorrect (Secretaire)");
	             return "logIn";
	         }
	     }
	     
	     model.addAttribute("loginError", "Aucun utilisateur trouvé pour cet email");
	     return "logIn";
	 }
	 
	 @GetMapping("/acceuil")
	 public String accueil(HttpSession session) {
		 if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
			    return "redirect:/login/Formlogin";
			}
	     return "acceuil";
	 }

	 
	 @GetMapping("/logout")
	 public String logout(HttpSession session) {
	     session.invalidate(); 
	     return "redirect:/RendezVous"; 
	 }
}
