package lab1_202_15.uwaterloo.ca.lab1_202_15;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import ca.uwaterloo.sensortoy.LineGraphView;


public class MainActivity extends AppCompatActivity {

    final float ZERO = 0; //Least possible reading for each value
    final int NUMBER_OF_SAMPLES = 100; // number of values to sample

    //Set initial maximums to zero
    float lightMaximum = ZERO;
    float[] accMaxim = {ZERO,ZERO,ZERO};
    float[] magnMaxim = {ZERO,ZERO,ZERO};
    float[] rotMaxim = {ZERO,ZERO,ZERO};

    //create 2d array to store sampled accelerometer values
    float[][] accelSamples = new float[NUMBER_OF_SAMPLES][3];

    //startSampling becomes true when button is pressed;
    boolean startSampling;
    int samplingNumber; //current sampling count
    int noTimes; //Keeps count of number of times the button is pressed

    //create a graph that can be modified in both onCreate and Sensor Class
    LineGraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if there is a saved instance of the app
        if(savedInstanceState != null){
            lightMaximum = savedInstanceState.getFloat("lightMaxim");
            accMaxim = savedInstanceState.getFloatArray("accelMaxim");
            magnMaxim = savedInstanceState.getFloatArray("magnMaxim");
            rotMaxim = savedInstanceState.getFloatArray("rotMaxim");
            noTimes = savedInstanceState.getInt("rotMaxim");
            startSampling = savedInstanceState.getBoolean("startSampling");
            samplingNumber = savedInstanceState.getInt("samplingNumber");

        }
        //else just assign default values
        else{
            startSampling = false;
            noTimes = 0;
        }


        //Reference the created Linear layout and set orientation to Vertical
        LinearLayout rl = (LinearLayout) findViewById(R.id.layout);
        rl.setOrientation(LinearLayout.VERTICAL);


        //TODO Learn how to implement blank lines in Linear Layout
        TextView blankLine = new TextView(getApplicationContext());
        blankLine.setText("");
        TextView bck = new TextView(getApplicationContext());
        bck.setText("");
        TextView bl = new TextView(getApplicationContext());
        bl.setText("");
        TextView blm = new TextView(getApplicationContext());
        blm.setText("");


        //created a graph which plots the recent 100 values
        graph = new LineGraphView(getApplicationContext(),300,Arrays.asList("x", "y", "z"));
        rl.addView(graph);
        graph.setVisibility(View.VISIBLE);


        rl.addView(blm); //TODO change this



        //Created Light Sensor TextViews
        TextView lightReading = new TextView(getApplicationContext());
        lightReading.setTextColor(Color.YELLOW);
        rl.addView(lightReading);
        TextView lightReadingMax = new TextView(getApplicationContext());
        lightReadingMax.setTextColor(Color.RED);
        rl.addView(lightReadingMax);

        rl.addView(blankLine); //TODO change this

        //Created Accelerometer Sensor TextViews
        TextView accelerometerReading = new TextView(getApplicationContext());
        accelerometerReading.setTextColor(Color.YELLOW);
        rl.addView(accelerometerReading);
        TextView accelerometerReadingMax = new TextView(getApplicationContext());
        accelerometerReadingMax.setTextColor(Color.RED);
        rl.addView(accelerometerReadingMax);

        rl.addView(bck);  //TODO change this


        //Created Magnetic Sensor TextViews
        TextView magneticReading = new TextView(getApplicationContext());
        magneticReading.setTextColor(Color.YELLOW);
        rl.addView(magneticReading);
        TextView magneticReadingMax = new TextView(getApplicationContext());
        magneticReadingMax.setTextColor(Color.RED);
        rl.addView(magneticReadingMax);

        //TODO Change this
        rl.addView(bl);


        //Created Rotational Vector TextViews
        TextView rotationalReading = new TextView(getApplicationContext());
        rotationalReading.setTextColor(Color.YELLOW);
        rl.addView(rotationalReading);
        TextView rotationalReadingMax = new TextView(getApplicationContext());
        rotationalReadingMax.setTextColor(Color.RED);
        rl.addView(rotationalReadingMax);

        //Creating "RESET MAX VALUES" BUTTON
        final Button resetMax = new Button(this);
        resetMax.setText("RESET MAX VALUES");
        resetMax.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rl.addView(resetMax);


        //Creating "SAMPLE BUTTON"
        final Button sample = new Button(this);
        sample.setText("GENERATE CSV FILE");

        sample.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rl.addView(sample);

        //Creating Action Listener for RESET BUTTON
        resetMax.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Assigns zero to all maximum values

                lightMaximum = 0;

                for (int i = 0; i < 3 ; i++){
                    accMaxim[i] = ZERO;
                    magnMaxim[i] = ZERO;
                    rotMaxim[i] = ZERO;
                }
            }

        });

        //Creating Action Listener for SAMPLE BUTTON
        sample.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //sets boolean to true and sample count to zero , sampling takes place in Accelerometer class
                noTimes++;
                startSampling = true;
                samplingNumber = 0;

            }
        });

        //Requesting Sensor Manager Control
        SensorManager senMan = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Change NORMAL delay to Game for increased frequency of readings

        //Registering Light Sensor with NORMAL DELAY
        Sensor lightSensor = senMan.getDefaultSensor(Sensor.TYPE_LIGHT);
        LightSensorEventListner lightHandler = new LightSensorEventListner(lightReading, lightReadingMax);
        senMan.registerListener(lightHandler, lightSensor,senMan.SENSOR_DELAY_NORMAL);

        //Registering Acceleration Sensor Sensor with NORMAL DELAY
        Sensor accelSensor = senMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        AccelerometerSensorEventListner accelHandler = new AccelerometerSensorEventListner(accelerometerReading, accelerometerReadingMax, sample);
        senMan.registerListener(accelHandler, accelSensor, senMan.SENSOR_DELAY_NORMAL);

        //Registering Magnetic Sensor with NORMAL DELAY
        Sensor magnSensor = senMan.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        MagneticSensorEventListner magnHandler = new MagneticSensorEventListner(magneticReading, magneticReadingMax);
        senMan.registerListener(magnHandler, magnSensor, senMan.SENSOR_DELAY_NORMAL);

        //Registering Rotational Sensor with NORMAL DELAY
        Sensor rotSensor = senMan.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        Rotational rotHandler = new Rotational(rotationalReading, rotationalReadingMax);
        senMan.registerListener(rotHandler, rotSensor, senMan.SENSOR_DELAY_NORMAL);
    }

    //Light Sensor Class
    class LightSensorEventListner implements SensorEventListener {

        TextView current;
        TextView max;

        public LightSensorEventListner(TextView currentReading, TextView maxReading){
            //Constructor takes two TextViews
            current = currentReading;
            max = maxReading;

        }
        public void onAccuracyChanged(Sensor s, int i){}

        public void onSensorChanged(SensorEvent se) {

            if(se.sensor.getType() == Sensor.TYPE_LIGHT){ //Check if data stream is from the right Sensor

                //Display Current Values
                current.setText(String.format("Current Light Intensity = %.6f"  ,se.values[0]));

                //Check if current value is greater than previous maximum (absolute values)
                if(Math.abs(se.values[0]) > Math.abs(lightMaximum)) lightMaximum = se.values[0];

                //Display Historical Maximum
                max.setText(String.format("Historical Maximum = %.6f", lightMaximum));

            }
        }
    }

    //Accelerometer Sensor Class
    class AccelerometerSensorEventListner implements SensorEventListener {

        TextView current;
        TextView max;
        Button but;

        public AccelerometerSensorEventListner(TextView currentReading, TextView maxReading, Button b){
            //Constructor takes two textviews and a button to change text on button after execution of sampling
            current = currentReading;
            max = maxReading;
            but = b;
        }
        public void onAccuracyChanged(Sensor s, int i) {}

        public void onSensorChanged(SensorEvent se){

            if(se.sensor.getType() == Sensor.TYPE_ACCELEROMETER){ //Check if data streak is from the right Sensor

                //Display Current Values
                current.setText(String.format("Current Acceleration = {%.6f,%.6f,%.6f}"  ,se.values[0], se.values[1],se.values[2]));

                //Check if current values are greter than previous maximum(absolute value)

                if(Math.abs(se.values[0]) > Math.abs(accMaxim[0])) accMaxim[0] = se.values[0];
                if(Math.abs(se.values[1]) > Math.abs(accMaxim[1])) accMaxim[1] = se.values[1];
                if(Math.abs(se.values[2]) > Math.abs(accMaxim[2])) accMaxim[2] = se.values[2];

                //Display Historical Maximum
                max.setText(String.format("Historical Maximum = {%.6f,%.6f,%.6f}",accMaxim[0],accMaxim[1],accMaxim[2]));

                //Add Values to graph
                graph.addPoint(se.values);

                if(startSampling && samplingNumber < NUMBER_OF_SAMPLES) {

                    //keeps adding values into samples 2d array until samplingNumber hits NUMBER_OF_SAMPLES
                    accelSamples[samplingNumber][0] = se.values[0];
                    accelSamples[samplingNumber][1] = se.values[1];
                    accelSamples[samplingNumber][2] = se.values[2];
                    samplingNumber++;

                }
                else if(noTimes != 0){

                    writeToFile(); //write to file after sampling is complete

                    startSampling = false; //set current sampling status to false

                    but.setText("Sampling Complete, Press to Re-Sample"); //Tell user that sampling is complete

                }
            }
        }
    }

    //Magnetic Sensor Class
    class MagneticSensorEventListner implements SensorEventListener {

        TextView current;
        TextView max;


        public MagneticSensorEventListner (TextView currentReading, TextView maxReading){
            //Constructor takes two Textviews
            current = currentReading;
            max = maxReading;
        }
        public void onAccuracyChanged(Sensor s, int i){ }

        public void onSensorChanged(SensorEvent se){

            if(se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){ //Check if data stream is from the right Sensor

                //Display Current Values
                current.setText(String.format("Current Magnetic Field = {%.6f,%.6f,%.6f}", se.values[0], se.values[1], se.values[2]));

                //Check if current values are greater than historical maximums
                if(Math.abs(se.values[0]) > Math.abs(magnMaxim[0])) magnMaxim[0] = se.values[0];
                if(Math.abs(se.values[1]) > Math.abs(magnMaxim[1])) magnMaxim[1] = se.values[1];
                if(Math.abs(se.values[2]) > Math.abs(magnMaxim[2])) magnMaxim[2] = se.values[2];

                //Display Historical Maximums
                max.setText(String.format("Historical Maximum = {%.6f,%.6f,%.6f}",magnMaxim[0],magnMaxim[1],magnMaxim[2]));
            }
        }
    }

    //Rotational Sensor Class
    class Rotational implements SensorEventListener {

        TextView current;
        TextView max;

        public Rotational (TextView currentReading, TextView maxReading){
            //Constructor takes teo Text Views
            current = currentReading;
            max = maxReading;

        }
        public void onAccuracyChanged(Sensor s, int i){ }

        public void onSensorChanged(SensorEvent se){

            if(se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){ //Checks if data stream is from the right sensor

                //Display Current Values
                current.setText(String.format("Current Rotational Vector = {%.6f,%.6f,%.6f}", se.values[0], se.values[1], se.values[2]));

                //Check if current Values are greater than the maximum balues
                if(Math.abs(se.values[0]) > Math.abs(rotMaxim[0])) rotMaxim[0] = se.values[0];
                if(Math.abs(se.values[1]) > Math.abs(rotMaxim[1])) rotMaxim[1] = se.values[1];
                if(Math.abs(se.values[2]) > Math.abs(rotMaxim[2])) rotMaxim[2] = se.values[2];

                //Display Maximum Values
                max.setText(String.format("Historical Maximum = {%.6f,%.6f,%.6f}",rotMaxim[0],rotMaxim[1],rotMaxim[2]));
            }
        }
    }

    protected void onSaveInstanceState(Bundle b){
        //Bundle is temp memory made to save data so that it doesn't gets destroyed during a screen rotate
        super.onSaveInstanceState(b);
        b.putFloat("lightMaxim", lightMaximum);
        b.putFloatArray("accelMaxim", accMaxim);
        b.putFloatArray("magnMaxim", magnMaxim);
        b.putFloatArray("rotMaxim",rotMaxim);
        b.putInt("noTimes", noTimes);
        b.putBoolean("startSampling",startSampling);
        b.putInt("samplingNumber", samplingNumber);


    }


    private void writeToFile(){
        File file = null; //creates variable for file
        PrintWriter writer = null; //creates writer
        try{
            file = new File(getExternalFilesDir("Accelerometer"), "readings.csv"); //creates and gets fast writing to csv file
            writer = new PrintWriter(file); // creates gives the advantage of unbuffered writing efficiency

            //writes array of last 100 values of the accelerometer to the file
            writer.println("X-Axis, Y-Axis, Z-Axis");
            for (int i=0; i<100;i++){
                writer.println(String.format("%.6f, %.6f, %.6f,", accelSamples[i][0], accelSamples[i][1], accelSamples[i][2]));
            }
        } catch(IOException e){
            Log.d("Write Warning", "File Write Fail: " + e.toString()); //catches error if file not found
        } finally{
            writer.flush();
            writer.close();
        }
    }
}

