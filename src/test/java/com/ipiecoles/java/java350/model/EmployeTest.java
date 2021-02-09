package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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

 
}
