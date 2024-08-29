package com.keevol.keewebx.utils

import io.vertx.core.http.HttpHeaders
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
 * @author fq@keevol.cn
 * @since 2017.5.12
 *
 *        Copyright 2017 © 杭州福强科技有限公司版权所有
 *        [[https://www.keevol.cn]]
 */

object BearerAuthorization {

  def authorized(ctx: RoutingContext, authorizationAccessToken: String): Boolean = {
    // check authorization first
    val authorization = StringUtils.trimToEmpty(ctx.request().headers().get(HttpHeaders.AUTHORIZATION))
    val parts = StringUtils.split(authorization, " ")
    val tokenOption = if (parts.length == 2) Some(StringUtils.trimToEmpty(parts(1))) else None

    if (!tokenOption.isDefined || !StringUtils.equals(tokenOption.get, authorizationAccessToken)) {
      false
    } else {
      true
    }
  }
}