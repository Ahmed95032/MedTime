package com.example.demo.controller;

import java.time.LocalDate;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.PdfGeneratorService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/RendezVous") 
public class RendezVousController {
	@Autowired private RendezVousRepository repoRend;
	@Autowired private MedcinRepository repoMed;
	@Autowired private VacanceRepository repoVac;
	private final PdfGeneratorService pdfGeneratorService;
	
	//affichage des rendez vous pour un medcin
	@GetMapping("/RendezVous/Medcin/{id}")
	public String AfficheRenVParMedcin(@PathVariable int id, Model model) {
	    List<RendezVous> listeRenV = repoRend.findByMedcinId(id);
	    model.addAttribute("RenVParM", listeRenV);
	    return "RendezVousParMedcin";
	}
	 
	
	public void ChechRole() {
		List<RendezVous> liste=repoRend.findAll();
		for(RendezVous r:liste) {
			if(r.getRole().equals("terminer") || r.getRole().equals("absent")) {
				if (r.getDate().isAfter(LocalDate.now())) {
				    r.setRole("prêt");
				    repoRend.save(r);
				} 
			}
		}
	}
	//return the medcin 
	@ModelAttribute("Medcin0")
	public Medcin getMedcin0() {
	    List<Medcin> medecins = repoMed.findAll();
	    if (!medecins.isEmpty()) {
	        return medecins.get(0);
	    }
	    return null;
	}

//	@ModelAttribute("rendezvous0")
//	public RendezVous getRendezVous0() {
//		List<RendezVous> REndv = repoRend.findAll();
//		if(!REndv.isEmpty()) {
//			return REndv.get(0);
//		}
//		return null;
//	}
	
	//return all the Vacances that not end yet 
	@ModelAttribute("allVacancesNotEnd")
	public List<Vacance> getAllVacancesNotEnd() {
			LocalDate today = LocalDate.now();
			return repoVac.findByDateFinGreaterThanEqual(today);
	}
	
	//affichage les rendez pour une role donner 
	@ModelAttribute("allRendezVousR")
	public List<RendezVous> getAllRendezVousR() {
		  return repoRend.findByRole("prêt");
	} 	
		
	 
	 //affichage des rendez vous pour une date donner 
	@GetMapping("/RendezVous/Date/{date}")
	public String AfficheRenVParDate(@PathVariable LocalDate date, Model model) {
	    List<RendezVous> listeRenV = repoRend.findByDate(date);
	    model.addAttribute("RenVParDate", listeRenV);
	    return "RendezVousParDate";
	}
	 
	//affichage des rendez vous pour un numero de telephone
	@GetMapping("/RendezVous/Tele/{telephonePatient}")
	public String AfficheRenVParTele(@PathVariable String telephonePatient, Model model) {
	    RendezVous RenV = repoRend.findByTelephonePatient(telephonePatient);
	    model.addAttribute("RenVParTel", RenV);
	    return "RendezVousParTelephone";
	}
	 
	//affichage d'un rendez vous pour un id donnees
	@GetMapping("/RendezVous/{id}")
	 public String AfficheRenVParid(@PathVariable int id, Model model) {
		RendezVous rendezVous = repoRend.findById(id).orElse(null);
		if (rendezVous != null) {
		    model.addAttribute("RenVParId", rendezVous);
		} else {
		    model.addAttribute("error", "Rendez-vous non trouvé");
		}
		return "RendezVousParId";
	 }	 
	 
	@GetMapping
	public String FormAjouterRendezVous(Model model,HttpSession session) {
		if(session.getAttribute("user") != null && session.getAttribute("role") != null) {
			return "redirect:/login/acceuil";
		}
	    List<Medcin> medcins = repoMed.findAll();
	    if (!medcins.isEmpty()) {
	    	if (model.getAttribute("rendezvous")==null) {
	    		model.addAttribute("rendezvous", new RendezVous());
	    	}	
	    	model.addAttribute("medcin", medcins.get(0));
	        model.addAttribute("id", medcins.get(0).getId());
	    } else {
	    	model.addAttribute("erreurRendezVous", "id de medcin non trouver ( vous voulez creer un medcin dans base donnes si aucun trouver  ) ");
	    }
	    return "rendezVous";
	}
	
	@GetMapping("/FormModifierRendezVous/{id}")
	 public String FormModifierRendezVous(Model model,@PathVariable int id,RedirectAttributes redirectAttributes,HttpSession session) {
		 if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
		 RendezVous rendezvous=repoRend.findById(id).orElse(null);
		 List<Medcin> medcins = repoMed.findAll();
		 if(!medcins.isEmpty()) {
			 if(rendezvous != null) {
				 model.addAttribute("rendezvous", rendezvous);
				 model.addAttribute("medcin", medcins.get(0));
				 model.addAttribute("id", medcins.get(0).getId());
				 return "rendezVous";
			 }
			 redirectAttributes.addFlashAttribute("erreur", "Rendez Vous n'existe pas ");
			 return "redirect:/login/acceuil";
		  }else {
			redirectAttributes.addFlashAttribute("erreur", "id de medcin non trouver ( vous voulez creer un medcin dans base donnes si aucun trouver  ) ");
			return "redirect:/login/acceuil";
		  }
	}
		 
	
	@PostMapping("/AjouterRendezVous")
	public String AjouterRendezVous(@ModelAttribute RendezVous rendezvous,@RequestParam(value = "RendezVousId", required = false)  Integer idR, Model model,@RequestParam(value = "Medcin", required = false) Integer idM,RedirectAttributes redirectAttributes,HttpSession session) {
			
 		if (idR !=null && idR !=0) {
			//Edit
			Medcin medcin = repoMed.findById(idM).orElse(null);
		     	if (medcin == null) {
		     		redirectAttributes.addFlashAttribute("erreurRendezVous", "Médecin introuvable !!!");
				    return "redirect:/RendezVous/FormModifierRendezVous/"+idR;
			    }else if (rendezvous.getNomPatient().equals("")) {
			    	redirectAttributes.addFlashAttribute("nomErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous/FormModifierRendezVous/"+idR;
			    }else if (rendezvous.getPrenomPatient().equals("")) {
			    	redirectAttributes.addFlashAttribute("prenomErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous/FormModifierRendezVous/"+idR;
			    }else if (rendezvous.getTelephonePatient().equals("")) {
			    	redirectAttributes.addFlashAttribute("telephoneErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous/FormModifierRendezVous/"+idR;
			    }else if (rendezvous.getnCarteNationale().equals("")) {
			    	redirectAttributes.addFlashAttribute("ncarteErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous/FormModifierRendezVous/"+idR;
			    }else if (rendezvous.getDate() == null) {
			    	redirectAttributes.addFlashAttribute("dateErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous/FormModifierRendezVous/"+idR;
			    }else if (rendezvous.getTime() == null) {
			    	redirectAttributes.addFlashAttribute("timeErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous/FormModifierRendezVous/"+idR;
			    }
		     RendezVous testRendezVous1 = repoRend.findByDateAndTimeAndMedcinAndRole(rendezvous.getDate(), rendezvous.getTime(), medcin,"prêt");
		     List<RendezVous> testRendezVous2 = repoRend.findByTelephonePatientAndNCarteNationaleAndRole(rendezvous.getTelephonePatient(), rendezvous.getnCarteNationale(),"prêt");
		     if((testRendezVous1 != null && testRendezVous1.getId() != idR ) || (!testRendezVous2.isEmpty()  && testRendezVous2.get(0).getId() != idR) ) {
		    	 
		    	 if(testRendezVous1 != null) {
		    		 redirectAttributes.addFlashAttribute("erreurRendezVous", "Ce créneau est déjà réservé.");
		    	 }
		    	 if(testRendezVous2!= null){
		    		 redirectAttributes.addFlashAttribute("erreurRendezVous", "Vous avez déjà un rendez-vous.");
		    		 redirectAttributes.addFlashAttribute("rendezvous1",testRendezVous2);
		    	 } 
		         return "redirect:/RendezVous/FormModifierRendezVous/"+idR;
		     }
		     
		     RendezVous NRend = repoRend.findById(idR).orElse(null);
		     if (NRend != null) {
		         NRend.setMedcin(medcin);
		         NRend.setDate(rendezvous.getDate());
		         NRend.setTime(rendezvous.getTime());
		         NRend.setRole("prêt");
		         NRend.setNomPatient(rendezvous.getNomPatient());
		         NRend.setPrenomPatient(rendezvous.getPrenomPatient());
		         NRend.setnCarteNationale(rendezvous.getnCarteNationale());
		         NRend.setTelephonePatient(rendezvous.getTelephonePatient());
		         NRend.setDureeS(medcin.getDureeM());

		         repoRend.save(NRend);
		         ChechRole();
		         redirectAttributes.addFlashAttribute("BienModifier", "Rendez Vous est bien modifier .");
		         return "redirect:/login/acceuil"; 
		     }
		     return "rendezVous";
		     
		     
		}else {
			
		   //Add
				Medcin medcin = repoMed.findById(idM).orElse(null);
				redirectAttributes.addFlashAttribute("rendezvous", rendezvous);
			    if (medcin == null) {
			    	redirectAttributes.addFlashAttribute("erreurRendezVous", "Médecin introuvable !!!");
				    return "redirect:/RendezVous";
			    }else if (rendezvous.getNomPatient().equals("")) {
			    	redirectAttributes.addFlashAttribute("nomErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous";
			    }else if (rendezvous.getPrenomPatient().equals("")) {
			    	redirectAttributes.addFlashAttribute("prenomErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous";
			    }else if (rendezvous.getTelephonePatient().equals("")) {
			    	redirectAttributes.addFlashAttribute("telephoneErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous";
			    }else if (rendezvous.getnCarteNationale().equals("")) {
			    	redirectAttributes.addFlashAttribute("ncarteErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous";
			    }else if (rendezvous.getDate() == null) {
			    	redirectAttributes.addFlashAttribute("dateErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous";
			    }else if (rendezvous.getTime() == null) {
			    	redirectAttributes.addFlashAttribute("timeErreur", "Ce champ est obligatoire");
				    return "redirect:/RendezVous";
			    }
			    
			    RendezVous testRendezVous1 = repoRend.findByDateAndTimeAndMedcinAndRole(rendezvous.getDate(), rendezvous.getTime(), medcin,"prêt");
			    List<RendezVous>  testRendezVous2 = repoRend.findByTelephonePatientAndNCarteNationaleAndRole(rendezvous.getTelephonePatient(), rendezvous.getnCarteNationale(),"prêt");

			    if (!testRendezVous2.isEmpty() || testRendezVous1 != null ) {
			        if (!testRendezVous2.isEmpty()){
			        	for(RendezVous v:testRendezVous2) {
			        		if(v.getDate().isAfter(LocalDate.now())) {
			        			redirectAttributes.addFlashAttribute("erreurRendezVousPdf", "Vous avez déjà un rendez-vous.");
			        			redirectAttributes.addFlashAttribute("idRendezVousPdf",v.getId());
			        			return "redirect:/RendezVous";
			        		}
			        	}
			        } else {
			        	redirectAttributes.addFlashAttribute("erreurRendezVous", "Ce créneau est déjà réservé pour ce médecin.");
			        	return "redirect:/RendezVous";
			        }
				    
			    }

			    rendezvous.setMedcin(medcin);
			    rendezvous.setRole("prêt");
			    rendezvous.setDureeS(medcin.getDureeM());
			    RendezVous savedRendV=repoRend.save(rendezvous);

			    redirectAttributes.addFlashAttribute("sendRendezVous", "Rendez Vous est bien ajouter .");
			    redirectAttributes.addFlashAttribute("idRendezVous",savedRendV.getId());
			    redirectAttributes.addFlashAttribute("mode", "ajout"); 
//			    redirectAttributes.addFlashAttribute("sendRendezVous", "Rendez Vous est bien ajouter (vérifie tes SMS).");
			    return "redirect:/RendezVous";
		}
	    
	}

	
	@GetMapping("/ModifierRole/{id}")
	 public String ModifierRole(@PathVariable int id, @RequestParam String role, RedirectAttributes redirectAttributes,HttpSession session) {
		if (session.getAttribute("user") == null || session.getAttribute("role") == null) {
	         return "redirect:/login/Formlogin";
	     }
		RendezVous NrendezVosu=repoRend.findById(id).orElse(null);
		 if(NrendezVosu !=null) {
			 if(role.equals("prêt")) {
				 return "redirect:/RendezVous/FormModifierRendezVous/" + id;
			 }else if((role.equals("terminer") || role.equals("absent")) ) {
					 if(NrendezVosu.getDate().isAfter(LocalDate.now())) {
						 redirectAttributes.addFlashAttribute("erreur", "Vous ne pouvez pas appliquer ce rôle  maintenant. Veuillez réessayer une fois la date du rendez-vous passée.");
						 return "redirect:/login/acceuil";
					 }else{
						 NrendezVosu.setRole(role);
						 repoRend.save(NrendezVosu);
			//			 redirectAttributes.addFlashAttribute("erreur", "Role de Rendez Vous est bien modifier  .");
						 ChechRole();
						 return "redirect:/login/acceuil"; 
					 }
			 }else if((role.equals("annuler"))) {
				 NrendezVosu.setRole("annuler");
				 repoRend.save(NrendezVosu);
				 return "redirect:/login/acceuil";
			 }
		 }
		 redirectAttributes.addFlashAttribute("erreur", "erreur dans l'operation   .");
		 return "redirect:/login/acceuil";
	 }
	 
	 @GetMapping("/SupprimerRendezVous/{id}")
	 public String SupprimerRendezVous(@PathVariable int id,RedirectAttributes redirectAttributes) {
		 if(repoRend.findById(id).orElse(null) !=null) {
			 repoRend.deleteById(id);
			 return "redirect:/login/acceuil";
		 }
		 redirectAttributes.addFlashAttribute("erreur", "erreur dans l'operation   .");
		 return "redirect:/login/acceuil";
	 }
	 
	 //PDF 
	 public RendezVousController(RendezVousRepository repoRend, PdfGeneratorService pdfGeneratorService) {
	        this.repoRend = repoRend;
	        this.pdfGeneratorService = pdfGeneratorService;
	    }

	    @GetMapping("/{id}/pdf")
	    public ResponseEntity<ByteArrayResource> downloadPdf(@PathVariable int id) {
	        RendezVous rdv = repoRend.findById(id).orElse(null);
	        if (rdv == null) {
	            return ResponseEntity.notFound().build();
	        }

	        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml(rdv);
	        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rendezvous.pdf")
	                .contentType(MediaType.APPLICATION_PDF)
	                .contentLength(pdfBytes.length)
	                .body(resource);
	    }
}
