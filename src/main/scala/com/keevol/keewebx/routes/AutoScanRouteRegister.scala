package com.keevol.keewebx.routes

import com.keevol.kate.RouteRegister
import io.vertx.ext.web.Router

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
 * @author fq@keevol.com
 * @since 2017.5.12
 *
 *        Copyright 2017 © 杭州福强科技有限公司版权所有
 *        [[https://www.keevol.com]]
 */

class AutoScanRouteRegister(scanPath: String) extends RouteRegister {
  /**
   * auto-scan classpath with library https://github.com/classgraph/classgraph
   *
   * https://github.com/classgraph/classgraph/wiki/Code-examples
   *
   * @param router
   */
  override def apply(router: Router): Unit = {
    // TODO
  }
}