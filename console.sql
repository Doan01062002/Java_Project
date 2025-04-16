-- Tạo database
create database if not exists course_management_system;
use course_management_system;

-- Bảng Admin
create table Admin (
    id int auto_increment primary key,
    username varchar(50) not null unique,
    password varchar(255) not null
);

-- Bảng Học viên
create table Student (
    id int auto_increment primary key,
    student_id varchar(50) not null unique,
    username varchar(50) not null unique,
    password varchar(255) not null,
    full_name varchar(100) not null,
    email varchar(100) not null unique,
    sex bit,
    phone varchar(20),
    created_at timestamp default current_timestamp
);

-- Bảng Khóa học
create table Course (
    id int auto_increment primary key,
    course_id varchar(50) not null unique,
    name varchar(100) not null,
    description text,
    duration int not null,
    instructor varchar(100) not null,
    created_by int not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    foreign key (created_by) references Admin(id)
);

-- Bảng Đăng ký khóa học
create table Enrollment (
    id int auto_increment primary key,
    enrollment_id varchar(50) not null unique,
    student_id varchar(50) not null,
    course_id varchar(50) not null,
    registration_date timestamp default current_timestamp,
    status enum('waiting', 'denied', 'cancel', 'confirm') default 'waiting',
    approved_by int,
    approved_at timestamp null,
    foreign key (student_id) references Student(student_id),
    foreign key (course_id) references Course(course_id),
    foreign key (approved_by) references Admin(id)
);
alter table Enrollment
    add constraint unique_enrollment unique (student_id, course_id);