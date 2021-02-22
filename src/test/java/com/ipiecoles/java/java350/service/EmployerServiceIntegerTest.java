package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@SpringBootTest
public class EmployerServiceIntegerTest {
        @Autowired
        private EmployeService employeService;

        @Autowired
        private EmployeRepository employeRepository;
        @Test
        public void testEmbauchePremierEmploye() throws Exception{
            //given
            String nom = "Doe";
            String prenom = "John";
            Poste poste = Poste.TECHNICIEN;
            NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
            Double tempsPartiel = 1.0 ;

            //when
            //employeService.embaucheEmploye(nom, prenom, poste, niveauEtude,tempsPartiel);
            employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);

            //then
            List<Employe> employes = employeRepository.findAll();
            Assertions.assertThat(employes).hasSize(1);
            Employe employe = employeRepository.findAll().get(0);
            Assertions.assertThat(employe.getNom()).isEqualTo(nom);
            Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
            Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
            Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
            Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
            Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");


        }

        @Test
        public void testCalculPerformanceCommercialEmployeMatriculeNull() throws  Exception{
            String nom = "Doe";
            String prenom = "John";
            Poste poste = Poste.COMMERCIAL;
            NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
            Double tempsPartiel = 1.0 ;

            employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);

            //then

           String matricule = "C00001";
           employeService.calculPerformanceCommercial(matricule,1500L,2500L);
           Employe employe = employeRepository.findByMatricule(matricule);
          // employe.getPerformance();
           Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);


        }


}

