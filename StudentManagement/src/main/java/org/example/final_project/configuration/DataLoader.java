package org.example.final_project.configuration;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import org.example.final_project.model.Enrollment;
import org.example.final_project.model.Student;
import org.example.final_project.model.StudentScore;
import org.example.final_project.repository.EnrollmentRepository;
import org.example.final_project.repository.StudentRepository;
import org.example.final_project.repository.StudentScoreRepository;
import org.example.final_project.repository.criteria.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public void run(String... args) throws Exception {
//       Optional<List<Enrollment>> list= enrollmentRepository.findByTeacherTeacherId("GV001");
//       list.get().forEach(e->System.out.println(e.getStudent().getStudentId()));
        Optional<List<Enrollment> >e = enrollmentRepository.findEnrollmentsWithoutScores("GV003");
       e.get().forEach(s-> System.out.println(s.getStudent().getStudentId()));


    }


}
