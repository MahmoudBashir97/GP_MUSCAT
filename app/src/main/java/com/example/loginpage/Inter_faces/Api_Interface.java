package com.example.loginpage.Inter_faces;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import com.example.loginpage.models.send;

public interface Api_Interface {

    //لما تبعت الداتا مع بعض زي مثلا عبارة عن package
    @Headers({"Authorization: key=AAAAgI6vvSI:APA91bHGvogeUgWQ97SkStw6arGtDecp-uxTrhIqP5Z0N0zFUASCLcsg_0SJ-R1U_pCl2jdGwgFjL4VW0vHckrVNWVSWI3AGKmZ79lc5HRWVCDuclbR-mF7Iq3o7Zutnsw12G_hQpfwP",
            "Content-Type:application/json"})
    @POST("fcm/send")
    public Call<send> storedata(@Body send send);

}
