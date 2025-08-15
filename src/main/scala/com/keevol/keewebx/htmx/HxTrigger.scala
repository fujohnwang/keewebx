package com.keevol.keewebx.htmx

import io.vertx.core.json.JsonObject

case class HxTrigger(eventName: String, eventDetail: Option[JsonObject] = None, eventValue: Option[String] = None)