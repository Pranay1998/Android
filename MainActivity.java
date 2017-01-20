package lab1_202_15.uwaterloo.ca.lab1_202_15;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;







public class MainActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.label1);
        LinearLayout layout2 = (LinearLayout)findViewById(R.id.label2);
        TextView tv1 = new TextView(getApplicationContext());
        layout2.addView(tv1);


        SensorManager lightSenMan = (SensorManager) getSystemService(SENSOR_SERVICE);

        Sensor lightSensor = lightSenMan.getDefaultSensor(Sensor.TYPE_LIGHT);

        LightSensorManager lightHandler = new LightSensorManager(tv, tv1);

        lightSenMan.registerListener(lightHandler, lightSensor,lightSenMan.SENSOR_DELAY_NORMAL);
        
        SensorManager accSenMan = (SensorManager) getSystemService(SENSOR_SERVICE);

        Sensor accSensor = accSenMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



    }
}

class LightSensorManager implements SensorEventListener {

    TextView tv1;
    TextView tv2;


    public LightSensorManager (TextView targetTV1, TextView targetTV2){
        this.tv1 = targetTV1;
        this.tv2 = targetTV2;
    }

    public void onAccuracyChanged(Sensor s, int i){ }

    public void onSensorChanged(SensorEvent se){

        if(se.sensor.getType() == Sensor.TYPE_LIGHT){


            tv1.setText("Current Light Intensity is "+ Float.toString(se.values[0]));
            tv1.setTextSize(10);


            if(se.values[0] <= 5f){
                tv2.setText("Ambient Light Too Low");
                tv2.setTextSize(10);
                tv2.setTextColor(Color.YELLOW);
            }
            else if (se.values[0] >= 350 ){
                tv2.setText("Ambient Light Too High");
                tv2.setTextSize(10);
                tv2.setTextColor(Color.RED);
            }
            else{
                tv2.setText("Ambient Light Normal");
                tv2.setTextSize(10);
                tv2.setTextColor(Color.GREEN);
            }
        }
    };
}

class AccSensorManager implements SensorEventListener {


    public void onAccuracyChanged(Sensor s, int i){ }

    public void onSensorChanged(SensorEvent se) {

        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER){





        }

    }
}