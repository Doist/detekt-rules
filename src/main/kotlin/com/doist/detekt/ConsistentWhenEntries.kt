package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtWhenEntry
import org.jetbrains.kotlin.psi.KtWhenExpression

class ConsistentWhenEntries(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Style,
        "If one when entry is multiline, all other entries should be multiline.",
        Debt.FIVE_MINS,
    )

    override fun visitWhenExpression(expression: KtWhenExpression) {
        super.visitWhenExpression(expression)

        if (expression.entries.any { it.isMultiline() } &&
            !expression.entries.all { it.isMultiline() }
        ) {
            val codeSmell = CodeSmell(
                issue = issue,
                entity = Entity.from(expression),
                message = "Some when entries are single line, some are multiline",
            )
            report(codeSmell)
        }
    }

    private fun KtWhenEntry.isMultiline() = text.count { it == '\n'} > 1
}
