package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class CSVReader {
    public static void main(String[] args) {
        CSVReader csvReader = new CSVReader();
        List<Employee> employess = csvReader.readCSV("src/main/resources/CSVInputFile.csv");

        /*System.out.println("Employee List\n\n"+employess);*/
        csvReader.ensureManagerSalary(employess);
        csvReader.validateOrgChart(employess);

    }

    public List<Employee> readCSV(String csvFile) {
        String line;
        String csvSplitBy = ",";
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(csvSplitBy);

                int id = Integer.parseInt(fields[0]);
                String firstName = fields[1];
                String LastName = fields[2];
                double salary = Double.parseDouble(fields[3]);
                int managerId = fields.length > 4 && !fields[4].isEmpty() ? Integer.parseInt(fields[4]) : 0;
                Employee employee = new Employee(id, firstName, LastName, salary, managerId);
                employees.add(employee);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public void validateOrgChart(List<Employee> employees) {
        Map<Employee, LinkedList<Employee>> orgChart = new HashMap<>();
        for (Employee employee : employees) {
            Employee baseEmployee = new Employee(employee);
            while (employee.getManagerId() != 0) {
                int managerId = employee.getManagerId();
                Employee manager = employees.stream().filter(x -> x.getId().equals(managerId)).findFirst().get();
                orgChart.computeIfAbsent(baseEmployee, k -> new LinkedList<>()).add(manager);
                employee = manager;
            }
        }
        for (Map.Entry<Employee, LinkedList<Employee>> entry : orgChart.entrySet()) {
            if (entry.getValue().size() > 5) {
                System.out.println("This employee has more than 4 managers upto CEO: " + entry.getKey());
            }
        }
    }

    public void ensureManagerSalary(List<Employee> employees) {
        Map<Employee, List<Employee>> managerSubordinatesMap = new HashMap<>();
        for (Employee employee : employees) {
            if (employee.getManagerId() != 0) {
                Employee managerEmployee = employees.stream().filter(x -> x.getId().equals(employee.getManagerId())).findFirst().get();
                managerSubordinatesMap.computeIfAbsent(managerEmployee, k -> new ArrayList<>()).add(employee);
            }
        }
        /*System.out.println("managersubordinatesMap");
        System.out.println(managerSubordinatesMap);*/
        // check manager's salaries
        for (Employee manager : employees) {
            List<Employee> subordinates = managerSubordinatesMap.get(manager);
            if (subordinates != null && !subordinates.isEmpty()) {
                double totalSubordinateSalary = 0;
                for (Employee subordinate : subordinates) {
                    totalSubordinateSalary += subordinate.getSalary();
                }
                double averageSubordinateSalary = totalSubordinateSalary / subordinates.size();
                double minSalaryLimitForManager = averageSubordinateSalary * 1.2;
                double maxSalaryLimitForManager = averageSubordinateSalary * 1.5;
                if (manager.getSalary() < minSalaryLimitForManager) {
                    System.out.println("The salary is less than the min salary range, by amount : " + (minSalaryLimitForManager - manager.getSalary()) + " for this manager ->" + manager);
                }
                if (manager.getSalary() > maxSalaryLimitForManager) {
                    System.out.println("The salary is more than the max salary range, by amount : " + (manager.getSalary() - maxSalaryLimitForManager) + " for this managers->" + manager);
                }
            }
        }
    }
}

