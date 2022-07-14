package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.isPrivate

class MutableObservablePropertyIsPrivate(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Defect,
        "Detects exposed MutableStateFlow, MutableLiveData and similar",
        Debt.FIVE_MINS,
    )

    private val mutableTypePrefixes = listOf("MutableLive", "MutableStateFlow")

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)

        val type = property.guessType() ?: return
        if (mutableTypePrefixes.any { type.startsWith(it) } && !property.isPrivate()) {
            val codeSmell = CodeSmell(
                issue = issue,
                entity = Entity.from(property),
                message = "${property.name} should be private.")
            report(codeSmell)
        }
    }

    // Guess type from type reference or infer it from the initializer.
    private fun KtProperty.guessType() = typeReference?.text ?: initializer?.firstChild?.text
}
