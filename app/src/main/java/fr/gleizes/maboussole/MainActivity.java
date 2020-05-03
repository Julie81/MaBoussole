package fr.gleizes.maboussole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CompasView mCompasView;

    //gestionnaire des capteurs
    private SensorManager sensorManager;

    //capteur de la boussole numérique
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //on récupère notre vue compasView
        mCompasView = (CompasView) findViewById(R.id.compasView);

        //récupération du sensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //on demande la liste des acpteurs de type boussole
        List <Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

        //on ne garde quele premier
        if (sensors.size() > 0) {
            mSensor = sensors.get(0);
        }
    }

    //listener sur le capteur de la boussole numérique
    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mCompasView.setNorthOrientation(event.values[SensorManager.DATA_X]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //on lie les évènements de la boussole numérique au listener
        sensorManager.registerListener(sensorListener, mSensor, sensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //on retire le lien entre le Listener et la boussole numérique
        sensorManager.unregisterListener(sensorListener);
    }
}
