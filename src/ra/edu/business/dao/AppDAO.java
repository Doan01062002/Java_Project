package ra.edu.business.dao;

import java.util.List;

public interface AppDAO<T> {
    boolean save(T t);
    boolean update(T t);
    boolean delete(T t);
    T findById(int id);
    List<T> findAll();
}