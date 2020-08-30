package cozy.middleware.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.util.*

@KtorExperimentalAPI
fun Authentication.Configuration.jwtBearer() {
    val config = HoconApplicationConfig(ConfigFactory.load())

    val jwtIssuer = config.property("jwtBearer.issuer").getString()
    val jwtAudience = config.property("jwtBearer.audience").getString()
    val jwtRealm = config.property("jwtBearer.realm").getString()
    val jwtSecret = config.property("jwtBearer.secret").getString()

    val algorithm: Algorithm = Algorithm.HMAC256(jwtSecret)

    fun verify(audience: String, issuer: String): JWTVerifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    jwt {
        realm = jwtRealm
        verifier(verify(jwtAudience, jwtIssuer))
        validate { JWTPrincipal(it.payload) }
    }
}