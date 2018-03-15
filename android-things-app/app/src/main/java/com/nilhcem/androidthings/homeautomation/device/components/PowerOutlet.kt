package com.nilhcem.androidthings.homeautomation.device.components

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.UartDevice
import com.nilhcem.androidthings.homeautomation.device.data.model.Outlet

class PowerOutlet : Component<Outlet>(), LifecycleObserver {

    companion object {
        private const val UART_NAME = "UART0"
    }

    private var uartDevice: UartDevice? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        openUart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        closeUart()
    }

    override fun onStateChanged(prevState: Outlet?, newState: Outlet) {
        if (newState.on != prevState?.on) {
            if (newState.on) on() else off()
        }
    }

    private fun on() = sendUart('1')
    private fun off() = sendUart('0')

    private fun openUart() {
        uartDevice = PeripheralManager.getInstance().openUartDevice(UART_NAME).apply {
            setBaudrate(9600)
            setDataSize(8)
            setParity(UartDevice.PARITY_NONE)
            setStopBits(1)
        }
    }

    private fun closeUart() {
        uartDevice?.close().also { uartDevice = null }
    }

    private fun sendUart(char: Char) {
        uartDevice?.write(byteArrayOf(char.toByte()), 1)
    }
}
