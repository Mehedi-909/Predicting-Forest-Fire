package com.example.forestfireprediction;

public class Patient {

    public int diagnosis;
    public double fvc;
    public double volume;
    public int performance;
    public int pain;
    public int hm;
    public int ds;
    public int cough;
    public int weakness;
    public int tumourSize;
    public int diabetes;
    public int mi;
    public int pad;
    public int smoking;
    public int asthma;
    public int age;
    public int risk;

    public double distance;

    public Patient(int diagnosis, double fvc, double volume, int performance, int pain, int hm, int ds, int cough, int weakness, int tumourSize, int diabetes, int mi, int pad, int smoking, int asthma, int age,int risk) {
        this.diagnosis = diagnosis;
        this.fvc = fvc;
        this.volume = volume;
        this.performance = performance;
        this.pain = pain;
        this.hm = hm;
        this.ds = ds;
        this.cough = cough;
        this.weakness = weakness;
        this.tumourSize = tumourSize;
        this.diabetes = diabetes;
        this.mi = mi;
        this.pad = pad;
        this.smoking = smoking;
        this.asthma = asthma;
        this.age = age;
        this.risk = risk;
    }

    public Patient(int diagnosis, double fvc, double volume, int performance, int pain, int hm, int ds, int cough, int weakness, int tumourSize, int diabetes, int mi, int pad, int smoking, int asthma, int age) {
        this.diagnosis = diagnosis;
        this.fvc = fvc;
        this.volume = volume;
        this.performance = performance;
        this.pain = pain;
        this.hm = hm;
        this.ds = ds;
        this.cough = cough;
        this.weakness = weakness;
        this.tumourSize = tumourSize;
        this.diabetes = diabetes;
        this.mi = mi;
        this.pad = pad;
        this.smoking = smoking;
        this.asthma = asthma;
        this.age = age;

    }


    public  void setDistance(double distance){
        this.distance = distance;
    }
    public double getDistance(){
        return this.distance;
    }



}
