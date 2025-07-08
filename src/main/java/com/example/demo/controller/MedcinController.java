package com.example.demo.controller;

import java.time.LocalTime;

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

@Controller
@RequestMapping("/Medcins")
public class MedcinController {
    @Autowired private MedcinRepository repoMed;

    
    @GetMapping("/ProfileMedcin")
    public String ProfileMedcin(Model model,HttpSession session) {
    	if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
        model.addAttribute("medcins", repoMed.findAll().get(0));
        return "profileMedcine"; 
    }

    
    
//    @GetMapping("/FormAjouterMedcin")
//    public String FormAjouterMedcin(RedirectAttributes redirectAttributes,Model model) {
//        List<Medcin> medcins = repoMed.findAll();
//        if (!medcins.isEmpty()) {
//            redirectAttributes.addFlashAttribute("erreurInscription", "Médecin existe déjà !!");
//            return "redirect:/RendezVous";
//        }
//        model.addAttribute("medcin", new Medcin());
//        return "signUp";
//    }
//
//
//    @PostMapping("/AjouterMedcin")
//    public String AjouterMedcin(@ModelAttribute Medcin medcin,Model model) {
//    	if(medcin.getJourDeTravaille() == null) {
//    		medcin.setJourDeTravaille((byte) 31);
//    	}
//        repoMed.save(medcin);
//        return "logIn";
//    }

    @GetMapping("/FormModifierMedcin/{idM}")
    public String FormModifierMedcin(Model model, @PathVariable int idM,RedirectAttributes redirectAttributes,HttpSession session) {
    	if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
        Medcin medcin = repoMed.findById(idM).orElse(null);
        if(medcin != null) {
            model.addAttribute("medcin", medcin);
            return "signUp";
        }
        redirectAttributes.addFlashAttribute("erreurModification", "Médecin n'est pas existe !!");
        return "redirect:/Medcins/ProfileMedcin";
    }

    @PostMapping("/ModifierMedcin")
    public String ModifierMedcin(@ModelAttribute Medcin medcin,RedirectAttributes redirectAttributes,HttpSession session,Model model) {
        Medcin Nmedcin = repoMed.findById(medcin.getId()).orElse(null);
        
        if(Nmedcin != null) {
            Nmedcin.setNom(medcin.getNom());
            Nmedcin.setPrenom(medcin.getPrenom());
            Nmedcin.setEmail(medcin.getEmail());
            Nmedcin.setTelephone(medcin.getTelephone());
            if (medcin.getDureeM().isBefore(LocalTime.of(0, 5))) {
                model.addAttribute("erreurDurre", "La durée minimale est 5 minutes.");
                return "signUp";
            }
            Nmedcin.setDureeM(medcin.getDureeM());
            Nmedcin.setHeurDebut(medcin.getHeurDebut());
            Nmedcin.setHeurFin(medcin.getHeurFin());
            Nmedcin.setJourDeTravaille(medcin.getJourDeTravaille());
            Nmedcin.setPassword(medcin.getPassword());
            Nmedcin.setAdresseDeCabinet(medcin.getAdresseDeCabinet());
            Nmedcin.setSpecialite(medcin.getSpecialite());

            repoMed.save(Nmedcin);
            session.setAttribute("Nom", Nmedcin.getNom());
            return "redirect:/Medcins/ProfileMedcin";
        }
        redirectAttributes.addFlashAttribute("erreurModification", "erreur dans l'operation  !!");
        return "redirect:/Medcins/ProfileMedcin";
    }

    @GetMapping("/SupprimerMedcin/{id}")
    public String SupprimerMedcin(@PathVariable int id,RedirectAttributes redirectAttributes) {
        if(repoMed.findById(id).orElse(null) != null) {
            repoMed.deleteById(id);
            return "redirect:/RendezVous"; 
        }
        redirectAttributes.addFlashAttribute("erreurInscription", "erreur dans l'operation  !!");
        return "redirect:/Medcins/ProfileMedcin";
    }
}
