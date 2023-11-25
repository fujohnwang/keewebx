package com.keevol.keewebx.templating

import gg.jte.Content
import io.vertx.core.json.JsonObject

object Partial {
  def apply(templatePath: String, context: JsonObject): Content = Jte.createContent(templatePath, context)
}