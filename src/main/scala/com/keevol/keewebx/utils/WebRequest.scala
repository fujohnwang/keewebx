package com.keevol.keewebx.utils

import io.vertx.ext.web.RoutingContext
import org.apache.commons.lang3.StringUtils

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

object WebRequest {

  def header(ctx: RoutingContext, headerName: String): String = StringUtils.trimToEmpty(ctx.request().getHeader(headerName))

  def param(ctx: RoutingContext, name: String): String = StringUtils.trimToEmpty(ctx.request().getParam(name))

  def body(ctx: RoutingContext): String = StringUtils.trimToEmpty(ctx.body().asString())

  def body(ctx: RoutingContext, encoding: String) = StringUtils.trimToEmpty(ctx.body().asString(encoding))

}