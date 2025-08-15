package com.keevol.keewebx.htmx

import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import org.apache.commons.lang3.StringUtils

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * This is the utility class implementation mentioned in ebook 'Unveil HTMX'(HTMX揭秘){@link https:// store.afoo.me / l / htmx}
 *
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

  /**
   * https://htmx.org/headers/hx-trigger/
   *
   * 需要客户端有响应的event handler定义与之配合：
   * <code>
   * document.body.addEventListener("myEvent", function(evt){
   *     alert("myEvent was triggered!");
   * })
   * </code>
   * @param ctx Web Handle
   * @param triggerEvent the trigger event definition
   */
  def setTrigger(ctx: RoutingContext, trigger: String): Unit = ctx.response().putHeader("HX-Trigger", trigger)

  def setTrigger(ctx: RoutingContext, triggerEvent: HxTrigger): Unit = setTriggerEvent(ctx, "HX-Trigger", triggerEvent)

  def setTriggerAfterSettle(ctx: RoutingContext, trigger: String): Unit = ctx.response().putHeader("HX-Trigger-After-Settle", trigger)

  def setTriggerAfterSettle(ctx: RoutingContext, triggerEvent: HxTrigger): Unit = setTriggerEvent(ctx, "HX-Trigger-After-Settle", triggerEvent)

  def setTriggerAfterSwap(ctx: RoutingContext, trigger: String): Unit = ctx.response().putHeader("HX-Trigger-After-Swap", trigger)

  def setTriggerAfterSwap(ctx: RoutingContext, triggerEvent: HxTrigger): Unit = setTriggerEvent(ctx, "HX-Trigger-After-Swap", triggerEvent)

  def triggerEvent(ctx: RoutingContext, eventName: String, message: String, timing: Option[String] = Some("HX-Trigger")): Unit = {
    val triggerHeader = timing match {
      case Some(header) => header
      case None => "HX-Trigger-After-Settle"
    }
    setTriggerEvent(ctx, triggerHeader, HxTrigger(eventName, eventValue = Some(message)))
  }

  private def setTriggerEvent(ctx: RoutingContext, triggerHeader: String, triggerEvent: HxTrigger): Unit = {
    if (triggerEvent.eventDetail.isEmpty && triggerEvent.eventValue.isEmpty) {
      ctx.response().putHeader(triggerHeader, encodeHeaderValue(triggerEvent.eventName))
    } else if (triggerEvent.eventValue.isDefined) {
      ctx.response().putHeader(triggerHeader, encodeHeaderValue(JsonObject.of(triggerEvent.eventName, triggerEvent.eventValue.get).encode()))
    } else if (triggerEvent.eventDetail.isDefined) {
      val payload = new JsonObject()
      payload.put(triggerEvent.eventName, triggerEvent.eventDetail.get)
      ctx.response().putHeader(triggerHeader, encodeHeaderValue(payload.encode()))
    }
  }

  private def encodeHeaderValue(headerValue:String) :String = URLEncoder.encode(headerValue, StandardCharsets.UTF_8)


  // ----------------------- Helpers Utilities -----------------------------
  private def getHeaderValueOf(header: String, ctx: RoutingContext): String = StringUtils.trimToEmpty(ctx.request().getHeader(header))

  private def option(value: String): Option[String] = if (StringUtils.isEmpty(value)) None else Some(value)
}