package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class NoNotNullOperatorTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports !! usage in the assignment`() {
        val code = """
            val a: String? = ""
            val b = a!!
        """
        val rule = NoNotNullOperator(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `reports !! usage before calling a method`() {
        val code = """
            val a: String? = ""
            val b = a!!.length
        """
        val rule = NoNotNullOperator(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `doesn't reports requireNoteNull usage`() {
        val code = """
            val a: String? = ""
            val b = requireNotNull(a)
        """
        val rule = NoNotNullOperator(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }
}
