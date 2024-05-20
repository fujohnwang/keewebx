package com.keevol.keewebx.utils

import com.keevol.kate.utils.ResponseUtils
import io.vertx.core.json.{Json, JsonObject}
import io.vertx.ext.web.RoutingContext


/**
 * enrich {@link com.keevol.kate.utils.ResponseUtils} if necessary
 *
 * @author {@link afoo.me}
 *
 */
object WebResponse {

  def ok(ctx: RoutingContext, message: String = ""): Unit = ctx.response().end(message)

  def internalError(ctx: RoutingContext, message: String = ""): Unit = ctx.response().setStatusCode(500).end(message)

  def json(ctx: RoutingContext, payload: JsonObject): Unit = ResponseUtils.json(ctx, payload, 200)

  def html(ctx: RoutingContext, html: String) = ResponseUtils.html(ctx, html, 200)

  def notFound(ctx: RoutingContext): Unit = ctx.response().setStatusCode(404).end()

  def notAuthenticated(ctx: RoutingContext): Unit = ctx.response().setStatusCode(401).end()

  def notAuthorized(ctx: RoutingContext): Unit = ctx.response().setStatusCode(403).end()

  def forbidden(ctx: RoutingContext): Unit = notAuthorized(ctx)

  def badRequest(ctx: RoutingContext): Unit = ctx.response().setStatusCode(400).setStatusMessage("bad request").end()

  def accepted(ctx: RoutingContext): Unit = ctx.response().setStatusCode(202).setStatusMessage("Accepted").end()

  def noContent(ctx: RoutingContext): Unit = ctx.response().setStatusCode(204).end()
}


