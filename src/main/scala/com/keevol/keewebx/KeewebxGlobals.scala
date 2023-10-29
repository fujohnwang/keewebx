package com.keevol.keewebx

import com.keevol.config.Konfig
import org.apache.commons.lang3.StringUtils

import java.util.concurrent.atomic.AtomicReference

/**
 * @author {@link afoo.me}
 */
object KeewebxGlobals {

  val config: AtomicReference[Konfig] = new AtomicReference[Konfig]()
  config.set(new Konfig(getClass.getClassLoader.getResourceAsStream("application.properties")))

  def isProductionEnv(): Boolean = {
    if (config.get() == null) return false
    val profile = config.get().get("profile")
    StringUtils.equalsIgnoreCase(profile, "production") || StringUtils.equalsIgnoreCase(profile, "prod") || StringUtils.equalsIgnoreCase(profile, "prd")
  }
}