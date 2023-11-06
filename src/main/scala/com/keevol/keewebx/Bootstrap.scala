package com.keevol.keewebx

import com.keevol.goodies.lifecycle.ShutdownHook
import com.keevol.kate.{Kate, RouteRegister}
import com.keevol.keewebx.templating.Jte
import com.keevol.keewebx.utils.{CsrfTokenManager, CsrfTokens, WebResponse}
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import org.slf4j.{Logger, LoggerFactory}

import java.util.Date

/**
 * @author {@link afoo.me}
 */
object Bootstrap {
  val logger: Logger = LoggerFactory.getLogger("Keewebx")

  val csrfTokenManager = new CsrfTokenManager("change password to use in production")

  def main(args: Array[String]): Unit = {
    val config = KeewebxGlobals.config.get()

    val host = config.get("server.host")
    val port = config.get("server.port")

    val webServer = new Kate(Array(new RouteRegister {
      override def apply(router: Router): Unit = {
        router.route("/").handler(ctx => ctx.response().end("fuck it"))
        router.route("/html").handler(ctx => {
          WebResponse.html(ctx, Jte.render("test.jte", new JsonObject().put("message", "mock message")))
        })
        router.get("/form_submit").handler(ctx => {
          val csrfToken = csrfTokenManager.issue(ctx)
          WebResponse.html(ctx, Jte.render("form.jte", new JsonObject().put(CsrfTokens.NAME, csrfToken)))
        })
        router.post("/form_submit").handler(ctx => {
          if (csrfTokenManager.isCsrfTokenValid(ctx)) {
            WebResponse.html(ctx, "OK")
          } else {
            WebResponse.html(ctx, s"oops, csrf token expires at ${new Date().getTime}")
          }
        })
      }
    }))
    ShutdownHook.add(() => webServer.stop())
    logger.info(s"start keewebx server at ${host}:${port}")
    webServer.start(host, port.toInt)

  }
}