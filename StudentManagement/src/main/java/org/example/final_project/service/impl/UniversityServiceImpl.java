package org.example.final_project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.CreateUniversityDto;
import org.example.final_project.dto.request.UpdateUniversityDto;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.exception.InvalidDataException;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.mapper.UniversityMapper;
import org.example.final_project.model.Student;
import org.example.final_project.model.University;
import org.example.final_project.model.UniversityDepartment;
import org.example.final_project.repository.UniversityDepartmentRepository;
import org.example.final_project.repository.UniversityRepository;
import org.example.final_project.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UniversityServiceImpl implements UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UniversityDepartmentRepository universityDepartmentRepository;

    @Autowired
    private UniversityMapper universityMapper;

    /**
     * Get all universities
     *
     * @return ResponseData<List < University>>
     */
    @Override
    public ResponseData<List<University>> getAllUniversity() {
        List<University> universities = universityRepository.findAll();
        return new ResponseData<>(HttpStatus.OK.value(), "All universities retrieved successfully", universities);
    }

    /**
     * Delete University by ID
     *
     * @param universityId
     * @return ResponseData<?>
     */
    @Transactional
    @Override
    public ResponseData<?> deleteUniversity(String universityId) {

        // Delete related UniversityDepartment entries
        List<UniversityDepartment> universityDepartments = universityDepartmentRepository
                .findByIdUniversityId(universityId);
        if (!universityDepartments.isEmpty()) {
            universityDepartmentRepository.deleteAll(universityDepartments);
        }

        log.info("Request to delete university with ID: {}", universityId);

        // Check if the University with the given ID exists
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> {
                    log.error("University not found with ID: {}", universityId);
                    return new ResourceNotFoundException("University not found with ID " + universityId);
                });

        // Delete the university
        universityRepository.delete(university);

        log.info("University deleted successfully with ID: {}", universityId);

        return new ResponseData<>(HttpStatus.OK.value(), "University has been deleted successfully", null);
    }

    /**
     * Save a new university
     *
     * @param request CreateUniversityDto
     * @return ResponseData<?>
     */
    @Transactional
    @Override
    public ResponseData<?> saveUniversity(CreateUniversityDto request) {

        log.info("Request to add university with ID: {}", request.getUniversityId());

        if (universityRepository.existsById(request.getUniversityId())) {
            log.warn("University ID already exists: {}", request.getUniversityId());
            throw new InvalidDataException("University ID already exists: " + request.getUniversityId());
        }

        University university = University.builder()
                .universityId(request.getUniversityId())
                .universityName(request.getUniversityName())
                .foundedYear(request.getFoundedYear())
                .address(request.getAddress())
                .phone(request.getPhone())
                .vision(request.getVision())
                .mission(request.getMission())
                .introduction(request.getIntroduction())
                .build();

        universityRepository.save(university);

        // Save university-department relationships
        if (request.getDepartmentIds() != null && !request.getDepartmentIds().isEmpty()) {
            for (String departmentId : request.getDepartmentIds()) {
                UniversityDepartment universityDepartment = UniversityDepartment.builder()
                        .id(new UniversityDepartment.UniversityDepartmentId(university.getUniversityId(), departmentId))
                        .build();
                universityDepartmentRepository.save(universityDepartment);
            }
        }

        log.info("University added successfully with ID: {}", university.getUniversityId());

        return new ResponseData<>(HttpStatus.OK.value(), "University has been added successfully", null);
    }

    /**
     * Update an existing university
     *
     * @param universityId
     * @param request      UpdateUniversityDto
     * @return ResponseData<?>
     */
    @Transactional
    @Override
    public ResponseData<?> updateUniversity(String universityId, UpdateUniversityDto request) {

        log.info("Request to update university with ID: {}", universityId);

        University existingUniversity = universityRepository.findById(universityId)
                .orElseThrow(() -> {
                    log.error("University not found with ID: {}", universityId);
                    return new ResourceNotFoundException("University not found with ID " + universityId);
                });

        // Update the fields of the existing university
        existingUniversity.setUniversityName(request.getUniversityName());
        existingUniversity.setFoundedYear(request.getFoundedYear());
        existingUniversity.setAddress(request.getAddress());
        existingUniversity.setPhone(request.getPhone());
        existingUniversity.setVision(request.getVision());
        existingUniversity.setMission(request.getMission());
        existingUniversity.setIntroduction(request.getIntroduction());

        // Save the updated University entity to the database
        universityRepository.save(existingUniversity);

        log.info("University updated successfully with ID: {}", existingUniversity.getUniversityId());

        return new ResponseData<>(HttpStatus.OK.value(), "University has been updated successfully", null);
    }

    @Override
    public ResponseData<University> getStudentById(String universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ResourceNotFoundException("University not found with id " + universityId));
        return new ResponseData<>(HttpStatus.OK.value(), "University retrieved successfully", university);
    }
}

