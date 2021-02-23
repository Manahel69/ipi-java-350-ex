package com.ipiecoles.java.java350.service;


import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {
    @InjectMocks
    private EmployeService employeService;
    @Mock
    private EmployeRepository employeRepository;
    @Test
    public void testEmbauchePremierEmploye() throws EmployeException {
        //Given Pas d'employés en base
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //Simuler qu'aucun employé n'est présent (ou du moins aucun matricule)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        //Simuler que la recherche par matricule ne renvoie pas de résultats
//        Mockito.when(employeRepository.findByMatricule(Mockito.anyString())).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(null);
        //When
       employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        //Then
//        Employe employe = employeRepository.findByMatricule("T00001");
        ArgumentCaptor<Employe> employeArgumentCaptor =ArgumentCaptor.forClass(Employe.class);
        //Mockito.verify(employeRepository,Mockito.times(1)).save(employeArgumentCaptor.capture());
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }



    @Test
    public void testEmbaucheLimiteMatricule(){
        //given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0 ;
        //simuler qu'il y a 99999 employes en base ( ou du moins que le matricule est 9999)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");


        //when
        //employeService.embaucheEmploye(nom, prenom, poste, niveauEtude,tempsPartiel);
        try{
            employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);
            Assertions.fail("embaucheEmploye aurait du lancer une exception");
        } catch (EmployeException e){
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
            //pour s'assurer qu'il n'est pas eu de sauvegarde
            Mockito.verify(employeRepository, Mockito.never()).save(Mockito.any(Employe.class));
        }


    }

    @Test
    public void testEmbaucheEmployeExisteDeja() throws EmployeException{
        //given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0 ;
        Employe employeExistant = new Employe("Doe","Jane","T00001",LocalDate.now(),1500d,1,1.0);
        //simuler qu'aucun employe n'est present (ou du moins aucun matricule)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);

        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(employeExistant);

        //when
        try{
            employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);
            Assertions.fail("embaucheEmploye aurait du lancer une exception");
        }catch (Exception e){
            //then
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");
            //pour s'assurer qu'il n'est pas eu de sauvegarde
            Mockito.verify(employeRepository, Mockito.never()).save(Mockito.any(Employe.class));
        }
    }

    @BeforeEach

    public void setup(){

        MockitoAnnotations.initMocks(this.getClass());

    }

    @Test
    void AugmenterSalairePercentIsNull() {
        //given
        Employe employe = new Employe();

        //when
        employe.setSalaire(1000D);


        //then
        Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(Employe.EXCEPTION_NULL_PERCENTAGE);
    }

    @Test
    void AugmenterSalairePercentIsNegative() {
        //given
        Employe employe = new Employe();

        //when
        employe.setSalaire(120D);


        //then
        Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(-1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(Employe.EXCEPTION_NEGATIVE_PERCENTAGE);
    }
    /*
    @Test
    @ParameterizedTest
    @CsvSource({
            "2019, 12, 12, 8",
            "2021, 12, 1, 10",
            "2022, 5, 12, 10",
            "2032, 4, 4, 11"
    })
    void testGetNbRtt(Integer year, Integer month, Integer day, Integer expectedRtt) {
        //given
        Employe employe = new Employe();
        //when
        Integer nbRtt = employe.getNbRtt(LocalDate.of(year, month, day));
        //then
        Assertions.assertThat(nbRtt).isEqualTo(expectedRtt);
    }*/

    @Test
    void CalculPerformanceCommercialSuccessCaseOneWithPerfAvgLessThanPerfEmploye() throws EmployeException {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        employe.setPerformance(5);
        Long caTraite = 500L;
        Long caObjectif = 1500L;
        //when
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(0D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif);

        //then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(2);
    }

    @Test
    void CalculPerformanceCommercialSuccessCaseOneWithPerfAvgGreaterThanPerfEmploye() throws EmployeException {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        employe.setPerformance(5);
        Long caTraite = 500L;
        Long caObjectif = 1500L;
        //when
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(2D);

        employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif);
        //then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(1);
    }

    @Test
    void CalculPerformanceCommercialSuccessCaseTwoWithPerfAvgLessThanPerfEmploye() throws EmployeException {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        employe.setPerformance(5);
        Long caTraite = 1300L;
        Long caObjectif = 1500L;
        //when
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(0D);

        employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif);
        //then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(4);
    }

    @Test
    void CalculPerformanceCommercialSuccessCaseTwoWithPerfAvgGreaterThanPerfEmploye() throws EmployeException {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        employe.setPerformance(5);
        Long caTraite = 1300L;
        Long caObjectif = 1500L;
        //when
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(5D);

        employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif);
        //then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(3);
    }

    @Test
    void CalculPerformanceCommercialSuccessCaseThreeWithPerfAvgLessThanPerfEmploye() throws EmployeException {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        employe.setPerformance(10);
        Long caTraite = 1505L;
        Long caObjectif = 1500L;
        //when
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(5D);

        employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif);
        //then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(11);
    }

    @Test
    void CalculPerformanceCommercialSuccessCaseThreeWithPerfAvgGreaterThanPerfEmploye() throws EmployeException {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        employe.setPerformance(5);
        Long caTraite = 1505L;
        Long caObjectif = 1500L;
        //when
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(10D);

        employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif);
        //then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(5);
    }

    @Test
    void CalculPerformanceCommercialSuccessCaseFourWithPerfAvgLessThanPerfEmploye() throws EmployeException {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        employe.setPerformance(8);
        Long caTraite = 1700L;
        Long caObjectif = 1500L;
        //when
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(5D);

        employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif);
        //then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(10);
    }

    @Test
    void CalculPerformanceCommercialSuccessCaseFourWithPerfAvgGreaterThanPerfEmploye() throws EmployeException {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        employe.setPerformance(7);
        Long caTraite = 1700L;
        Long caObjectif = 1500L;
        //when
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(10D);

        employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif);
        //then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(8);
    }



    @Test
    void CalculPerformanceCommercialThrowEmployeExceptionBecauseCaTraiteIsNull() {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");

        //when
        Long caTraite = null;
        Long caObjectif = 1500L;

        //then
        Assertions.assertThatThrownBy(() ->
                employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif))
                .isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le chiffre d'affaire traité ne peut être négatif ou null !");
    }

    @Test
    void CalculPerformanceCommercialThrowEmployeExceptionBecauseCaTraiteIsNegative() {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");

        //when
        Long caTraite = -1L;
        Long caObjectif = 1500L;


        //then
        Assertions.assertThatThrownBy(() ->
                employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif))
                .isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le chiffre d'affaire traité ne peut être négatif ou null !");
    }

    @Test
    void CalculPerformanceCommercialThrowEmployeExceptionBecauseCaObjectifIsNull() {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");

        //when
        Long caTraite = 1300L;
        Long caObjectif = null;

        //then
        Assertions.assertThatThrownBy(() ->
                employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif))
                .isInstanceOf(EmployeException.class)
                .hasMessageContaining("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }

    @Test
    void CalculPerformanceCommercialThrowEmployeExceptionBecauseCaObjectifIsNegative() {
        //given
        Employe employe = new Employe();
        employe.setMatricule("C00001");


        //when
        Long caTraite = 1300L;
        Long caObjectif = -1L;


        //then
        Assertions.assertThatThrownBy(() ->
                employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif))
                .isInstanceOf(EmployeException.class)
                .hasMessageContaining("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }

    @Test
    void CalculPerformanceCommercialThrowEmployeExceptionBecauseMatriculeIsNull() {
        //given
        Employe employe = new Employe();
        employe.setMatricule(null);

        //when
        Long caTraite = 1300L;
        Long caObjectif = 1500L;

        //then
        Assertions.assertThatThrownBy(() ->
                employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif))
                .isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le matricule ne peut être null et doit commencer par un C !");
    }

    @Test
    void CalculPerformanceCommercialThrowEmployeExceptionBecauseMatriculeItDoesntStartWithLetterC() {
        //given
        Employe employe = new Employe();
        employe.setMatricule("T00001");

        //when
        Long caTraite = 1300L;
        Long caObjectif = 1500L;

        //then
        Assertions.assertThatThrownBy(() ->
                employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif))
                .isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le matricule ne peut être null et doit commencer par un C !");
    }


}