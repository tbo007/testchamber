package io.github.tbo007.testchamber.json.mapping.dept;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Arrays;

public class Insert {

    public static void main(String[] args) {
        String postUrl = "http://localhost:8080/ords/hr/departments/";

        Department department = new Department();
        department.setId(51);
        department.setDepartmentName("PE");
        department.setLocation("Koblenz");

        Department.Employee employee1 = new Department.Employee();
        employee1.setEmployeeNumber(1);
        employee1.setEmployeeName("LULA");
        employee1.setJob("CLERK");
        employee1.setSalary(2.5);

        Department.Employee employee2 = new Department.Employee();
        employee2.setEmployeeNumber(2);
        employee2.setEmployeeName("ELLISON");
        employee2.setJob("MANAGER");
        employee2.setSalary(5.0);

        department.setEmployees(Arrays.asList(employee1, employee2));

        // HTTP Client für POST-Anfrage erstellen
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(postUrl);
            ObjectMapper mapper = new ObjectMapper();

            // Objekt in JSON umwandeln
            String json = mapper.writeValueAsString(department);
            StringEntity entity = new StringEntity(json);
            postRequest.setEntity(entity);
            postRequest.setHeader("Accept", "application/json");
            postRequest.setHeader("Content-type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                System.out.println("Response Status: " + response.getStatusLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

