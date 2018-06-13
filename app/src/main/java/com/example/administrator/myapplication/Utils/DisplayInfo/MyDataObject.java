package com.example.administrator.myapplication.Utils.DisplayInfo;

import com.google.gson.annotations.SerializedName;

public class MyDataObject {


    @SerializedName("name")
    public String name;
    @SerializedName("operator_name")
    public String operator_name;
    @SerializedName("temp")
    public String curr_temp;
    @SerializedName("running_time")
    public String running_time;
    @SerializedName("workstation_eff")
    public String workstation_eff;
    @SerializedName("id")
    public String id;
    @SerializedName("current_task")
    public String curr_task;
    @SerializedName("WorkCell")
    public String workcell_name;
    @SerializedName("Remarks")
    public String remarks;

    public MyDataObject(){
        name= "N/A";
        id = "N/A";
        operator_name = "N/A";
        running_time ="N/A";
        workstation_eff = "N/A";
        operator_name = "N/A";
        curr_task = "N/A";
        workcell_name = "N/A";
        remarks = "N/A";
    }
}
