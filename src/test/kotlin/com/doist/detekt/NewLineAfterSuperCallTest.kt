package com.doist.detekt


import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.lint
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class NewLineAfterSuperCallTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `should report when there is no blank line after super() call`() {
        val code = """
            override fun foo() {
                super.foo()
                bar.bazinga()
            }
        """

        val findings = NewLineAfterSuperCall().lint(code)
        assertEquals(1, findings.size)
        assertEquals("There should be a new line after the call to super().", findings.first().message)
    }

    @Test
    fun `should not report when there is a blank line after super() call`() {
        val code = """
            override fun foo() {
                super.foo()

                bar.bazinga()
            }
        """

        val findings = NewLineAfterSuperCall().lint(code)
        assertEquals(0, findings.size)
    }

    @Test
    fun `should not report when super() call is the last line`() {
        val code = """
            override fun foo() {
                bar.bazinga()

                super.foo()
            }
        """

        val findings = NewLineAfterSuperCall().lint(code)
        assertEquals(0, findings.size)
    }

    @Test
    fun `should not report when super() call is the only line`() {
        val code = """
            override fun foo() {
                super.foo()
            }
        """

        val findings = NewLineAfterSuperCall().lint(code)
        assertEquals(0, findings.size)
    }
}
