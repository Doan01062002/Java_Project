package ra.edu.business.service.statistic;

import ra.edu.business.dao.enrollment.EnrollmentDAOImp;
import ra.edu.business.dao.statistic.StatisticDAOImp;
import ra.edu.business.model.Course;

import java.util.List;

public class StatisticServiceImp implements StatisticService {
    private final StatisticDAOImp statisticDAO = new StatisticDAOImp();
    private final EnrollmentDAOImp enrollmentDAO = new EnrollmentDAOImp();

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
        return statisticDAO.getTop5CoursesByStudents();
    }

    @Override
    public List<Course> getCoursesWithMoreThan10Students() {
        return statisticDAO.getCoursesWithMoreThan10Students();
    }

    @Override
    public int countStudentsByCourse(int courseId) {
        return enrollmentDAO.countStudentsByCourse(courseId);
    }
}