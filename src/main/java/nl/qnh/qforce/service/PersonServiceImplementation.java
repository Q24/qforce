package nl.qnh.qforce.service;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import nl.qnh.qforce.deserializers.PersonDeserializer;
import nl.qnh.qforce.domain.Person;

@Service
public class PersonServiceImplementation implements PersonService {
    
    @Override
    public Optional<Person> get(long id) {
        try {
            String url = "https://swapi.dev/api/people/" + id + "/";
            HttpResponse<String> response = WebService.fetchOne(url);
            if(response.statusCode() != 200) {
                return Optional.empty();
            }
            String responseBody = response.body();
            Person person = PersonDeserializer.get(id, responseBody); 
            return Optional.of(person);
        } catch (Exception e) {
            System.err.println(e);
            return Optional.empty();
        }
    }

    @Override
    public List<Person> search(String query) {
        List<Person> persons = new ArrayList<>();
        try {
            String url = "https://swapi.dev/api/people/?search=" + query;
            HttpResponse<String> response = WebService.fetchOne(url);
            if(response.statusCode() == 200) {
                JSONObject jsonObject = new JSONObject(response.body());
                if (jsonObject.has("results")) {
                    JSONArray results = jsonObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) { 
                        Person person = PersonDeserializer.get(Long.valueOf(-1), results.getJSONObject(i).toString()); 
                        persons.add(person);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return persons;
    }

}
