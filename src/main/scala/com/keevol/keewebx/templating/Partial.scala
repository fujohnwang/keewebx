package com.keevol.keewebx.templating

import gg.jte.Content
import io.vertx.core.json.JsonObject

/**
 * refer to project /Users/fq/workspace.keevol/KeevolAdminWebStarter for demo usage.
 */
object Partial {
  /**
   * 这个是从模板创建Content占位符内容， 但其实也可以直接实现Content实现类作为模板参数。
   *
   * @param templatePath
   * @param context
   * @return
   */
  def apply(templatePath: String, context: JsonObject): Content = Jte.createContent(templatePath, context)
}