package com.keevol.keewebx.utils

import com.keevol.kate.utils.ResponseUtils
import io.vertx.ext.web.RoutingContext


/**
 * enrich {@link com.keevol.kate.utils.ResponseUtils} if necessary
 *
 * @author {@link afoo.me}
 *
 */
object WebResponse {
  def ok(ctx: RoutingContext, html: String) = ResponseUtils.html(ctx, html, 200)
}


