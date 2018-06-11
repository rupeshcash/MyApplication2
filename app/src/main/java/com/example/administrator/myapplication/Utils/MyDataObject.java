package com.example.administrator.myapplication.Utils;

import com.google.gson.annotations.SerializedName;

public class MyDataObject {


    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public String id;

    public MyDataObject(String a, String b){
        name= a;
        id = b;
    }
}
