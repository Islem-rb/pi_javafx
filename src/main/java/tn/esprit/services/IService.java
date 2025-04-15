package tn.esprit.services;

import java.util.List;

public interface IService<T> {

    void add(T t);

    List<T> getAll(); // Récupérer tous les objets


    void update(T t);

    void delete(T t);

}
