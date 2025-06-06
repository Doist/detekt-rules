package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class ConsistentWhenEntriesTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports inconsistent when entries`() {
        val code = """
            val a = when {
                1 == 1 -> {
                    true
                }
                else -> false
            }
        """
        val rule = ConsistentWhenEntries(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `doesn't report multiline when entries`() {
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
        val rule = ConsistentWhenEntries(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `doesn't report single line when entries`() {
        val code = """
            val a = when {
                1 == 1 -> true
                else -> false
            }
        """
        val rule = ConsistentWhenEntries(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `doesn't report single line when entries with multiple conditions and trailing comma`() {
        val code = """
            val a = listOf<Int>()
            val b = when(a) {
                is ArrayList,
                is MutableList,
                -> true
                else -> false
            }
        """
        val rule = ConsistentWhenEntries(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `report inconsistent when entries with multiple conditions and trailing comma`() {
        val code = """
            val a = listOf<Int>()
            val b = when(a) {
                is ArrayList,
                is MutableList,
                -> {
                    true
                }
                else -> false
            }
        """
        val rule = ConsistentWhenEntries(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }
}
