package com.evsyukoov.module.access;

import com.evsyukoov.module.entities.City;
import com.evsyukoov.module.exceptions.AlreadyInDatabaseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CityDao {

    final private static SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("hibernate_dictionary_cities.cfg.xml")
                .buildSessionFactory();
    }

    public static void addCity(City city) {
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

    public static List<City> getAllCities() {
        List<City> result;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<City> query = session.createQuery("FROM City", City.class);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result;
    }

    public static List<City> getAllCitiesSorted() {
        List<City> result;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<City> query = session.createQuery("FROM City ORDER BY LOWER(name) DESC", City.class);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result;
    }

    public static List<City> getAllCitiesGrouping() {
        List<City> result;
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Query<City> query = session.createQuery("FROM City ORDER BY district, name ASC", City.class);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result;
    }

    public static String getMaxPopulation() {
        City cityWithMaxPopulation = Collections.max(getAllCities(), (City a, City b)
                -> Integer.compare(a.getPopulation(), b.getPopulation()));
        return String.format("[%d] - %d", cityWithMaxPopulation.getId(), cityWithMaxPopulation.getPopulation());
    }

    public static List<String> getRegions() {
        List<City> cities = getAllCities();
        List<String> result = new ArrayList<>();
        cities.stream()
                .collect(Collectors.groupingBy(City::getRegion, Collectors.counting()))
                .forEach((k, v) ->
                    result.add(String.format("%s - %d", k, v)));
        return result;
    }

}
