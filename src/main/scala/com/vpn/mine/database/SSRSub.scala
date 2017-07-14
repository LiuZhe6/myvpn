package com.vpn.mine.database

import com.j256.ormlite.field.DatabaseField

/**
  * Created by coder on 17-7-13.
  */
class SSRSub {
  @DatabaseField(generatedId = true)
  var id: Int = 0

  @DatabaseField
  var url: String = ""

  @DatabaseField
  var url_group: String = ""
}
