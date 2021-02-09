package com.ipiecoles.java.java350.repository;


import com.ipiecoles.java.java350.Java350Application;
import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Java350Application.class})
//@DataJpaTest
//@SpringBootTest
class EmployeRepositoryTest {
    @Autowired
    EmployeRepository employeRepository;

    @Test
    public void testFindLastMatricule0Employe(){
        //given

        //when
        String lastMatricule = employeRepository.findLastMatricule();

        //then
        Assertions.assertThat(lastMatricule).isNull();
    }

    @Test
    public void testFindLastMatricule1Employe(){
        //given
        //inserer des données en base
        employeRepository.save(new Employe("Doe","John","T12345", LocalDate.now(),1500d,1,1.0));

        //when
        //executer des requetes en base
        String lastMatricule = employeRepository.findLastMatricule();

        //then

        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }

    @Test
    public void testFindLastMatriculeNEmployes(){
        //given
        employeRepository.save(new Employe("Doe","John","T12345", LocalDate.now(),1500d,1,1.0));
        employeRepository.save(new Employe("Doe","Jane","M40325", LocalDate.now(),1500d,1,1.0));
        employeRepository.save(new Employe("Doe","Jim","C06432", LocalDate.now(),1500d,1,1.0));


        //when
        String lastMatricule = employeRepository.findLastMatricule();

        //then
        Assertions.assertThat(lastMatricule).isEqualTo("40325");
    }

    @BeforeEach
    @AfterEach
    public void purgeBdd(){
        employeRepository.deleteAll();
    }

}