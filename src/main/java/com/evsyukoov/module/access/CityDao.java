package com.evsyukoov.module.access;

import com.evsyukoov.module.entities.City;
import com.evsyukoov.module.exceptions.AlreadyInDatabaseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class CityDao implements DAO<City> {

    final private static SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("hibernate_dictionary_cities.cfg.xml")
                .buildSessionFactory();
    }

    @Override
    public void addEntity(City city) {
        boolean exit = false;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<String> query = session.createQuery("SELECT name FROM City WHERE name = :name AND id = :id", String.class);
            query.setParameter("name", city.getName());
            query.setParameter("id", city.getId());
            if (query.getResultList().isEmpty()) {
                session.save(city);
                session.getTransaction().commit();
            } else {
                exit = true;
            }
        }
        if (exit) {
            throw new AlreadyInDatabaseException(
                    String.format("Город с названием %s уже есть в базе данных и не будет добавлен", city.getName()));
        }
    }

    @Override
    public List<City> getAllEntities() {
        List<City> result;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<City> query = session.createQuery("FROM City", City.class);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result;
    }

    @Override
    public List<City> getAllEntitiesSorted() {
        List<City> result;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<City> query = session.createQuery("FROM City ORDER BY LOWER(name) DESC", City.class);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result;
    }

    @Override
    public List<City> getAllEntitiesGrouping() {
        List<City> result;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<City> query = session.createQuery("FROM City ORDER BY district DESC, name DESC", City.class);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result;
    }
}
