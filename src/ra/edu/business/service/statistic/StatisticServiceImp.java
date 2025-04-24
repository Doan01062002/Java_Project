package ra.edu.business.service.statistic;

import ra.edu.business.dao.enrollment.EnrollmentDAOImp;
import ra.edu.business.dao.statistic.StatisticDAOImp;
import ra.edu.business.model.Course;
import ra.edu.business.model.EnrollmentStatus;
import ra.edu.business.service.course.CourseService;
import ra.edu.business.service.course.CourseServiceImp;
import ra.edu.business.service.enrollment.EnrollmentService;
import ra.edu.business.service.enrollment.EnrollmentServiceImp;

import java.util.List;

public class StatisticServiceImp implements StatisticService {
    private final StatisticDAOImp statisticDAO = new StatisticDAOImp();
    private final EnrollmentDAOImp enrollmentDAO = new EnrollmentDAOImp();
    private final CourseService courseService = new CourseServiceImp(); // Initialize courseService
    private final EnrollmentService enrollmentService = new EnrollmentServiceImp(); // Initialize enrollmentService

    @Override
    public int getTotalCourses() {
        return statisticDAO.getTotalCourses();
    }

    @Override
    public int getTotalStudents() {
        return statisticDAO.getTotalStudents();
    }

    @Override
    public List<Course> getCoursesWithStudentCount() {
        return statisticDAO.getCoursesWithStudentCount();
    }

    @Override
    public List<Course> getTop5CoursesByStudents() {
        return courseService.findAll().stream()
                .sorted((c1, c2) -> {
                    int count1 = (int) enrollmentService.findByCourseId(c1.getId()).stream()
                            .filter(e -> e.getStatus() == EnrollmentStatus.CONFIRM)
                            .count();
                    int count2 = (int) enrollmentService.findByCourseId(c2.getId()).stream()
                            .filter(e -> e.getStatus() == EnrollmentStatus.CONFIRM)
                            .count();
                    return Integer.compare(count2, count1); // Sort descending by student count
                })
                .limit(5) // Get top 5 courses
                .toList();
    }

    @Override
    public List<Course> getCoursesWithMoreThan10Students() {
        return courseService.findAll().stream()
                .filter(course -> {
                    int studentCount = (int) enrollmentService.findByCourseId(course.getId()).stream()
                            .filter(e -> e.getStatus() == EnrollmentStatus.CONFIRM)
                            .count();
                    return studentCount > 10;
                })
                .toList();
    }

    @Override
    public int countStudentsByCourse(int courseId) {
        return (int) enrollmentDAO.findByCourseId(courseId)
                .stream()
                .filter(enrollment -> enrollment.getStatus() == EnrollmentStatus.CONFIRM)
                .count();
    }
}