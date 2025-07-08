package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import jakarta.servlet.http.HttpSession;



//Controller
@Controller
@RequestMapping("/Vacances")
public class VacanceController {
	 @Autowired private VacanceRepository repoVac;
	 @Autowired private MedcinRepository repoMed;
	 
	//return all the Vacances 
	 @ModelAttribute("allVacances")
		public List<Vacance> getAllVacances() {
		    return repoVac.findAll();
	 }      
		 
	 @GetMapping("/listeVacance")
	 public String listeVacance(HttpSession session) {
		 if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
         return "vacance"; 
	 }
	 
	 @GetMapping("/FormAjouterVacance")
	 public String FormAjouterVacance(Model model,HttpSession session) {
		 if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
	     model.addAttribute("vacance", new Vacance());
	     return "ajouteVacance";
	 }
	 
	 @GetMapping("/FormModifierVacance/{id}")
	 public String FormModifierVacance(Model model, @PathVariable int id,RedirectAttributes redirectAttributes,HttpSession session) {
		 if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
	     Vacance vacance = repoVac.findById(id).orElse(null);
	     if(vacance != null) {
	         model.addAttribute("vacance", vacance); 
	         return "ajouteVacance"; 
	     }
	     redirectAttributes.addFlashAttribute("erreur", "aucun Vacance trouver pour ce id ");
	     return "redirect:/Vacances/listeVacance";
	 }
	 
	 @PostMapping("/saveVacance")
	 public String saveVacance(@ModelAttribute Vacance vacance, RedirectAttributes redirectAttributes,Model model) {
		 List<Medcin> medcins = repoMed.findAll();
		 if(medcins.isEmpty()) {
			 redirectAttributes.addFlashAttribute("erreur", "id de medcin non trouver ( vous voulez creer un medcin dans base donnes si aucun trouver  ) ");
             return "redirect:/Vacances/listeVacance";
		 }
	     if (vacance.getId() != 0) {
	         // Edit 
	         Vacance NVacance = repoVac.findById(vacance.getId()).orElse(null);
	         if (NVacance != null) {
	        	 if (vacance.getDateDebut().isAfter(vacance.getDateFin())) {
	        		 model.addAttribute("erreurVacanceDate", "La date de début doit être avant la date de fin.");   		 
	        	        return "ajouteVacance";
	        	 }
	             Medcin medcin = medcins.get(0);
	             NVacance.setMedcin(medcin);
	             NVacance.setDateDebut(vacance.getDateDebut());
	             NVacance.setDateFin(vacance.getDateFin());
	             repoVac.save(NVacance);
	             return "redirect:/Vacances/listeVacance";
	         } else {
	             redirectAttributes.addFlashAttribute("erreur", "aucun Vacance trouver pour ce id ");
	             return "redirect:/Vacances/listeVacance";
	         }
	     } else {
	         // Add
	    	 if (vacance.getDateDebut().isAfter(vacance.getDateFin())) {
	    	     model.addAttribute("erreurVacanceDate", "La date de début doit être avant la date de fin.");
	    	     return "ajouteVacance";
	    	 }  	 
    		 Medcin medcin = medcins.get(0);
	         vacance.setMedcin(medcin);
	         repoVac.save(vacance);
	         return "redirect:/Vacances/listeVacance";   	 
	     }
	 }

 
 @GetMapping("/SupprimerVacance/{id}")
 public String SupprimerVacance(@PathVariable int id,RedirectAttributes redirectAttributes,HttpSession session) {
	 if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
         return "redirect:/login/Formlogin";
     }
     if(repoVac.findById(id).orElse(null) != null) {
         repoVac.deleteById(id);
         return "redirect:/Vacances/listeVacance"; 
     }
     redirectAttributes.addFlashAttribute("erreur", "erreur leur dr la suppression  ");
     return "redirect:/Vacances/listeVacance";
 }
}
