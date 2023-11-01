package com.keevol.keewebx

import com.keevol.kate.{Kate, RouteRegister}
import com.keevol.keewebx.templating.Jte
import com.keevol.keewebx.utils.WebResponse
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import org.slf4j.{Logger, LoggerFactory}

/**
 * @author {@link afoo.me}
 */
object Bootstrap {
  val logger: Logger = LoggerFactory.getLogger("Keewebx")

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
      }
    }))
    logger.info(s"start keewebx server at ${host}:${port}")
    webServer.start(host, port.toInt)
  }
}