package com.vpn.mine.utils

import java.io.{FileWriter, InputStream, OutputStream}
import com.vpn.mine.utils.CloseUtils._
/**
  * Created by coder on 17-7-13.
  */
object IOUtils {
  private final val BUFFER_SIZE = 32 * 1024

  def copy(in: InputStream, out: OutputStream) {
    val buffer = new Array[Byte](BUFFER_SIZE)
    while (true) {
      val count = in.read(buffer)
      if (count >= 0) out.write(buffer, 0, count) else return
    }
  }

  def readString(in: InputStream): String = {
    val builder = new StringBuilder()
    val buffer = new Array[Byte](BUFFER_SIZE)
    while (true) {
      val count = in.read(buffer)
      if (count >= 0) builder.append(new String(buffer, 0, count)) else return builder.toString()
    }
    null
  }

  def writeString(file: String, content: String) = autoClose(new FileWriter(file))(writer => writer.write(content))
}
