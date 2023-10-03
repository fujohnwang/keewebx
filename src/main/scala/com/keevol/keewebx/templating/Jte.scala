package com.keevol.keewebx.templating

import gg.jte.{ContentType, TemplateEngine}
import gg.jte.output.StringOutput
import io.vertx.core.json.JsonObject

import java.util.concurrent.atomic.AtomicReference
import com.keevol.keewebx.KeewebxGlobals._
import gg.jte.resolve.DirectoryCodeResolver
import org.slf4j.LoggerFactory

import java.nio.file.Path
/**
 * @author {@link afoo.me}
 */
object Jte {
  private val logger = LoggerFactory.getLogger("Jte Utility")

  // init template engine at start

  private val templateEngine: AtomicReference[TemplateEngine] = new AtomicReference[TemplateEngine]()
  templateEngine.set(if (isProductionEnv()) {
    logger.info(s"profile=production, create Precompiled TemplateEngine for Jte.")
    TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html, getClass.getClassLoader())
  } else {
    logger.info(s"profile is not production, so create hot-reloadable TemplateEngine for Jte in Development phase.")
    val codeResolver = new DirectoryCodeResolver(Path.of("src", "main", "jte")) // ResourceCodeResolver
    TemplateEngine.create(codeResolver, ContentType.Html)
  })

  def render(templatePath: String, context: JsonObject): String = {
    val output = new StringOutput()
    templateEngine.get().render(templatePath, context, output)
    output.toString
  }


  def getJteTemplateEngineIfNecessary(): TemplateEngine = templateEngine.get()

  def main(args: Array[String]): Unit = {
    println(Jte.render("test.jte", new JsonObject().put("message", "mock message")))
  }

}