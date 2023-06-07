package tw.edu.pu.s1103018.afinal

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var sm: SensorManager? = null
    var sensor: Sensor? = null
    var lastX = 0f
    var lastY = 0f
    var lastZ = 0f
    var S = 0.0
    var lastTime: Long = 0
    var t0: TextView? = null
    var t1: TextView? = null
    var tv: TextView? = null
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        t0 = findViewById<View>(R.id.txv2) as TextView
        t1 = findViewById<View>(R.id.txv4) as TextView
        tv = findViewById<View>(R.id.txv5) as TextView

        sm = this.getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sm!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sm!!.registerListener(SL, sensor, SensorManager.SENSOR_DELAY_GAME)
    }

    private val SL: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val currentTime = System.currentTimeMillis()
            val xTime = currentTime - lastTime
            if (xTime < 200) return
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val dx = x - lastX
            val dy = y - lastY
            val dz = z - lastZ
            S = Math.sqrt(
                Math.pow(dx.toDouble(), 2.0) + Math.pow(
                    dy.toDouble(),
                    2.0
                ) + Math.pow(dz.toDouble(), 2.0) / xTime *1000
            )
            if (S >= 150 && S <= 200);
            run {
                tv!!.text = "初位置:$x,$y,$z，末位置$dx,$dy,$dz"
                i++
                t0!!.text = i.toString()
                t1!!.text =
                    "當前觸發時間:$currentTime，上次觸發時間$lastTime，觸發間隔:$xTime，速度值為$S"
            }
            lastTime = currentTime
            lastX = x
            lastY = y
            lastZ = z
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    protected fun onDestory() {
        super.onDestroy()
        sm!!.unregisterListener(SL)
    }
}