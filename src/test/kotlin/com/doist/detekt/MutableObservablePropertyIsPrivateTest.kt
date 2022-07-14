package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class MutableObservablePropertyIsPrivateTest(private val env: KotlinCoreEnvironment) {
    @Test
    fun `reports public mutable property`() {
        val code = """
        val a: MutableLiveData<String>
        val b: MutableLiveData<String>
        val c: MutableLiveData<String>
        """
        val rule = MutableObservablePropertyIsPrivate(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 3
    }

    @Test
    fun `reports initialized public mutable property`() {
        val code = """
        val a = MutableLiveData<String>()
        val b = MutableStateFlow<String>()
        val c = MutableLiveEvent<String>()
        """
        val rule = MutableObservablePropertyIsPrivate(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 3
    }

    @Test
    fun `reports protected mutable property`() {
        val code = """
        protected val a: MutableLiveData<String>
        protected val b: MutableLiveData<String>
        protected val c: MutableLiveData<String>
        """
        val rule = MutableObservablePropertyIsPrivate(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 3
    }

    @Test
    fun `reports initialized protected mutable property`() {
        val code = """
        protected val a = MutableLiveData<String>()
        protected val b = MutableStateFlow<String>()
        protected val c = MutableLiveEvent<String>()
        """
        val rule = MutableObservablePropertyIsPrivate(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 3
    }

    @Test
    fun `doesn't reports private mutable property`() {
        val code = """
        private val a: MutableLiveData<String>
        private val b: MutableStateFlow<String>
        private val c: MutableLiveEvent<String>
        """
        val rule = MutableObservablePropertyIsPrivate(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `doesn't reports private initialized mutable property`() {
        val code = """
        private val a = MutableLiveData<String>()
        private val b = MutableStateFlow<String>()
        private val c = MutableLiveEvent<String>()
        """
        val rule = MutableObservablePropertyIsPrivate(Config.empty)
        val findings = rule.compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }
}
