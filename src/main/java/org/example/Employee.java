package org.example;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {
    private Integer id;
    private String firstName;
    private String lastName;
    private Double salary;
    private Integer managerId;

    public Employee(Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.salary = employee.getSalary();
        this.managerId=employee.getManagerId();
    }
}
