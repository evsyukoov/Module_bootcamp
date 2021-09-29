package com.evsyukoov.module;

import com.evsyukoov.module.access.CityDao;
import com.evsyukoov.module.access.DAO;
import com.evsyukoov.module.entities.City;
import com.evsyukoov.module.exceptions.AlreadyInDatabaseException;
import com.evsyukoov.module.exceptions.UserParseException;
import com.evsyukoov.module.service.CityService;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Manager {

    private final static String DELIMETR = "\n";

    private final static String PATTERN = "\\d{1,4}";

    private City parseLine(String line) throws Exception {
        String[] parsed = line.split(";\\s*");
        City city = new City();
        if (parsed.length != 6) {
            throw new UserParseException("Некорректный файл для загрузки в БД");
        }
        try {
            city.setId(Integer.parseInt(parsed[0]));
            city.setName(parsed[1]);
            city.setRegion(parsed[2]);
            city.setDistrict(parsed[3]);
            city.setPopulation(Integer.parseInt(parsed[4]));
            if (!parsed[5].matches(PATTERN)) {
                throw new RuntimeException();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            Date date = formatter.parse(parsed[5]);
            city.setFoundation(date);
        } catch (RuntimeException e) {
            throw new UserParseException("Некорректный формат файла для загрузки в БД");
        }
        return city;

    }

    public int addToDatabase() throws Exception {
        InputStream inputStream = getClass()
                .getClassLoader().getResourceAsStream("input/dictionary.txt");
        if (inputStream == null) {
            throw new UserParseException("Файл загрузки не найден");
        }
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter(DELIMETR);
        int addToRepo = 0;
        DAO<City> dao = new CityDao();
        while (scanner.hasNext()) {
            try {
                dao.addEntity(parseLine(scanner.next()));
                addToRepo++;
            }
            catch (AlreadyInDatabaseException e) {
                System.out.println(e.getMessage());
            }
        }
        return addToRepo;
    }

    private static void printMainMenu() {
        System.out.println("1. Вывести список городов");
        System.out.println("2. Вывести отсортированный список городов");
        System.out.println("3. Найти город с наибольшим количеством жителей");
        System.out.println("4. Поиск количеcтва городов в разрезе регионов");
        System.out.println("5. CMD + D - Завершить работу");
    }

    private static void printExtraMenu() {
        System.out.println("1. Сортировка списка городов по наименованию в алфавитном порядке по убыванию без учета регистра");
        System.out.println("2. Сортировка списка городов по федеральному округу и наименованию города" +
                "внутри каждого федерального округа в алфавитном порядке по убыванию с учетом регистра");
    }

    public void mainMenuListener() {
        printMainMenu();
        Scanner scanner = new Scanner(System.in);
        CityDao dao = new CityDao();
        while (scanner.hasNext()) {
            String task = scanner.next();
            if (task.equals("1")) {
                dao.getAllEntities().forEach(System.out::println);
            } else if (task.equals("2")) {
                printExtraMenu();
                String extraTask = scanner.next();
                if (extraTask.equals("1")) {
                    dao.getAllEntitiesSorted().forEach(System.out::println);
                } else if (extraTask.equals("2")) {
                    dao.getAllEntitiesGrouping().forEach(System.out::println);
                }
            } else if (task.equals("3")) {
                System.out.println(new CityService(dao).getMaxPopulation());
            } else if (task.equals("4")) {
                new CityService(dao).getRegions().forEach(System.out::println);
            }
            printMainMenu();
        }
    }
}
