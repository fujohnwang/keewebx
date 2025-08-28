package com.keevol.keewebx.htmx

/**
 * <pre>
 * :::    ::: :::::::::: :::::::::: :::     :::  ::::::::  :::
 * :+:   :+:  :+:        :+:        :+:     :+: :+:    :+: :+:
 * +:+  +:+   +:+        +:+        +:+     +:+ +:+    +:+ +:+
 * +#++:++    +#++:++#   +#++:++#   +#+     +:+ +#+    +:+ +#+
 * +#+  +#+   +#+        +#+         +#+   +#+  +#+    +#+ +#+
 * #+#   #+#  #+#        #+#          #+#+#+#   #+#    #+# #+#
 * ###    ### ########## ##########     ###      ########  ##########
 * </pre>
 * <p>
 * KEEp eVOLution!
 * <p>
 *
 * @author fq@keevol.cn
 * @since 2017.5.12
 * <p>
 * Copyright 2017 © 杭州福强科技有限公司版权所有 (<a href="https://www.keevol.cn">keevol.cn</a>)
 */

import com.keevol.keewebx.utils.WebResponse
import io.vertx.ext.web.RoutingContext

/**
 * util class to handle web response in htmx spec.
 */
object Hx {

  /**
   * send back response in html and trigger local js function at same time.
   *
   * So a local js function should be defined in the page, so that it can be triggered.
   *
   * @param ctx routing content handle
   * @param html content to send back to client in html format
   * @param localFuncToRun local js function definition name
   */
  def ok(ctx: RoutingContext, html: String, localFuncToRun: Option[String] = None): Unit = {
    if (localFuncToRun.isDefined) {
      HTMX.setTrigger(ctx, localFuncToRun.get)
    }
    WebResponse.html(ctx, html)
  }

  /**
   *     htmx.on("done", function(evt){
   *         Swal.fire({
   *           title: decodeURIComponent(evt.detail.value),
   *           icon: "success"
   *         })
   *     })
   * @param ctx
   * @param message
   */
  def done(ctx: RoutingContext, message: String): Unit = {
    HTMX.setTrigger(ctx, HxTrigger("done", eventValue = Option(message)))
    WebResponse.ok(ctx)
  }

  /**
   *     htmx.on("oops", function(evt){
   *         Swal.fire({
   *           title: decodeURIComponent(evt.detail.value),
   *           icon: "error"
   *         });
   *     })
   *
   * @param ctx routing context handle to use
   * @param errorMessage the notification message on error
   */
  def oops(ctx: RoutingContext, errorMessage: String): Unit = {
    HTMX.setTrigger(ctx, HxTrigger("oops", eventValue = Option(errorMessage)))
    WebResponse.ok(ctx)
  }

}