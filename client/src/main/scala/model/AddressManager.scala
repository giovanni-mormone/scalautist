package model

import com.typesafe.config.ConfigFactory

object AddressManager{

  private val LOCAL_KEY: String = "server.address.local"
  private val REMOTE_KEY: String = "server.address.remote"

  var address:String = ConfigFactory.defaultApplication().getString(LOCAL_KEY)

  def remoteServer():Unit = {
    address = ConfigFactory.defaultApplication().getString(REMOTE_KEY)
  }
}