package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;

public class EmployeTest {
    @Test
    public void testGetAnneeAncienneteDateEmbaucheNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //When
        Integer nbAnneeAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneeAnciennete).isNull();
    }

    @Test
    public void testGetAnneeAncienneteDateEmbaucheSupNow(){
        //given
        Employe employe = new Employe("Doe","John","T12345",
                LocalDate.now().minusYears(6),1500d,1,1.0);

        //when
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        //then
        Assertions.assertThat(anneeAnciennete).isEqualTo(6);
    }

    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheInfNow(){
        //given
        Employe employe = new Employe("Doe","John","T12345",
                LocalDate.now().plusYears(6),1500d,1,1.0);

        //when
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        //then
        Assertions.assertThat(anneeAnciennete).isNull();
    }

    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheNow(){
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, 1, 1.0);
        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(anneeAnciennete).isEqualTo(0);
    }

    @RunWith(value = Parameterized.class)
    public class EmployeeTest{
        @Parameterized.Parameter(value = 0)
        public Integer performance;

        @Parameterized.Parameter(value = 1)
        public String matricule;

        @Parameterized.Parameter(value = 2)
        public Double tauxActivite;

        @Parameterized.Parameter(value = 3)
        public Long nbAnneesAnciennete;
    }

    @ParameterizedTest(name = "Perf{0}, matricule{1}, txActivite{2}, anciennete{3} => prime{4}")
    @CsvSource({
            "1, 'T12345', 1.0, 0, 1000.0",
            "1, 'T12345', 0.5, 0 , 500.0",
            "2, 'T12345', 1.0, 0, 2300.0",
            "1, 'T12345', 1.0, 2, 1200.0",
            "2, 'T12345', 1.0, 1, 2400.0",
            "1,'M12345',1.0,0,1700"
    })
    public void  testGetPrimeAnnuelle(Integer performance,String matricule,Double tauxActivite,Long nbAnneesAnciennete,
                                        Double primeAttendue){
        //given
      /*  Integer performance = 1;
        String matricule = "T12345";
        Double tauxActivite = 1.0;
        Long nbAnneesAnciennete = 0L;*/

        Employe employe = new Employe("Doe","John",matricule,LocalDate.now().minusYears(nbAnneesAnciennete),
                1500d,performance,tauxActivite);

        //when
        Double prime = employe.getPrimeAnnuelle();

        //then
        //Double primeAttendue = 1000.0;
        Assertions.assertThat(prime).isEqualTo(primeAttendue);
    }

    @Test
    public void  testGetPrimeAnnuelleMatriculeNull(){
        //given
        Employe employe = new Employe("Doe","John",null,LocalDate.now(),
                1500d,1,1.0);

        //when
        Double prime = employe.getPrimeAnnuelle();

        //then
        //Double primeAttendue = 1000.0;
        Assertions.assertThat(prime).isEqualTo(1000.0);
    }


}
