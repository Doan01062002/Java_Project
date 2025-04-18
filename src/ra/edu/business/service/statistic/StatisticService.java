package ra.edu.business.service.statistic;

import ra.edu.business.model.Course;

import java.util.List;

public interface StatisticService{
    int getTotalCourses();
    int getTotalStudents();
    List<Course> getCoursesWithStudentCount();
    List<Course> getTop5CoursesByStudents();
    List<Course> getCoursesWithMoreThan10Students();
    int countStudentsByCourse(int courseId);
}