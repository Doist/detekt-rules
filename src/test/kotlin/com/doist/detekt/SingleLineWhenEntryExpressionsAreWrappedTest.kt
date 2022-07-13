package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class SingleLineWhenEntryExpressionsAreWrappedTest(
    private val env: KotlinCoreEnvironment,
) {
    @Test
    fun `reports missing brackets in when entry expression`() {
        val code = """
        val a = when {
            1 == 1 -> 
                true
            else -> {
                false
            }
        }
        """
        val rule = SingleLineWhenEntryExpressionsAreWrapped(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `doesn't report when brackets are not needed`() {
        val code = """
        val a = when {
            1 == 1 -> true
            else -> false
        }
        """
        val rule = SingleLineWhenEntryExpressionsAreWrapped(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `doesn't report single line when entries with brackets`() {
        val code = """
        val a = when {
            1 == 1 -> {
                true
            }
            else -> {
                false
            }
        }
        """
        val rule = SingleLineWhenEntryExpressionsAreWrapped(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }
}
