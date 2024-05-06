package com.Chatting.chatapp.Service;

import com.Chatting.chatapp.Models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface Dataservice {
    @Headers({
            "Content-Type: application/json",
            "Authorization: AAAAZrVUDMA:APA91bHrz9BS31jxh1FaYRCNwlCGACNBy-_GvODIGYpR6TQiFk29-rVHEXJyxbCy0VKpFGm2rpwbVaYANAdFj82PgYVtvLdPtPvHku3T8rzRhw6zY8r9nfCRPVv-OIgo_VBhbhcFowMf"
    })
    @GET("users.json")
    Call<List<UserModel>> getDataUser();
}
