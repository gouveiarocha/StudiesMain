package com.gouveia.studiesmain.pps.proximity_sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PowerManager
import android.view.View
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentPpsSensorBinding

// 1- FUNCIONAMENTO DO SENSOR MANAGER - https://developer.android.com/guide/topics/sensors/sensors_position?hl=pt-br
// 2- DESLIGAR E LIGAR A TELA DO APARELHO DE ACORDO COM A PROXIMIDADE - https://www.appsloveworld.com/kotlin/100/74/how-to-turn-screen-off-based-on-proximity-sensor
// Explicação: Iniciei essa poc com o objetivo final de desligar a tela de acordo com a
// proximidade, primeiro, pensei em pegar o sensor de proximidade e nele tratar o desligamento da
// tela, porém, ao partir para essa implementação, descobri que há um recurso que já faz isso, sem
// precisar usar o sensor. Para isso, basta usar o WakeLock. Para fins de estudo, vamos manter
// ambas implementações.

class PpsSensor : Fragment(R.layout.fragment_pps_sensor), SensorEventListener {

    private lateinit var binding: FragmentPpsSensorBinding

    // SENSORMANAGER
    private lateinit var sensorManager: SensorManager
    private var sensorProximity: Sensor? = null
    private var sensorLuminosity: Sensor? = null

    // A seguir, criamos uma lista para colocar os sensores e facilitar na hr de reg os listeners
    private lateinit var sensors: Array<Sensor?>

    // POWERMANAGER (PARA LIGAR\DESLIGAR O DISPLAY)
    private lateinit var powerManager: PowerManager
    private lateinit var lock: PowerManager.WakeLock

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPpsSensorBinding.bind(view)

        // Instancia o SensorManager.
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Com o SensorManager, pegamos os sensores especificos que precisamos.
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        sensorLuminosity = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        // Adicionamos os sensores na lista -> Refatorar? Verificar como definir esse array no
        // escopo da classe.
        sensors = arrayOf(sensorProximity, sensorLuminosity)

        // Instancia o PowerManager.
        powerManager = requireContext().getSystemService(Context.POWER_SERVICE) as PowerManager
        // Com o PowerManager, pegamos o lock.
        lock = powerManager.newWakeLock(
            PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK,
            "simplewakelock:wakelocktag"
        )

        // Enable: Faz o bloqueio da tela se ainda não tiver sido bloqueada.
        if (!lock.isHeld) lock.acquire()

    }

    // OUVINTE DO SENSOR -> SENSOR
    override fun onSensorChanged(event: SensorEvent) {
        val value = event.values[0].toString()
        when (event.sensor.type) {
            // O Sensor de Proximidade retorna 0(proximo) ou 5(longe) em (cm)
            Sensor.TYPE_PROXIMITY -> binding.ppsSensorProximityTxt.text = value
            // O Sensor de Luminosidade retorna em (lux)
            Sensor.TYPE_LIGHT -> binding.ppsSensorLuminosityTxt.text = value
            else -> return
        }

    }

    // OUVINTE DO SENSOR -> PRECISÃO
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        // Registra os listeners.
        sensors.forEach { sensor ->
            sensor.let {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }

//        sensorProximity.also { proximity ->
//            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_UI)
//        }
//        sensorLuminosity.also { luminosity ->
//            sensorManager.registerListener(this, luminosity, SensorManager.SENSOR_DELAY_UI)
//        }

    }

    override fun onPause() {
        super.onPause()
        // Desregistra o listener.
        sensorManager.unregisterListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Disable: Solte o bloqueio se ainda não foi liberado
        if (lock.isHeld) lock.release()
    }

}