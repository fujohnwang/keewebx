package com.keevol.keewebx.routes

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler

class PublicStaticResourcesRouteRegister extends AbstractRouteRegister {
  override def apply(router: Router): Unit = {
    router.route("/public/*").handler(StaticHandler.create("public"))
    router.route("/css/*").handler(StaticHandler.create("public/css"))
    router.route("/js/*").handler(StaticHandler.create("public/js"))
    router.route("/images/*").handler(StaticHandler.create("public/images"))
  }
}