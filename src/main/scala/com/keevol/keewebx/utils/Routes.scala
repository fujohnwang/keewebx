package com.keevol.keewebx.utils

import scala.collection.JavaConverters._

import java.util.concurrent.ConcurrentHashMap

/**
 * {{{
 *   _                                    _
 *  | |                                  | |
 *  | | __   ___    ___  __   __   ___   | |
 *  | |/ /  / _ \  / _ \ \ \ / /  / _ \  | |
 *  |   <  |  __/ |  __/  \ V /  | (_) | | |
 *  |_|\_\  \___|  \___|   \_/    \___/  |_|
 * }}}
 *
 * KEEp eVOLution!
 *
 * @author fq@keevol.cn
 * @since 2017.5.12
 *
 *        Copyright 2017 © 杭州福强科技有限公司版权所有
 *        [[https://www.keevol.cn]]
 */

object Routes {

  private val routesCache: ConcurrentHashMap[String, String] = new ConcurrentHashMap[String, String]()

  def trace(route: String, method: String = "*"): String = {
    routesCache.put(route, method)
    route
  }

  def dump(): String = routesCache.asScala.map(e => s"${e._1} -> ${e._2}").mkString("\n")
}