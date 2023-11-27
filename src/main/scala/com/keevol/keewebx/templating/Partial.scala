package com.keevol.keewebx.templating

import gg.jte.Content
import io.vertx.core.json.JsonObject

/**
 * refer to project /Users/fq/workspace.keevol/KeevolAdminWebStarter for demo usage.
 */
object Partial {
  def apply(templatePath: String, context: JsonObject): Content = Jte.createContent(templatePath, context)
}