package org.example;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVReaderTest {

    @Test
    void testReadCSV() {
        CSVReader csvReader = new CSVReader();
        List<Employee> employees = csvReader.readCSV("src/test/resources/TestCSVInputFile.csv");
        assertNotNull(employees, "Employee list should not be null");
        assertFalse(employees.isEmpty(), "Employee list should not be empty");
    }

    @Test
    void testValidateOrgChart() {
        CSVReader csvReader = new CSVReader();
        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee(1, "John", "Doe", 5000.00, 0));
        employees.add(new Employee(2, "Jane", "Smith", 4500.00, 1));
        employees.add(new Employee(3, "Tom", "Johnson", 4000.00, 2));
        employees.add(new Employee(4, "Alice", "Brown", 3500.00, 3));

        assertDoesNotThrow(() -> csvReader.validateOrgChart(employees),
                "Validation of org chart should not throw exceptions");
    }

    @Test
    void testEnsureManagerSalary() {
        CSVReader csvReader = new CSVReader();
        List<Employee> employees = new ArrayList<>();

        Employee manager = new Employee(1, "John", "Doe", 7000.00, 0);
        Employee subordinate1 = new Employee(2, "Jane", "Smith", 5000.00, 1);
        Employee subordinate2 = new Employee(3, "Tom", "Johnson", 4000.00, 1);

        employees.add(manager);
        employees.add(subordinate1);
        employees.add(subordinate2);

        assertDoesNotThrow(() -> csvReader.ensureManagerSalary(employees),
                "Ensuring manager's salary should not throw exceptions");
    }
}

