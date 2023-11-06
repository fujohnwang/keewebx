package com.keevol.keewebx.utils

import com.google.common.hash.Hashing
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink._
import io.vertx.core.http.{Cookie, CookieSameSite, HttpHeaders}
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory

import java.io.{File, FileOutputStream}
import java.nio.charset.StandardCharsets
import java.util.{Base64, UUID}

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
 * @author fq@keevol.cn
 * @since 2017.5.12
 *
 *        Copyright 2017 © 杭州福强科技有限公司版权所有
 *        [[https://www.keevol.cn]]
 */

/**
 * both token and sand should be stored in local cookie with user session.
 *
 * we don't store such things on server side which may cause too much central states.
 *
 * @param token
 * @param sand
 */
case class CsrfToken(val token: String, val sand: String)

class CsrfTokenManager(password: String = "set your password to protect the sand send to user local") {
  private val logger = LoggerFactory.getLogger(getClass.getName)

  private val defaultEncoding = StandardCharsets.UTF_8
  private val keysetHandleFilename = "keewebx-csrftoken-keyset"
  private val ad = "keewebx".getBytes(defaultEncoding)

  AeadConfig.register()
  val keysetHandleFile = new File(keysetHandleFilename)
  if (!keysetHandleFile.exists()) {
    val newKeysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES256_GCM"))
    CleartextKeysetHandle.write(newKeysetHandle, BinaryKeysetWriter.withOutputStream(new FileOutputStream(keysetHandleFile)))
  }

  val aead = CleartextKeysetHandle.read(BinaryKeysetReader.withBytes(FileUtils.readFileToByteArray(keysetHandleFile))).getPrimitive(classOf[Aead])

  private def generate(context: String): CsrfToken = {
    val sand = UUID.randomUUID().toString
    CsrfToken(Hashing.sha1().hashString(context + sand, StandardCharsets.UTF_8).toString, encrypt(sand))
  }

  private def validate(token: CsrfToken, context: String): Boolean = {
    try {
      val hashValue = Hashing.sha1().hashString(context + decrypt(token.sand), StandardCharsets.UTF_8).toString
      if (StringUtils.equals(hashValue, token.token)) true else false
    } catch {
      case t: Throwable => {
        logger.info(s"something goes wrong when validating csrf token: ${token}, context=${context} \n${ExceptionUtils.getStackTrace(t)}")
        false
      }
    }
  }

  val CSRF_TOKEN_S_COOKIE_NAME = "KEE_CSRF_TOKEN_S"
  val CSRF_TOKEN_F_NAME = "csrf_token"

  /**
   * place encrypted sand in cookie and return csrf token value to embed in form.
   *
   * the encrypted sand can be replaced with hmac value instead.
   *
   * @param ctx of web
   * @return generated csrf token value
   */
  def issue(ctx: RoutingContext): String = {
    val csrfToken = generate(ctx.request().getHeader(HttpHeaders.USER_AGENT))
    ctx.response().addCookie(strictCookie(Cookie.cookie(CSRF_TOKEN_S_COOKIE_NAME, csrfToken.sand)))
    csrfToken.token
  }

  private def checkValidation(ctx: RoutingContext): Option[CsrfToken] = {
    val tokenValue = StringUtils.trimToEmpty(ctx.request().getParam(CSRF_TOKEN_F_NAME))
    val sCookie = ctx.request().getCookie(CSRF_TOKEN_S_COOKIE_NAME)
    if (StringUtils.isEmpty(tokenValue) || sCookie == null) {
      return None
    }

    val csrfToken = CsrfToken(tokenValue, sCookie.getValue)
    if (!validate(csrfToken, ctx.request().getHeader(HttpHeaders.USER_AGENT))) {
      return None
    }
    ctx.response().removeCookie(CSRF_TOKEN_S_COOKIE_NAME)
    Some(csrfToken)
  }

  def isCsrfTokenValid(ctx: RoutingContext): Boolean = checkValidation(ctx).isDefined

  private def encrypt(str: String): String = Base64.getEncoder.encodeToString(aead.encrypt(str.getBytes(defaultEncoding), ad))

  private def decrypt(str: String): String = new String(aead.decrypt(Base64.getDecoder.decode(str), ad), defaultEncoding)

  def strictCookie(cookie: Cookie): Cookie = {
    cookie.setSameSite(CookieSameSite.STRICT)
    cookie
  }
}

object CsrfTokens {

  val NAME = "csrf_token"

  // how to use in jte: `$unsafe{CsrfTokens.hiddenInputFrom(jsonObjectContext)}`
  def hiddenInputFrom(pageContext: JsonObject): String = {
    s"""<input type="hidden" name="${NAME}" value="${pageContext.getString(NAME)}"/>"""
  }

  def main(args: Array[String]): Unit = {
    val csrfTokenManager = new CsrfTokenManager

    // 1. at get request of the from
    //    csrfTokenManager.distribute(ctx)

    // 2. at post request of the form
    //    if (csrfTokenManager.checkValidation(ctx).isEmpty) {
    //      throw new IllegalAccessException("please refresh the page and retry")
    //    }
  }
}