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

  /**
   * 这里有个问题， 因为是通过http header设置一个json字符串，但其实，header的编码是ISO-8859-1，所以， 中文字符会出现乱码。
   *
   * 编码也没有太好的方式，所以，这里的参数值最好都是英文的。
   *
   * ---
   *
   * NOTE: 配合客户端js函数里decodeURIComponent(evt.detail.value)， 我们通过在server side对json里的value进行uri encode， 现在可以实现中文内容通过header传递， 就是复杂json可能就没那么友好，还得在客户单decode之后加JSON.parse。
   *
   * 这也算是htmx这个框架坑的地方吧！
   *
   * @param ctx
   * @param triggerHeader
   * @param triggerEvent
   */
  private def setTriggerEvent(ctx: RoutingContext, triggerHeader: String, triggerEvent: HxTrigger): Unit = {
    if (triggerEvent.eventDetail.isEmpty && triggerEvent.eventValue.isEmpty) {
      ctx.response().putHeader(triggerHeader, triggerEvent.eventName)
    } else if (triggerEvent.eventValue.isDefined) {
      ctx.response().putHeader(triggerHeader, JsonObject.of(triggerEvent.eventName, encodeHeaderValue(triggerEvent.eventValue.get)).encode())
    } else if (triggerEvent.eventDetail.isDefined) {
      val payload = new JsonObject()
      payload.put(triggerEvent.eventName, encodeHeaderValue(triggerEvent.eventDetail.get.encode()))
      ctx.response().putHeader(triggerHeader, payload.encode())
    }
  }

  /**
   * JavaScript 的 encodeURIComponent 编码后不会对某些字符（如 !, ', (, ), *）进行转义，而 URLEncoder 可能会转义这些字符。如果需要完全匹配 encodeURIComponent 的行为，可以在编码后对这些字符进行反转义处理。
   *
   * @param headerValue
   * @return
   */
  def encodeHeaderValue(headerValue: String): String = URLEncoder.encode(headerValue, StandardCharsets.UTF_8.name())
    .replace("+", "%20")
    .replace("%21", "!")
    .replace("%27", "'")
    .replace("%28", "(")
    .replace("%29", ")")
    .replace("%2A", "*");


  // ----------------------- Helpers Utilities -----------------------------
  private def getHeaderValueOf(header: String, ctx: RoutingContext): String = StringUtils.trimToEmpty(ctx.request().getHeader(header))

  private def option(value: String): Option[String] = if (StringUtils.isEmpty(value)) None else Some(value)

  def main(args: Array[String]): Unit = {
    val json = JsonObject.of("oops", "请求参数不全 ！")
    println(encodeHeaderValue(json.encode()))
  }
}