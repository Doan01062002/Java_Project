-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS course_management_system;
USE course_management_system;

-- Bảng Admin
CREATE TABLE IF NOT EXISTS Admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Bảng Student
CREATE TABLE IF NOT EXISTS Student (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_code VARCHAR(10) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    sex BIT,
    phone VARCHAR(15),
    dob DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BIT DEFAULT 1
);

-- Bảng Course
CREATE TABLE IF NOT EXISTS Course (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_by_admin_id INT,
    duration INT NOT NULL,
    instructor VARCHAR(100),
    created_at VARCHAR(50),
    FOREIGN KEY (created_by_admin_id) REFERENCES Admin(id)
);

-- Bảng Enrollment
CREATE TABLE IF NOT EXISTS Enrollment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    enrollment_code VARCHAR(20) NOT NULL UNIQUE,
    student_ref_id INT NOT NULL,
    course_ref_id INT NOT NULL,
    admin_ref_id INT,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('WAITING', 'CANCEL', 'CONFIRM') DEFAULT 'WAITING',
    FOREIGN KEY (student_ref_id) REFERENCES Student(id),
    FOREIGN KEY (course_ref_id) REFERENCES Course(id),
    FOREIGN KEY (admin_ref_id) REFERENCES Admin(id)
);

-- Stored Procedure cho Admin
DELIMITER //
CREATE PROCEDURE AdminSave(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_full_name VARCHAR(100),
    IN p_email VARCHAR(100)
)
BEGIN
    INSERT INTO Admin (username, password, full_name, email)
    VALUES (p_username, p_password, p_full_name, p_email);
END //

CREATE PROCEDURE AdminUpdate(
    IN p_id INT,
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_full_name VARCHAR(100),
    IN p_email VARCHAR(100)
)
BEGIN
    UPDATE Admin
    SET username = p_username,
        password = p_password,
        full_name = p_full_name,
        email = p_email
    WHERE id = p_id;
END //

CREATE PROCEDURE AdminDelete(
    IN p_id INT
)
BEGIN
    DELETE FROM Admin WHERE id = p_id;
END //

CREATE PROCEDURE AdminFindById(
    IN p_id INT
)
BEGIN
    SELECT * FROM Admin WHERE id = p_id;
END //

CREATE PROCEDURE AdminFindAll()
BEGIN
    SELECT * FROM Admin;
END //

CREATE PROCEDURE AdminLogin(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT * FROM Admin
    WHERE username = p_username AND password = p_password;
END //

CREATE PROCEDURE LockStudent(
    IN p_student_id INT,
    IN p_is_active BIT
)
BEGIN
    UPDATE Student
    SET is_active = p_is_active
    WHERE id = p_student_id;
END //

-- Stored Procedure cho Student
CREATE PROCEDURE StudentSave(
    IN p_student_code VARCHAR(10),
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_full_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_sex BIT,
    IN p_phone VARCHAR(15),
    IN p_dob DATE
)
BEGIN
    INSERT INTO Student (student_code, username, password, full_name, email, sex, phone, dob)
    VALUES (p_student_code, p_username, p_password, p_full_name, p_email, p_sex, p_phone, p_dob);
END //

CREATE PROCEDURE StudentRegister(
    IN p_student_code VARCHAR(10),
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_full_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_sex BIT,
    IN p_phone VARCHAR(15),
    IN p_dob DATE
)
BEGIN
    INSERT INTO Student (student_code, username, password, full_name, email, sex, phone, dob, is_active)
    VALUES (p_student_code, p_username, p_password, p_full_name, p_email, p_sex, p_phone, p_dob, 1);
END //

CREATE PROCEDURE StudentUpdate(
    IN p_id INT,
    IN p_student_code VARCHAR(10),
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_full_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_sex BIT,
    IN p_phone VARCHAR(15),
    IN p_dob DATE
)
BEGIN
    UPDATE Student
    SET student_code = p_student_code,
        username = p_username,
        password = p_password,
        full_name = p_full_name,
        email = p_email,
        sex = p_sex,
        phone = p_phone,
        dob = p_dob
    WHERE id = p_id;
END //

CREATE PROCEDURE StudentDelete(
    IN p_id INT
)
BEGIN
    DELETE FROM Student WHERE id = p_id;
END //

CREATE PROCEDURE StudentFindById(
    IN p_id INT
)
BEGIN
    SELECT * FROM Student WHERE id = p_id;
END //

CREATE PROCEDURE StudentFindAll()
BEGIN
    SELECT * FROM Student;
END //

CREATE PROCEDURE StudentLogin(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT * FROM Student
    WHERE username = p_username AND password = p_password;
END //

CREATE PROCEDURE StudentFindByNameOrEmailOrCode(
    IN p_search VARCHAR(100)
)
BEGIN
    SELECT * FROM Student
    WHERE full_name LIKE p_search
       OR email LIKE p_search
       OR student_code LIKE p_search;
END //

CREATE PROCEDURE StudentSortByName(
    IN p_order VARCHAR(4)
)
BEGIN
    IF p_order = 'ASC' THEN
        SELECT * FROM Student
        ORDER BY full_name COLLATE utf8mb4_unicode_ci ASC;
    ELSE
        SELECT * FROM Student
        ORDER BY full_name COLLATE utf8mb4_unicode_ci DESC;
    END IF;
END //

CREATE PROCEDURE StudentSortByCode(
    IN p_order VARCHAR(4)
)
BEGIN
    IF p_order = 'ASC' THEN
        SELECT * FROM Student
        ORDER BY student_code ASC;
    ELSE
        SELECT * FROM Student
        ORDER BY student_code DESC;
    END IF;
END //

-- Stored Procedure cho Course
CREATE PROCEDURE CourseSave(
    IN p_course_code VARCHAR(10),
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    IN p_created_by_admin_id INT,
    IN p_duration INT,
    IN p_instructor VARCHAR(100),
    IN p_created_at VARCHAR(50)
)
BEGIN
    INSERT INTO Course (course_code, name, description, created_by_admin_id, duration, instructor, created_at)
    VALUES (p_course_code, p_name, p_description, p_created_by_admin_id, p_duration, p_instructor, p_created_at);
END //

CREATE PROCEDURE CourseUpdate(
    IN p_id INT,
    IN p_course_code VARCHAR(10),
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    IN p_created_by_admin_id INT,
    IN p_duration INT,
    IN p_instructor VARCHAR(100),
    IN p_created_at VARCHAR(50)
)
BEGIN
    UPDATE Course
    SET course_code = p_course_code,
        name = p_name,
        description = p_description,
        created_by_admin_id = p_created_by_admin_id,
        duration = p_duration,
        instructor = p_instructor,
        created_at = p_created_at
    WHERE id = p_id;
END //

CREATE PROCEDURE CourseDelete(
    IN p_id INT
)
BEGIN
    DELETE FROM Course WHERE id = p_id;
END //

CREATE PROCEDURE CourseFindById(
    IN p_id INT
)
BEGIN
    SELECT * FROM Course WHERE id = p_id;
END //

CREATE PROCEDURE CourseFindAll()
BEGIN
    SELECT * FROM Course;
END //

CREATE PROCEDURE CourseFindByName(
    IN p_name VARCHAR(100)
)
BEGIN
    SELECT * FROM Course
    WHERE name LIKE p_name;
END //

CREATE PROCEDURE CourseSortByName(
    IN p_order VARCHAR(4)
)
BEGIN
    IF p_order = 'ASC' THEN
        SELECT * FROM Course
        ORDER BY name COLLATE utf8mb4_unicode_ci ASC;
    ELSE
        SELECT * FROM Course
        ORDER BY name COLLATE utf8mb4_unicode_ci DESC;
    END IF;
END //

CREATE PROCEDURE CourseSortById(
    IN p_order VARCHAR(4)
)
BEGIN
    IF p_order = 'ASC' THEN
        SELECT * FROM Course
        ORDER BY id ASC;
    ELSE
        SELECT * FROM Course
        ORDER BY id DESC;
    END IF;
END //

CREATE PROCEDURE GetTopCourses(
    IN p_limit INT
)
BEGIN
    SELECT c.*, COUNT(e.id) as enrollment_count
    FROM Course c
             LEFT JOIN Enrollment e ON c.id = e.course_ref_id
    GROUP BY c.id
    ORDER BY enrollment_count DESC
    LIMIT p_limit;
END //

-- Stored Procedure cho Enrollment
CREATE PROCEDURE EnrollmentSave(
    IN p_enrollment_code VARCHAR(20),
    IN p_student_ref_id INT,
    IN p_course_ref_id INT,
    IN p_status VARCHAR(20)
)
BEGIN
    INSERT INTO Enrollment (enrollment_code, student_ref_id, course_ref_id, status)
    VALUES (p_enrollment_code, p_student_ref_id, p_course_ref_id, p_status);
END //

CREATE PROCEDURE EnrollmentUpdate(
    IN p_id INT,
    IN p_enrollment_code VARCHAR(20),
    IN p_student_ref_id INT,
    IN p_course_ref_id INT,
    IN p_admin_ref_id INT,
    IN p_status VARCHAR(20)
)
BEGIN
    UPDATE Enrollment
    SET enrollment_code = p_enrollment_code,
        student_ref_id = p_student_ref_id,
        course_ref_id = p_course_ref_id,
        admin_ref_id = IF(p_admin_ref_id = 0, NULL, p_admin_ref_id),
        status = p_status
    WHERE id = p_id;
END //

CREATE PROCEDURE EnrollmentDelete(
    IN p_id INT
)
BEGIN
    DELETE FROM Enrollment WHERE id = p_id;
END //

CREATE PROCEDURE EnrollmentFindById(
    IN p_id INT
)
BEGIN
    SELECT * FROM Enrollment WHERE id = p_id;
END //

CREATE PROCEDURE EnrollmentFindAll()
BEGIN
    SELECT * FROM Enrollment;
END //

CREATE PROCEDURE EnrollmentFindByCourseId(
    IN p_course_id INT
)
BEGIN
    SELECT * FROM Enrollment
    WHERE course_ref_id = p_course_id;
END //

CREATE PROCEDURE EnrollmentFindByStudentId(
    IN p_student_id INT
)
BEGIN
    SELECT * FROM Enrollment
    WHERE student_ref_id = p_student_id;
END //

-- Stored Procedure cho Statistic
CREATE PROCEDURE GetTotalCourses()
BEGIN
    SELECT COUNT(*) as total FROM Course;
END //

CREATE PROCEDURE GetTotalStudents()
BEGIN
    SELECT COUNT(*) as total FROM Student;
END //


CREATE PROCEDURE GetCoursesWithStudentCount()
BEGIN
    SELECT c.*, COUNT(e.id) AS student_count
    FROM course c
             LEFT JOIN enrollment e ON c.id = e.course_ref_id AND e.status = 'CONFIRM'
    GROUP BY c.id;
END //


CREATE PROCEDURE GetTop5CoursesByStudents()
BEGIN
    SELECT c.*
    FROM course c
             JOIN enrollment e ON c.id = e.course_ref_id
    WHERE e.status = 'CONFIRM'
    GROUP BY c.id
    ORDER BY COUNT(e.id) DESC
    LIMIT 5;
END //


CREATE PROCEDURE GetCoursesWithMoreThan10Students()
BEGIN
    SELECT c.*
    FROM course c
             JOIN enrollment e ON c.id = e.course_ref_id
    WHERE e.status = 'CONFIRM'
    GROUP BY c.id
    HAVING COUNT(e.id) > 10;
END //

DELIMITER ;

-- Phân trang dữ liệu
DELIMITER //

-- Stored Procedure cho phân trang khóa học
CREATE PROCEDURE CourseFindAllWithPagination(
    IN p_page INT,
    IN p_page_size INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_page - 1) * p_page_size;
    SELECT * FROM Course
    ORDER BY id ASC
    LIMIT p_page_size OFFSET v_offset;
END //

-- Stored Procedure cho tìm kiếm khóa học theo tên với phân trang
CREATE PROCEDURE CourseSearchByNameWithPagination(
    IN p_name VARCHAR(100),
    IN p_page INT,
    IN p_page_size INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_page - 1) * p_page_size;
    SELECT * FROM Course
    WHERE name LIKE p_name
    ORDER BY name COLLATE utf8mb4_unicode_ci ASC
    LIMIT p_page_size OFFSET v_offset;
END //

-- Stored Procedure cho sắp xếp khóa học với phân trang
CREATE PROCEDURE CourseSortWithPagination(
    IN p_sort_by VARCHAR(20),
    IN p_order VARCHAR(4),
    IN p_page INT,
    IN p_page_size INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_page - 1) * p_page_size;
    SET @query = CONCAT(
            'SELECT * FROM Course ORDER BY ',
            p_sort_by,
            ' ',
            p_order,
            ' LIMIT ',
            p_page_size,
            ' OFFSET ',
            v_offset
                 );
    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END //

-- Stored Procedure cho phân trang sinh viên
CREATE PROCEDURE StudentFindAllWithPagination(
    IN p_page INT,
    IN p_page_size INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_page - 1) * p_page_size;
    SELECT * FROM Student
    ORDER BY id ASC
    LIMIT p_page_size OFFSET v_offset;
END //

-- Stored Procedure cho tìm kiếm sinh viên theo tên, email hoặc mã với phân trang
CREATE PROCEDURE StudentSearchByNameOrEmailOrCodeWithPagination(
    IN p_search VARCHAR(100),
    IN p_page INT,
    IN p_page_size INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_page - 1) * p_page_size;
    SELECT * FROM Student
    WHERE full_name LIKE p_search
       OR email LIKE p_search
       OR student_code LIKE p_search
    ORDER BY full_name COLLATE utf8mb4_unicode_ci ASC
    LIMIT p_page_size OFFSET v_offset;
END //

-- Stored Procedure cho sắp xếp sinh viên với phân trang
CREATE PROCEDURE StudentSortWithPagination(
    IN p_sort_by VARCHAR(20),
    IN p_order VARCHAR(4),
    IN p_page INT,
    IN p_page_size INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_page - 1) * p_page_size;
    SET @query = CONCAT(
            'SELECT * FROM Student ORDER BY ',
            p_sort_by,
            ' ',
            p_order,
            ' LIMIT ',
            p_page_size,
            ' OFFSET ',
            v_offset
                 );
    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END //

-- Stored Procedure cho phân trang danh sách sinh viên theo khóa học
CREATE PROCEDURE EnrollmentFindByCourseIdWithPagination(
    IN p_course_id INT,
    IN p_page INT,
    IN p_page_size INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_page - 1) * p_page_size;
    SELECT e.*, s.full_name, s.student_code, s.email
    FROM Enrollment e
             JOIN Student s ON e.student_ref_id = s.id
    WHERE e.course_ref_id = p_course_id
    ORDER BY e.registration_date ASC
    LIMIT p_page_size OFFSET v_offset;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE sp_enrollments_count_students_by_course(
    IN p_course_id INT
)
BEGIN
    SELECT COUNT(*)
    FROM enrollment e
    WHERE e.course_ref_id = p_course_id
      AND e.status = 'CONFIRM';
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE sp_courses_exists_by_course_code(
    IN p_course_code VARCHAR(50),
    OUT p_exists BOOLEAN
)
BEGIN
    SELECT COUNT(*) INTO p_exists
    FROM course
    WHERE course_code = p_course_code;
    SET p_exists = p_exists > 0;
END //

CREATE PROCEDURE sp_courses_exists_by_name(
    IN p_name VARCHAR(255),
    OUT p_exists BOOLEAN
)
BEGIN
    SELECT COUNT(*) INTO p_exists
    FROM course
    WHERE name = p_name;
    SET p_exists = p_exists > 0;
END //

DELIMITER ;

DELIMITER //
CREATE PROCEDURE sp_enrollments_delete_by_id(
    IN p_id INT
)
BEGIN
    DELETE FROM enrollment
    WHERE id = p_id AND status = 'CANCEL';
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE sp_enrollment_exists_by_student_and_course(
    IN p_student_id INT,
    IN p_course_id INT,
    OUT p_exists BOOLEAN
)
BEGIN
    SELECT COUNT(*) INTO p_exists
    FROM enrollment
    WHERE student_ref_id = p_student_id AND course_ref_id = p_course_id;
    SET p_exists = p_exists > 0;
END //
DELIMITER ;