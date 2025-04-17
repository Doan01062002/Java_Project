package ra.edu.business.service;

import java.util.List;

public interface AppService<T> {
    boolean save(T t);
    boolean update(T t);
    boolean delete(T t);
    T findById(int id);
    List<T> findAll();
}