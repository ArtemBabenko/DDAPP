package ddapp_project.ddapp;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiEndpointInterface {

    @GET("api/test/students")
    Call<List<Student>> getStudents();
}
