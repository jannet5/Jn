package com.jn.winremote.crypto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

/**
 * Exercises the real pinning trust manager against a real, self-signed
 * X.509 certificate (generated once with `openssl req -x509 ...`, its PEM
 * embedded below). The expected fingerprint was computed independently
 * with:
 *   openssl x509 -in test-cert.pem -pubkey -noout | openssl pkey -pubin -outform DER | openssl dgst -sha256
 * matching PROTOCOL.md §1's definition (SHA-256 of the DER SubjectPublicKeyInfo).
 */
class CertPinningTest {

    private val testCertPem = """
        -----BEGIN CERTIFICATE-----
        MIIDGzCCAgOgAwIBAgIUD8ne0M9D+BN1/PlrQI3RixxHQwkwDQYJKoZIhvcNAQEL
        BQAwHTEbMBkGA1UEAwwSV2luUmVtb3RlVGVzdEFnZW50MB4XDTI2MDkyNzExMDY1
        N1oXDTM2MDkyNDExMDY1N1owHTEbMBkGA1UEAwwSV2luUmVtb3RlVGVzdEFnZW50
        MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt7cXUcHOEuYIIULRQ/bl
        sRl+e60v8UrhysP5d7rkecB8saU3YeCkqIe5PERnxyjsW52lozOLcbnBm+UXDZuB
        dEKRlMsDoYKqQ2vxt4tqLyUy43EhiCv6/ikJ4WXvbcQIlHYeXlOq7bJz22PP1VwA
        UfYRlGJh6sGIa3eFIxu6noAWxFCoYEuAGslAqmn3LE1R5F2jfmoL80eYF6lMYlsS
        6aWpVSh2nDE7w91m/x2XmW/z89q73R1EAZTn2G/JpPyk43TR2tC5mJR1L1IraywR
        oDO4jA0oF62szzIjftj5pRfnVjB2liYlYDdgJb4XnF5IEdg1zrVPbrpfMdY0nJjf
        1wIDAQABo1MwUTAdBgNVHQ4EFgQUX2fLune15WFxpPnIF6gUQSnVc+IwHwYDVR0j
        BBgwFoAUX2fLune15WFxpPnIF6gUQSnVc+IwDwYDVR0TAQH/BAUwAwEB/zANBgkq
        hkiG9w0BAQsFAAOCAQEAYNuVdggMI4qzSJpZ4+LQH0/csaoj/9KTz2WPwVG5v+dc
        kJF1W2bsJ//Bi25gIRMruXikhsAKfThqDHam9vbitIDKQqsXPtDN7C0NTh5aD+KY
        fHkPfF7qHaGH8IXQZJ8vGA7wNK9kSMNCHazqrPHEPdhWFBUtho/nLBQpCov9onMg
        MvKwwcqGixijOw4EJghJl9WOzrzqvcxY4Q3wUsNIODDGdprRz7bnbHo1NEhIeCXA
        vYUwyRReK2s8aR+LYlogUh1rGrl7Sw1rZul0CrFGuNBswiXLd9kIUL7WKr09XsKF
        JTUxNcEcxy6f7MRJbnw902PEZ74vPUMvdeAQxuX+Tw==
        -----END CERTIFICATE-----
    """.trimIndent()

    private val expectedFingerprintHex = "5862968569fed300994b5bcfad98a15c91abcf3fb5aba9ab2d04776e3105e66d".let {
        // The command above yields exactly 64 hex chars (32 bytes); guard that assumption here too.
        require(it.length == 64)
        it
    }

    private fun loadTestCert(): X509Certificate {
        val factory = CertificateFactory.getInstance("X.509")
        return factory.generateCertificate(ByteArrayInputStream(testCertPem.toByteArray())) as X509Certificate
    }

    @Test
    fun `publicKeyFingerprintHex matches openssl-computed SPKI SHA-256`() {
        val cert = loadTestCert()
        assertEquals(expectedFingerprintHex, publicKeyFingerprintHex(cert))
    }

    @Test
    fun `normalizeFingerprint strips separators, spaces and case`() {
        val withColons = expectedFingerprintHex.chunked(2).joinToString(":").uppercase()
        assertEquals(expectedFingerprintHex, normalizeFingerprint(withColons))
        assertEquals(expectedFingerprintHex, normalizeFingerprint("  $expectedFingerprintHex  "))
    }

    @Test
    fun `trust manager accepts a certificate matching the pinned fingerprint`() {
        val manager = PinnedFingerprintTrustManager(expectedFingerprintHex)
        // Must not throw.
        manager.checkServerTrusted(arrayOf(loadTestCert()), "RSA")
    }

    @Test
    fun `trust manager accepts fingerprint pinned with colons and different case`() {
        val decorated = expectedFingerprintHex.chunked(2).joinToString(":").uppercase()
        val manager = PinnedFingerprintTrustManager(decorated)
        manager.checkServerTrusted(arrayOf(loadTestCert()), "RSA")
    }

    @Test
    fun `trust manager rejects a mismatched fingerprint`() {
        val wrongFingerprint = "0".repeat(64)
        val manager = PinnedFingerprintTrustManager(wrongFingerprint)
        val ex = assertThrows(CertificatePinningException::class.java) {
            manager.checkServerTrusted(arrayOf(loadTestCert()), "RSA")
        }
        assertTrue(ex.message!!.contains("sunucu kimliği doğrulanamadı"))
    }

    @Test
    fun `trust manager rejects an empty certificate chain`() {
        val manager = PinnedFingerprintTrustManager(expectedFingerprintHex)
        assertThrows(CertificateException::class.java) {
            manager.checkServerTrusted(emptyArray(), "RSA")
        }
    }

    @Test
    fun `trust manager never trusts client certificates (protocol has no client-cert auth)`() {
        val manager = PinnedFingerprintTrustManager(expectedFingerprintHex)
        assertThrows(CertificateException::class.java) {
            manager.checkClientTrusted(arrayOf(loadTestCert()), "RSA")
        }
    }

    @Test
    fun `hostname verifier intentionally trusts any hostname (pin is the real check)`() {
        assertTrue(TrustAllHostnames.verify("anything.invalid", null))
    }
}
