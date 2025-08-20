package com.keevol.keewebx

import com.keevol.config.Konfig
import com.keevol.goodies.Presets
import org.apache.commons.lang3.{StringUtils, Strings}
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory

import java.util.concurrent.atomic.AtomicReference

/**
 * @author {@link afoo.me}
 */
object KeewebxGlobals {

  private val logger = LoggerFactory.getLogger("KeewebxGlobalsLogger")

  Presets.apply()

  val config: AtomicReference[Konfig] = new AtomicReference[Konfig]()
  config.set(new Konfig(getClass.getClassLoader.getResourceAsStream("application.properties")))

  def isProductionEnv(): Boolean = {
    if (config.get() == null) return false
    val profile = config.get().get("profile")
    Strings.CI.equals(profile, "production") || Strings.CI.equals(profile, "prod") || Strings.CI.equals(profile, "prd")
  }
}