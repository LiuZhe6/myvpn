package com.vpn.mine

import java.io._
import java.lang.System.currentTimeMillis
import java.util.concurrent.Semaphore
import android.util.Log

import scala.collection.JavaConversions._
import scala.collection.immutable.Stream
import scala.util.control.Exception._

/**
  * Created by coder on 17-7-14.
  */
class StreamLogger(is: InputStream, tag: String) extends Thread {

  def withCloseable[T <: Closeable, R](t: T)(f: T => R): R = {
    allCatch.andFinally{t.close} apply { f(t) }
  }

  override def run() {
    withCloseable(new BufferedReader(new InputStreamReader(is))) {
      br => try Stream.continually(br.readLine()).takeWhile(_ != null).foreach(Log.i(tag, _)) catch {
        case ignore: IOException =>
      }
    }
  }
}

/**
  * @author ayanamist@gmail.com
  */
class GuardedProcess(cmd: Seq[String]) {
  private val TAG = classOf[GuardedProcess].getSimpleName

  @volatile private var guardThread: Thread = _
  @volatile private var isDestroyed: Boolean = _
  @volatile private var process: Process = _
  @volatile private var isRestart = false

  def start(onRestartCallback: () => Unit = null): GuardedProcess = {
    val semaphore = new Semaphore(1)
    semaphore.acquire
    @volatile var ioException: IOException = null

    guardThread = new Thread(() => {
      try {
        var callback: () => Unit = null
        while (!isDestroyed) {
          Log.i(TAG, "start process: " + cmd)
          val startTime = currentTimeMillis

          process = new ProcessBuilder(cmd).redirectErrorStream(true).start

          val is = process.getInputStream
          new StreamLogger(is, TAG).start

          if (callback == null) callback = onRestartCallback else callback()

          semaphore.release
          process.waitFor

          this.synchronized {
            if (isRestart) {
              isRestart = false
            } else {
              if (currentTimeMillis - startTime < 1000) {
                Log.w(TAG, "process exit too fast, stop guard: " + cmd)
                isDestroyed = true
              }
            }
          }

        }
      } catch {
        case ignored: InterruptedException =>
          Log.i(TAG, "thread interrupt, destroy process: " + cmd)
          process.destroy()
        case e: IOException => ioException = e
      } finally semaphore.release
    }, "GuardThread-" + cmd)

    guardThread.start()
    semaphore.acquire

    if (ioException != null) {
      throw ioException
    }

    this
  }

  def destroy() {
    isDestroyed = true
    guardThread.interrupt()
    process.destroy()
    try guardThread.join() catch {
      case ignored: InterruptedException =>
    }
  }

  def restart() {
    this.synchronized {
      isRestart = true
      process.destroy()
    }
  }

  @throws(classOf[InterruptedException])
  def waitFor = {
    guardThread.join()
    0
  }
}

