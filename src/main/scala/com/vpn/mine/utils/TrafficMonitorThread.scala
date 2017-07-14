package com.vpn.mine.utils

import java.io.{File, IOException}
import java.nio.{ByteBuffer, ByteOrder}
import java.util.concurrent.Executors

import android.content.Context
import android.net.{LocalServerSocket, LocalSocket, LocalSocketAddress}
import android.util.Log

/**
  * Created by coder on 17-7-13.
  */
class TrafficMonitorThread(context: Context)  extends Thread{

  val TAG = "TrafficMonitorThread"
  lazy val PATH = context.getApplicationInfo.dataDir + "/stat_path"

  @volatile var serverSocket: LocalServerSocket = _
  @volatile var isRunning: Boolean = true

  def closeServerSocket() {
    if (serverSocket != null) {
      try {
        serverSocket.close()
      } catch {
        case _: Exception => // ignore
      }
      serverSocket = null
    }
  }

  def stopThread() {
    isRunning = false
    closeServerSocket()
  }

  override def run() {

    new File(PATH).delete()

    try {
      val localSocket = new LocalSocket
      localSocket.bind(new LocalSocketAddress(PATH, LocalSocketAddress.Namespace.FILESYSTEM))
      serverSocket = new LocalServerSocket(localSocket.getFileDescriptor)
    } catch {
      case e: IOException =>
        Log.e(TAG, "unable to bind", e)
        return
    }

    val pool = Executors.newFixedThreadPool(1)
    while (isRunning) {
      try {
        val socket = serverSocket.accept()

        pool.execute(() => {
          try {
            val input = socket.getInputStream
            val output = socket.getOutputStream

            val buffer = new Array[Byte](16)
            if (input.read(buffer) != 16) throw new IOException("Unexpected traffic stat length")
            val stat = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN)
            TrafficMonitor.update(stat.getLong(0), stat.getLong(8))

            output.write(0)

            input.close()
            output.close()

          } catch {
            case e: Exception =>
              Log.e(TAG, "Error when recv traffic stat", e)
          }

          // close socket
          try {
            socket.close()
          } catch {
            case _: Exception => // ignore
          }

        })
      } catch {
        case e: IOException =>
          Log.e(TAG, "Error when accept socket", e)
          return
      }
    }
  }
}
