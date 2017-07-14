package com.vpn.mine

import java.io.{File, FileDescriptor, IOException}
import java.util.concurrent.Executors

import android.net.{LocalServerSocket, LocalSocket, LocalSocketAddress}
import android.util.Log

/**
  * Created by coder on 17-7-14.
  */
object ShadowsocksVpnThread {
  val getInt = classOf[FileDescriptor].getDeclaredMethod("getInt$")
}

class ShadowsocksVpnThread(vpnService: MyVpnService) extends Thread {
  import ShadowsocksVpnThread._

  val TAG = "ShadowsocksVpnService"
  lazy val PATH = vpnService.getApplicationInfo.dataDir + "/protect_path"

  @volatile var isRunning: Boolean = true
  @volatile var serverSocket: LocalServerSocket = _

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

    val pool = Executors.newFixedThreadPool(4)

    while (isRunning) {
      try {
        val socket = serverSocket.accept()

        pool.execute(() => {
          try {
            val input = socket.getInputStream
            val output = socket.getOutputStream

            input.read()

            val fds = socket.getAncillaryFileDescriptors

            if (fds.nonEmpty) {
              val fd = getInt.invoke(fds(0)).asInstanceOf[Int]
              val ret = vpnService.protect(fd)

              // Trick to close file decriptor
              System.jniclose(fd)

              if (ret) {
                output.write(0)
              } else {
                output.write(1)
              }
            }

            input.close()
            output.close()

          } catch {
            case e: Exception =>
              Log.e(TAG, "Error when protect socket", e)
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

