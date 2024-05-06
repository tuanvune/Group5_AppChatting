package com.Chatting.chatapp.Service;

public class APIservice {
    private static String base_url = "https://chatapp-73da9-default-rtdb.firebaseio.com/"; //192.168.1.46

    public static Dataservice getService() {
        return APIRetrofitClient.getClient(base_url).create(Dataservice.class);
    }
}
