package com.keevol.keewebx.htmx

import io.vertx.ext.web.RoutingContext
import org.apache.commons.lang3.StringUtils

/**
 * @author {@link afoo.me}
 */
object HTMX {
  // ----------------------- Request Utilities -----------------------------
  def isHtmxRequest(ctx: RoutingContext): Boolean = ctx.request().headers().contains("HX-Request")

  def isHtmxBoosted(ctx: RoutingContext): Boolean = ctx.request().headers().contains("HX-Boosted")

  def currentURL(ctx: RoutingContext): Option[String] = option(getHeaderValueOf("HX-Current-URL", ctx))

  def getPrompt(ctx: RoutingContext): Option[String] = option(getHeaderValueOf("HX-Prompt", ctx))

  def isHistoryRestoreRequest(ctx: RoutingContext): Boolean = {
    val value = getHeaderValueOf("HX-History-Restore-Request", ctx)
    StringUtils.isNotEmpty(value) && value.toBoolean
  }

  /**
   * @alias for #getTargetElementId
   */
  def getTarget(ctx: RoutingContext): Option[String] = getTargetElementId(ctx)

  def getTargetElementId(ctx: RoutingContext): Option[String] = option(getHeaderValueOf("HX-Target", ctx))

  /*	the name of the triggered element if it exists */
  def getTriggerName(ctx: RoutingContext): Option[String] = option(getHeaderValueOf("HX-Trigger-Name", ctx))

  /* the id of the triggered element if it exists */
  def getTrigger(ctx: RoutingContext): Option[String] = option(getHeaderValueOf("HX-Trigger", ctx))


  // ----------------------- Response Utilities -----------------------------
  def setLocation(ctx: RoutingContext, location: String): Unit = ctx.response().putHeader("HX-Location", location)

  def setPushUrl(ctx: RoutingContext, url: String): Unit = ctx.response().putHeader("HX-Push-Url", url)

  def setRedirect(ctx: RoutingContext, url: String): Unit = ctx.response().putHeader("HX-Redirect", url)

  def setRefresh(ctx: RoutingContext): Unit = ctx.response().putHeader("HX-Refresh", "true")

  def setReplaceUrl(ctx: RoutingContext, url: String): Unit = ctx.response().putHeader("HX-Replace-Url", url)

  /* possible values: https://htmx.org/attributes/hx-swap/ */
  def setReswap(ctx: RoutingContext, value: String): Unit = ctx.response().putHeader("HX-Reswap", value)

  def setRetarget(ctx: RoutingContext, retargetTo: String): Unit = ctx.response().putHeader("HX-Retarget", retargetTo)

  def setReselect(ctx: RoutingContext, value: String): Unit = ctx.response().putHeader("HX-Reselect", value)

  def setTrigger(ctx: RoutingContext, trigger: String): Unit = ctx.response().putHeader("HX-Trigger", trigger)

  def setTriggerAfterSettle(ctx: RoutingContext, trigger: String): Unit = ctx.response().putHeader("HX-Trigger-After-Settle", trigger)

  def setTriggerAfterSwap(ctx: RoutingContext, trigger: String): Unit = ctx.response().putHeader("HX-Trigger-After-Swap", trigger)


  // ----------------------- Helpers Utilities -----------------------------
  private def getHeaderValueOf(header: String, ctx: RoutingContext): String = StringUtils.trimToEmpty(ctx.request().getHeader(header))

  private def option(value: String): Option[String] = if (StringUtils.isEmpty(value)) None else Some(value)
}