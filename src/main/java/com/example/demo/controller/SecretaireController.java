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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Secretaires")
public class SecretaireController {
    @Autowired private SecretaireRepository repoSec;
    @Autowired private MedcinRepository repoMed;
    
    //return all the Secretaires 
    @ModelAttribute("allSecretaires")
		public List<Secretaire> getAllSecretaires() {
		    return repoSec.findAll();
	}	
    
    @GetMapping("/ProfileSecretaire/{id}")
    public String ProfileSecretaire(HttpSession session,Model model,@PathVariable int id,RedirectAttributes redirectAttributes) {
    	if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
    	Secretaire secretaire = repoSec.findById(id).orElse(null);
    	List<Medcin> medcins = repoMed.findAll();
    	if(!medcins.isEmpty()) {
    		if(secretaire != null ) {
                model.addAttribute("secretaire", secretaire);
                model.addAttribute("medcin",medcins.get(0)); 
                return "profileSecretaire"; 
            }
    		redirectAttributes.addFlashAttribute("erreurSecretaire","aucun secretaire trouver");
        	return "redirect:/login/acceuil";
    	}else {
    		redirectAttributes.addFlashAttribute("erreur","id de medcin non trouver ( vous voulez creer un medcin dans base donnes si aucun trouver  )");
    		return "redirect:/login/acceuil";
    	}
        
    }
    
    
    @GetMapping("/ListeSecretaires")
    public String liste(HttpSession session) {
    	if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
    	return "securitaire";
    }
    
    @GetMapping("/FormAjouterSecretaire")
    public String FormAjouterSecretaire(Model model,HttpSession session,RedirectAttributes redirectAttributes) {
    	if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
    	List<Medcin> medcins = repoMed.findAll();
    	if(!medcins.isEmpty()) {
    		model.addAttribute("secretaire", new Secretaire());
            model.addAttribute("medcin", repoMed.findAll().get(0));
            return "ajouteSecuritaire"; 
    	}
    	redirectAttributes.addFlashAttribute("erreur","id de medcin non trouver ( vous voulez creer un medcin dans base donnes si aucun trouver  )");
        return "redirect:/Secretaires/ListeSecretaires";
    }
    
    
    @GetMapping("/FormModifierSecretaire/{id}")
    public String FormModifierSecretaire(Model model, @PathVariable int id,RedirectAttributes redirectAttributes,HttpSession session) {
    	if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
        Secretaire secretaire = repoSec.findById(id).orElse(null);
        List<Medcin> medcins = repoMed.findAll();
        if(!medcins.isEmpty()) {
        	if(secretaire != null) {
                model.addAttribute("secretaire", secretaire);
                model.addAttribute("medcin", repoMed.findAll().get(0)); 
                return "ajouteSecuritaire"; 
            }
            redirectAttributes.addFlashAttribute("erreur","aucun Secretaire trouver pour ce id  ou id de medcin non trouver ");
            return "redirect:/Secretaires/ListeSecretaires";
        }else {
        	redirectAttributes.addFlashAttribute("erreur","id de medcin non trouver ( vous voulez creer un medcin dans base donnes si aucun trouver  )");
    		return "redirect:/login/acceuil";
        }
        
    }
    
    @PostMapping("/saveSecretaire")
    public String ModifierSecretaire(@ModelAttribute Secretaire secretaire, @RequestParam("Medcin") int idM,RedirectAttributes redirectAttributes,Model model) {
    	if(secretaire.getId() != 0) {
    		// Edit
    		Secretaire Nsecretaire = repoSec.findById(secretaire.getId()).orElse(null);
            Medcin Nmedcin = repoMed.findById(idM).orElse(null);
            
            if(Nsecretaire != null && Nmedcin != null) {
            	if(Nmedcin.getEmail().equals(secretaire.getEmail())) {
            		model.addAttribute("MemeEmail", "Email doit être différent du médecin ");
            		model.addAttribute("medcin", repoMed.findAll().get(0));
            		return "ajouteSecuritaire";
            	}
                Nsecretaire.setNom(secretaire.getNom());
                Nsecretaire.setPrenom(secretaire.getPrenom());
                Nsecretaire.setEmail(secretaire.getEmail());
                Nsecretaire.setTelephone(secretaire.getTelephone());
                Nsecretaire.setPassword(secretaire.getPassword());
                Nsecretaire.setMedcin(Nmedcin);
                
                repoSec.save(Nsecretaire);
                return "redirect:/Secretaires/ListeSecretaires"; 
            }
            redirectAttributes.addFlashAttribute("erreur","erreur dans cette operation ");
            return "redirect:/Secretaires/ListeSecretaires";
    	}else {
    		//Add
    		Medcin medcin = repoMed.findById(idM).orElse(null);
            if(medcin != null) {
            	if(medcin.getEmail().equals(secretaire.getEmail())) {
            		model.addAttribute("MemeEmail", "Email doit être différent du médecin ");
            		model.addAttribute("medcin", repoMed.findAll().get(0));
            		return "ajouteSecuritaire";
            	}
                secretaire.setMedcin(medcin);
                repoSec.save(secretaire);
                return "redirect:/Secretaires/ListeSecretaires"; 
            }
            redirectAttributes.addFlashAttribute("erreur", "erreur dans cette operation ");
            return "redirect:/Secretaires/ListeSecretaires";
    	}
        
    }
    
    @GetMapping("/SupprimerSecretaire/{id}")
    public String SupprimerSecretaire(@PathVariable int id,RedirectAttributes redirectAttributes,HttpSession session) {
    	if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
        if(repoSec.findById(id).orElse(null) != null) {
            repoSec.deleteById(id);
            return "redirect:/Secretaires/ListeSecretaires"; 
        }
        redirectAttributes.addFlashAttribute("erreur","erreur dans l'operation ");
        return "redirect:/Secretaires/ListeSecretaires";
    }
}
