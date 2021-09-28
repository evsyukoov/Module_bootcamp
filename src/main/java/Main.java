import com.evsyukoov.module.Manager;
import com.evsyukoov.module.exceptions.UserParseException;

import java.util.TimeZone;


public class Main {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        Manager manager = new Manager();
        int addToRepo = 0;
        try {
            System.out.println("Началась загрузка справочников");
            addToRepo = manager.addToDatabase();
        } catch (Exception e) {
            if (e instanceof UserParseException) {
                System.out.println(e.getMessage());
            } else {
                System.out.println("Непредвиденная ошибка, обратитесь в техподдержку");
            }
            return;
        }
        System.out.printf("Загрузка справочников успешно завершена\n" +
                "Загружено %d записей\n" +
                "Нажмите цифру, чтобы выбрать требуемое действие\n", addToRepo);
        manager.mainMenuListener();
    }
}
