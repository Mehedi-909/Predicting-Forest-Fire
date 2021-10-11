package com.example.forestfireprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class LungCancer extends AppCompatActivity {


    EditText x,y,month,day,ffmc,dmc,dc,isi,temperature,rh,wind,rain,pad,smoking,asthma,age;
    Button manual, crossValidation;
    TextView textView;
    ArrayList <Patient> arrayListPatient = new ArrayList<>();
    //ArrayList <FireData> arrayList = new ArrayList<>();
    double minimunDistance = 100000000;
    double dmax = 0.00;
    String expectedCategory = null;
    int k = 0;
    double all = 0.00;
    int count=0;
    int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lung_cancer);

        x= findViewById(R.id.dg);
        y= findViewById(R.id.fvc);
        month= findViewById(R.id.volume);
        day= findViewById(R.id.perf);
        ffmc= findViewById(R.id.pain);
        dmc= findViewById(R.id.hae);
        dc= findViewById(R.id.dys);
        isi= findViewById(R.id.cough);
        temperature= findViewById(R.id.wk);
        rh= findViewById(R.id.tm);
        wind= findViewById(R.id.db);
        rain= findViewById(R.id.mi);
        pad = findViewById(R.id.pad);
        smoking= findViewById(R.id.smoke);
        asthma= findViewById(R.id.asthma);
        age = findViewById(R.id.age);


        manual = findViewById(R.id.buttonExT);
        crossValidation = findViewById(R.id.buttonCr);
        textView = findViewById(R.id.print);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            k = bundle.getInt("k");
        }

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int xValue =  Integer.parseInt(x.getText().toString());
                double yValue =  Double.parseDouble(y.getText().toString());
                double monthValue =  Double.parseDouble(month.getText().toString());
                int dayValue =  Integer.parseInt(day.getText().toString());
                int ffmcValue =  Integer.parseInt(ffmc.getText().toString());
                int dmcValue =  Integer.parseInt(dmc.getText().toString());
                int dcValue =  Integer.parseInt(dc.getText().toString());
                int isiValue =  Integer.parseInt(isi.getText().toString());
                int tempValue =  Integer.parseInt(temperature.getText().toString());
                int rhValue =  Integer.parseInt(rh.getText().toString());
                int windValue =  Integer.parseInt(wind.getText().toString());
                int rainValue =  Integer.parseInt(rain.getText().toString());

                int pd =  Integer.parseInt(pad.getText().toString());
                int smk =  Integer.parseInt(smoking.getText().toString());
                int ast =  Integer.parseInt(asthma.getText().toString());
                int ag =  Integer.parseInt(age.getText().toString());

                Patient patient = new Patient(xValue,yValue,monthValue,dayValue,ffmcValue,dmcValue,dcValue,isiValue,tempValue,rhValue,windValue,rainValue,pd,smk,ast,ag);
                readFile();
                predictRisk(patient);


            }
        });

        crossValidation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFile();
                //calculateMaxDistance();
                crossValidation();

            }
        });
    }

    public void readFile(){

        Patient patient;
        BufferedReader reader;
        String [] values;
        int count = 0;
        try{
            final InputStream file = getAssets().open("input4.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while(line != null){
                values = line.split(",");
                if(values.length == 17){
                    //patient = new Patient(Integer.parseInt(values[0]),Double.parseDouble(values[1]),Double.parseDouble(values[2]),Integer.parseInt(values[3]),Integer.parseInt(values[4]),Integer.parseInt(values[5]),Integer.parseInt(values[6]),Integer.parseInt(values[7]),Integer.parseInt(values[8]),Integer.parseInt(values[9]),Integer.parseInt(values[10]),Integer.parseInt(values[11]),Integer.parseInt(values[12]),Integer.parseInt(values[13]),Integer.parseInt(values[14]),Integer.parseInt(values[15]),Integer.parseInt(values[16]));
                    patient = new Patient(Integer.parseInt(values[0]),Double.parseDouble(values[1]),Double.parseDouble(values[2]),Integer.parseInt(values[3]),Integer.parseInt(values[4]),Integer.parseInt(values[5]),Integer.parseInt(values[6]),Integer.parseInt(values[7]),Integer.parseInt(values[8]),Integer.parseInt(values[9]),Integer.parseInt(values[10]),Integer.parseInt(values[11]),Integer.parseInt(values[12]),Integer.parseInt(values[13]),Integer.parseInt(values[14]),Integer.parseInt(values[15]),Integer.parseInt(values[16]));
                    arrayListPatient.add(patient);
                    count ++;

                }
                line = reader.readLine();
            }

        } catch(IOException ioe){
            ioe.printStackTrace();
        }
        //Toast.makeText(this, "Data "+count, Toast.LENGTH_LONG).show();
        //textView.setText("Data read :" + arrayListPatient.size());


    }

    public void predictRisk(Patient testData){

        for(Patient patient : arrayListPatient){
            patient.setDistance(calculateDistance(testData,patient));
        }

        Collections.sort(arrayListPatient, new Comparator<Patient>(){
            @Override
            public int compare(Patient p1, Patient p2) {
                double dif=p1.getDistance()-p2.getDistance();
                if(dif>0)
                    return 1;
                else if(dif<0)
                    return -1;
                else{
                    return 0;
                }
            }


        });

        int prediction = calculateOutput();
        if(prediction == 1){
            textView.setText("Prediction : " + "At risk");
        }
        else{
            textView.setText("Prediction : " + "Out of risk");
        }

    }

    public void crossValidation(){

        int fold=arrayListPatient.size()/10;
        int iteration=0;
        int current=0;

        for(int i=0;i<10;i++){


            current+=fold;
            ArrayList<Integer> al = new ArrayList<Integer>();
            int numberOfRand=0;
            while(numberOfRand<fold){

                Random r= new Random();
                int rand = r.nextInt(current);
                boolean  flag = al.contains(rand);

                if(!flag){
                    al.add(rand);
                    numberOfRand++;
                }

            }

            applyKNN(al,fold);
            al.clear();
            iteration ++;
        }

    }

    public int calculateOutput(){
        int positive =0, negative= 0;
        double w1=0;
        double w2=0;
        double w3=0;
        double w4=0;

        for (int m =0; m< k; m++){

            int category = arrayListPatient.get(m).risk;
            if(category == 2){
                negative++;

            }
            else if(category == 1){
                positive++;
            }



        }

        if(positive > negative){
            return 1;
        }
        else{
            return 2;
        }


    }

    public void applyKNN(ArrayList<Integer> al,int fold){

        int index=0;
        for(int i=0;i<fold;i++){

            for(int j=0;j<arrayListPatient.size()-1;j++){

                boolean  flag = al.contains(j);

                if(!flag && index<10){
                    arrayListPatient.get(j).distance = calculateDistance(arrayListPatient.get(al.get(i)),arrayListPatient.get(j));

                }

            }

            int actualDecision = arrayListPatient.get(al.get(i)).risk;
            Collections.sort(arrayListPatient, new Comparator<Patient>(){
                @Override
                public int compare(Patient p1, Patient p2) {
                    double dif=p1.distance-p2.distance;
                    if(dif>0)
                        return 1;
                    else if(dif<0)
                        return -1;
                    else{
                        return 0;
                    }
                }



            });


            int decision=calculateOutput();
            if(decision == actualDecision){
                count++;
            }
            total++;
        }

        //textView.setText("Correct Prediction "+count + "  Total Prediction " + total);
        textView.setText("Accuracy " + (count*100)/total +"%");


    }

    public double calculateDistance (Patient patient1, Patient patient2)
    {
        return Math.sqrt(
                Math.pow(patient1.diagnosis - patient2.diagnosis,2)+
                        Math.pow(patient1.fvc - patient2.fvc,2) +
                        Math.pow(patient1.volume - patient2.volume,2) +
                        Math.pow(patient1.performance - patient2.performance,2) +
                        Math.pow(patient1.pain - patient2.pain,2) +
                        Math.pow(patient1.hm - patient2.hm,2)+
                        Math.pow(patient1.ds - patient2.ds,2) +
                        Math.pow(patient1.cough - patient2.cough,2) +
                        Math.pow(patient1.weakness - patient2.weakness,2) +
                        Math.pow(patient1.tumourSize - patient2.tumourSize,2) +
                        Math.pow(patient1.diabetes - patient2.diabetes,2)+
                        Math.pow(patient1.mi - patient2.mi,2)+
                        Math.pow(patient1.pad - patient2.pad,2) +
                        Math.pow(patient1.smoking - patient2.smoking,2) +
                        Math.pow(patient1.asthma - patient2.asthma,2)+
                        Math.pow(patient1.age - patient2.age,2)


        );
    }

}