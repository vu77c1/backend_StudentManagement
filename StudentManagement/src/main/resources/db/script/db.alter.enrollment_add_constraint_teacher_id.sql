ALTER TABLE tbl_enrollment
    ADD CONSTRAINT unique_student_course
        UNIQUE (student_id, course_id);
