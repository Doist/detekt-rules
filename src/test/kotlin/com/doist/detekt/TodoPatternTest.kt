package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class TodoPatternTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports invalid todo`() {
        val code = """
            // TODO: Invalid todo.
            // TODO(Piotr) Invalid todo.
        """
        val config = TestConfig(mapOf(TodoPattern.PATTERN to "// TODO(.+): .*"))
        val rule = TodoPattern(config)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }

    @Test
    fun `doesn't report correct todo`() {
        val code = """
            // TODO(Piotr): valid todo.
        """
        val config = TestConfig(mapOf(TodoPattern.PATTERN to "// TODO(.+): .*"))
        val rule = TodoPattern(config)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `accepts all TODOs if there is no pattern`() {
        val code = """
            // TODO fix this.
            // some other comment.
        """
        val rule = TodoPattern(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }
}
