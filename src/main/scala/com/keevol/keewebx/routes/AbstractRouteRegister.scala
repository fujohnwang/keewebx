package com.keevol.keewebx.routes

import com.keevol.kate.RouteRegister
import com.keevol.keewebx.templating.Jte
import com.keevol.keewebx.utils.{Handlers, WebResponse}
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.{Router, RoutingContext}
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.{Logger, LoggerFactory}

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

abstract class AbstractRouteRegister extends RouteRegister {
  val logger: Logger = LoggerFactory.getLogger(classOf[AbstractRouteRegister])

  def render(ctx: RoutingContext, templatePath: String, templateDataMap: JsonObject): Unit = WebResponse.html(ctx, Jte.render(templatePath, templateDataMap))

  def respondWithJson(ctx: RoutingContext, json: JsonObject): Unit = WebResponse.json(ctx, json)
}

/**
 * you can apply similar logic in your own route register impl. or use this one directly.
 *
 * @param routes to register to router.
 */
class DefaultRouteRegister(routes: Array[(String, Handler[RoutingContext])]) extends AbstractRouteRegister {
  override def apply(router: Router): Unit = {
    routes.foreach(route => router.route(route._1).handler(Handlers.chain(route._2)))
  }
}