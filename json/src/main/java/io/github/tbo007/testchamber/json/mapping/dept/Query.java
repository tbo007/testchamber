package io.github.tbo007.testchamber.json.mapping.dept;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class Query {

    public static void main(String[] args) {
        String restUrl = "http://localhost:8080/ords/hr/departments/50";

        // HTTP Client für REST-Aufruf erstellen
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(restUrl);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Department department = mapper.readValue(response.getEntity().getContent(), Department.class);

                // Verarbeitung der Daten

                    System.out.println("Department Name: " + department.getDepartmentName());
                    System.out.println("Location: " + department.getLocation());
                    System.out.println("ID: " + department.getId());
                    for (Department.Employee employee : department.getEmployees()) {
                        System.out.println("Employee Name: " + employee.getEmployeeName());
                        System.out.println("Job: " + employee.getJob());
                        System.out.println("Salary: " + employee.getSalary());
                    }
                    System.out.println();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


