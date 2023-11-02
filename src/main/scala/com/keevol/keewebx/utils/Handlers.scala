package com.keevol.keewebx.utils

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * {{{
 *   _                                    _
 *  | |                                  | |
 *  | | __   ___    ___  __   __   ___   | |
 *  | |/ /  / _ \  / _ \ \ \ / /  / _ \  | |
 *  |   <  |  __/ |  __/  \ V /  | (_) | | |
 *  |_|\_\  \___|  \___|   \_/    \___/  |_|
 * }}}
 *
 * KEEp eVOLution!
 *
 * @author fq@keevol.com
 * @since 2017.5.12
 *
 *        Copyright 2017 © 杭州福强科技有限公司版权所有
 *        [[https://www.keevol.com]]
 */

object Handlers {

  val logger: Logger = LoggerFactory.getLogger("Handlers")

  def safe(handler: Handler[RoutingContext]): Handler[RoutingContext] = { ctx =>
    try {
      handler.handle(ctx)
    } catch {
      case t: Throwable => {
        logger.warn(s"sth went wrong when processing web request with exception:\n${ExceptionUtils.getStackTrace(t)}")
        ctx.response().setStatusCode(500).setStatusMessage("my bad :(").end()
      }
    }
  }

  def async(handler: Handler[RoutingContext]): Handler[RoutingContext] = { ctx =>
    Future {
      handler.handle(ctx)
    }
  }

  def asyncV(handler: Handler[RoutingContext]): Handler[RoutingContext] = { ctx =>
    Thread.ofVirtual().name("keewebx handler virtual thread").uncaughtExceptionHandler(new Thread.UncaughtExceptionHandler {
      override def uncaughtException(t: Thread, e: Throwable): Unit = logger.warn(s"exception on virtual thread:${t.getName} \n${ExceptionUtils.getStackTrace(e)}")
    }).start(() => handler.handle(ctx))
  }

  def chain(handler: Handler[RoutingContext]): Handler[RoutingContext] =
    async {
      safe {
        handler
      }
    }

  def fastReturn[T](preprocessor: RoutingContext => T, action: T => Unit): Handler[RoutingContext] = { ctx =>
    val handlerContext = preprocessor(ctx) // take out parameters or body information before run action in async.
    Future {
      try {
        action(handlerContext)
      } catch {
        case t: Throwable => logger.warn(s"ignorable exception in fast return handler: \n${ExceptionUtils.getStackTrace(t)}")
      }
    }
    ctx.response().setStatusCode(202).setStatusMessage("Accepted").end()
  }

}