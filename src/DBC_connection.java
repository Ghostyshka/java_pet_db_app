import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class DBC_connection  {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost/lessi_bd_v1.0";
        String user = "postgres";
        String password = "admin";

        Connection connection = null;
        QueryRunner queryRunner = new QueryRunner();
        DbUtils.loadDriver("org.postgresql.Driver");
        try {
            connection = DriverManager.getConnection(url, user, password);
            doSmth(connection, queryRunner);
            System.out.println(" Connected to the PostgreSQL server successfully.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    static void doSmth(Connection connection, QueryRunner queryRunner) throws SQLException {
        ResultSetHandler<List<Animal>> resultHandler = new BeanListHandler<>(Animal.class);

        String sometext = "true";

        Scanner scannerInputForBool = new Scanner(System.in);
        System.out.println("Do you want add some animal to DB?");
        scannerInputForBool.nextLine();

        if (scannerInputForBool.equals(sometext)){
            System.out.println("It's work");
        }

        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Введіть назву: ");
            String name = scanner.next();

            System.out.print("Введіть стан: ");
            String state = scanner.next();

            String insertQuery = "INSERT INTO animals (name, state) VALUES (?, ?)";
            queryRunner.update(connection, insertQuery, name, state);

            List<Animal> animals = queryRunner.query(connection, "SELECT * FROM animals", resultHandler);

            for (Animal animal : animals) {
                System.out.println("ID: " + animal.getId());
                System.out.println("Name: " + animal.getName());
                System.out.println("State: " + animal.getState());
                System.out.println(); // Порожній рядок для розділення даних про різні тварини
            }
        } finally {
            DbUtils.close(connection);
        }
    }

    public static class Animal{
        int id;
        String name;
        String state;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }
    }
}

