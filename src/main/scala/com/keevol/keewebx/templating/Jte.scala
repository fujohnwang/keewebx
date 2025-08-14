package com.keevol.keewebx.templating

import gg.jte.{Content, ContentType, TemplateEngine, TemplateOutput}
import gg.jte.output.{StringOutput, Utf8ByteOutput}
import io.vertx.core.json.JsonObject

import java.util.concurrent.atomic.AtomicReference
import com.keevol.keewebx.KeewebxGlobals._
import gg.jte.resolve.DirectoryCodeResolver
import org.slf4j.LoggerFactory

import java.nio.file.Path

/**
 * For General Usage Purpose.
 *
 * Most of the  time, we will use Jte's companion object directly.
 *
 * @param templateDir the directory to load jte template from.
 * @param templateContentType default is html.
 */
class Jte(templateDir: Path, templateContentType: ContentType = ContentType.Html) {
  private val templateEngine = TemplateEngine.create(new DirectoryCodeResolver(templateDir), templateContentType)

  def render(template: String, context: java.util.Map[String, Object], output: TemplateOutput): TemplateOutput = {
    templateEngine.render(template, context, output)
    output
  }
}

/**
 * @author fq@keevol.cn
 */
object Jte {
  private val logger = LoggerFactory.getLogger("Jte Utility")

  private val templateEngine: AtomicReference[TemplateEngine] = new AtomicReference[TemplateEngine]()
  // init template engine at start
  templateEngine.set(if (isProductionEnv()) {
    logger.info(s"profile=production, create Precompiled TemplateEngine for Jte.")
    val te = TemplateEngine.createPrecompiled(ContentType.Html)
    te.setBinaryStaticContent(true) // if we don't use Utf8ByteOutput, then the byte array will rollback to string for StringOutput
    te
  } else {
    logger.info(s"profile is not production, so create hot-reloadable TemplateEngine for Jte in Development phase.")
    val codeResolver = new DirectoryCodeResolver(Path.of("src", "main", "jte")) // ResourceCodeResolver
    TemplateEngine.create(codeResolver, Path.of("jte-classes"), ContentType.Html)
  })

  def apply[T](templatePath: String, context: T): String = {
    val output = new StringOutput()
    templateEngine.get().render(templatePath, context, output)
    output.toString
  }

  def render(templatePath: String, context: JsonObject): String = {
    val output = new StringOutput()
    templateEngine.get().render(templatePath, context, output)
    output.toString
  }

  /**
   * mainly for rendering binary output like images, audio, video, etc.
   *
   * @param templatePath path of jte template file
   * @param context view model data
   * @return the binary data representation.
   */
  def binaryRender(templatePath: String, context: JsonObject): Array[Byte] = {
    val output = new Utf8ByteOutput()
    templateEngine.get().render(templatePath, context, output)
    output.toByteArray
  }


  /**
   * @see also [[com.keevol.keewebx.templating.Partial]]
   *
   * @param templatePath path of jte template file
   * @param context view model data
   * @return Content of jte
   */
  def createContent(templatePath: String, context: JsonObject): Content = new Content {
    override def writeTo(templateOutput: TemplateOutput): Unit = {
      Jte.getJteTemplateEngineIfNecessary().render(templatePath, context, templateOutput)
    }
  }

  def getJteTemplateEngineIfNecessary(): TemplateEngine = templateEngine.get()

  def main(args: Array[String]): Unit = {
    println(Jte.render("test.jte", new JsonObject().put("message", "mock message")))
  }

}