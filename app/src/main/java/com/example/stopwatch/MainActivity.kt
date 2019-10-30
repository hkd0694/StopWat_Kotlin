package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //버튼을 누르는순간 isRunning의 boolean 타입에 맞게 start()함수 및 pause() 함수를 호출합니다.
        fab.setOnClickListener {
            isRunning = !isRunning
            if(isRunning) start()
            else pause()
        }

    }

    private fun start(){
        fab.setImageResource(R.drawable.ic_pause_black_24dp)
        //timer는 워커 스레드에서 동작하기 땨문에 UI 동작이 불가능합니다.
        timerTask = timer(period = 10) {
            time++
            val sec = time / 100
            val milli = time % 100
            //그렇기 때문에 runOnUiThread로 감싸서 UI 조작이 가능하게 해줄 수 있습니다.
            runOnUiThread {
                secTextView.text = "$sec"
                milliTextView.text = "$milli"
            }
        }
    }

    private fun pause(){
        fab.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        timerTask?.cancel()
    }
}
