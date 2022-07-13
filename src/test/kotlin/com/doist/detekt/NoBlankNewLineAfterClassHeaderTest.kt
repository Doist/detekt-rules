package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class NoBlankNewLineAfterClassHeaderTest(private val env: KotlinCoreEnvironment) {

    @Test
    fun `reports blank line after class header`() {
        val code = """
        class A {
          
          val b = true
        }
        """
        val rule = NoBlankNewLineAfterClassHeader(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `doesn't report blank line after class header`() {
        val code = """
        class A {
          val b = true
        }
        """
        val rule = NoBlankNewLineAfterClassHeader(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }
}
