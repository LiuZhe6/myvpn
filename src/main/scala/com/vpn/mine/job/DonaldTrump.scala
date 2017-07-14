package com.vpn.mine.job

import android.util.Log
import com.evernote.android.job.JobCreator

/**
  * Created by coder on 17-7-13.
  */
object DonaldTrump extends JobCreator {
  def create(tag: String) = {
    val parts = tag.split(":")
    parts(0) match {
      case AclSyncJob.TAG => new AclSyncJob(parts(1))
      case SSRSubUpdateJob.TAG => new SSRSubUpdateJob()
      case _ =>
        Log.w("DonaldTrump", "Unknown job tag: " + tag)
        null
    }
  }
}
